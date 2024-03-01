package pl.rosmedia.rozmowkipolnorfull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class LessonFragment extends Fragment {
	
	public int lessonIndex = 0;
	private ImageToogleButton tPlay;
	private SeekBar sbProgress;
	private TextView tvPosition;
	private ImageView imbBack;
	private ListView lvEntries;
	private EntriesAdapter adapter;
	 
	private MediaPlayer mediaPlayer;
	private enum MP_State {Idle, Initialized, Prepared, Started, Paused, Stopped, PlaybackCompleted, End, Error, Preparing}
	private MP_State mediaPlayerState;
	
	private int[][] times;
	private Runnable action;
	private boolean playWhole = true;
	private int actionEndTime = -1;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_lesson, container, false);
		
		imbBack = (ImageView) rootView.findViewById(R.id.imgBack);
		imbBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				LessonFragment.this.getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
			}
		});
		
		tPlay = (ImageToogleButton) rootView.findViewById(R.id.tPlay);
		tPlay.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(tPlay.isChecked()){
					cmdPause();
					tPlay.setChecked(false);
					
					if(action != null){
						lvEntries.removeCallbacks(action);
					}
				}else{
					cmdStart();
					tPlay.setChecked(true);
					playWhole = true;
				}
			}
		});
		
		tvPosition = (TextView) rootView.findViewById(R.id.tvPosition);
		tvPosition.setText("0:00");
		sbProgress = (SeekBar) rootView.findViewById(R.id.sbProgress);
		
		cmdReset();
		AssetFileDescriptor descriptor;
		try {
			descriptor = getActivity().getAssets().openFd((lessonIndex + 1) + ".mp3");
			cmdSetDataSource(descriptor);
	        descriptor.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		cmdPrepare();
		
		final Handler monitorHandler = new Handler(){

			  @Override
			  public void handleMessage(Message msg) {
				  mediaPlayerMonitor();
			  }
			     
		};
		
		ScheduledExecutorService myScheduledExecutorService = Executors.newScheduledThreadPool(1);
        
        myScheduledExecutorService.scheduleWithFixedDelay(
        		new Runnable(){
        			@Override
        			public void run() {
        				monitorHandler.sendMessage(monitorHandler.obtainMessage());
        			}
        		}, 
		        100, //initialDelay
		        100, //delay
		        TimeUnit.MILLISECONDS);
        
        sbProgress.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				if(fromUser){
					cmdSeekTo(progress * 1000);
				}
			}
		});
        
        times = new int[Data.TIMES[lessonIndex].length / 2][];
        for(int i = 0; i < Data.TIMES[lessonIndex].length; i+=2){
        	
        	float startTime = Float.parseFloat(Data.TIMES[lessonIndex][i]);
        	float duration = Float.parseFloat(Data.TIMES[lessonIndex][i + 1]);
        	
        	times[i / 2] = new int[2];
        	
        	times[i / 2][0] = (int) (startTime * 1000);
        	times[i / 2][1] = (int) (duration * 1000);
        }
        
        ArrayList<Entry> entries = LessonParser.parse(getActivity().getApplicationContext(), Data.LESSONS[lessonIndex][0], 
				Data.LESSONS[lessonIndex][3], Data.XML_FIRST_ITEM, Data.XML_SECOND_ITEM);
		
		if(entries != null){
			lvEntries = (ListView) rootView.findViewById(R.id.lvEntries);
			adapter = new EntriesAdapter(getActivity(), android.R.layout.simple_list_item_single_choice, entries, getActivity());
			lvEntries.setAdapter(adapter);
			lvEntries.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
			
			lvEntries.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {				
					cmdSeekTo(times[position][0]);
					cmdStart();
					
					tPlay.setChecked(true);
					
					if(action != null){
						lvEntries.removeCallbacks(action);
					}
					action = new Runnable(){
						@Override
						public void run() {
							cmdPause();
							tPlay.setChecked(false);
							int oldChecked = lvEntries.getCheckedItemPosition();
							if(oldChecked >= 0 && oldChecked < adapter.getCount()){
								lvEntries.setItemChecked(oldChecked, false);
							}
							actionEndTime = -1;
						}
					};
					
					actionEndTime = times[position][0] + times[position][1];
					
//					lvEntries.postDelayed(action, times[position][1]);
					
					playWhole = false;
				}
				
			});
		}
		
		return rootView;
	}
	
	private void cmdReset(){
		if(mediaPlayer == null){
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setOnErrorListener(new OnErrorListener() {
				
				@Override
				public boolean onError(MediaPlayer mp, int what, int extra) {
					mediaPlayerState = MP_State.Error;
					Log.e("MediaPlayer Error", "What: " + what + ", extra: " + extra);
					return false;
				}
			});
	    }
		
	    mediaPlayer.reset();
	    mediaPlayerState = MP_State.Idle;
	}
	
	private void cmdSetDataSource(AssetFileDescriptor descriptor){
		
	     if(mediaPlayerState == MP_State.Idle){
			try {
				mediaPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
				mediaPlayerState = MP_State.Initialized;
			}
			catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
			catch (IllegalStateException e) {
				e.printStackTrace();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	     
	}
	
	private void cmdPrepare(){
	     
		if(mediaPlayerState == MP_State.Initialized || mediaPlayerState == MP_State.Stopped){
			try {
				mediaPlayer.prepare();
				mediaPlayerState = MP_State.Prepared;
			} 
			catch (IllegalStateException e) {
	    	  e.printStackTrace();
			}
			catch (IOException e) {
			  e.printStackTrace();
			}
	     }
		
	}
	
	private void cmdStart(){
		
	     if(mediaPlayerState == MP_State.Prepared || mediaPlayerState == MP_State.Started 
	    		 || mediaPlayerState == MP_State.Paused || mediaPlayerState == MP_State.PlaybackCompleted){
	    	 mediaPlayer.start();
	    	 mediaPlayerState = MP_State.Started;
	     }
	     
	}
	
	private void cmdPause(){
		
		if(mediaPlayerState == MP_State.Started || mediaPlayerState == MP_State.Paused){
			mediaPlayer.pause();
			mediaPlayerState = MP_State.Paused;
		}
		
	}
	    
	private void cmdStop(){
	  
	  if(mediaPlayerState == MP_State.Prepared || mediaPlayerState == MP_State.Started || mediaPlayerState == MP_State.Stopped
	          || mediaPlayerState == MP_State.Paused || mediaPlayerState == MP_State.PlaybackCompleted){
		  	mediaPlayer.stop();
		  	mediaPlayerState = MP_State.Stopped;
	  }

	}
	    
	private void cmdSeekTo(int msec){
		
		if(mediaPlayerState == MP_State.Prepared || mediaPlayerState == MP_State.Started || mediaPlayerState == MP_State.Paused
	       ||mediaPlayerState == MP_State.PlaybackCompleted){
	      mediaPlayer.seekTo(msec);
	    }
	     
	}
	
	private void mediaPlayerMonitor(){
		
		if (mediaPlayer != null){
			if(mediaPlayer.isPlaying()){

				int position = mediaPlayer.getCurrentPosition();
				int mediaDuration = mediaPlayer.getDuration() / 1000;
				int mediaPosition = position / 1000;
				sbProgress.setMax(mediaDuration);
				sbProgress.setProgress(mediaPosition);
				
				int seconds = mediaPosition;
				int minutes = seconds / 60;
				seconds -= minutes * 60;
				
				tvPosition.setText(Integer.toString(minutes) + ":" + String.format("%02d", seconds));
				
				if(playWhole){
					
					int oldChecked = lvEntries.getCheckedItemPosition();
				
					for(int i = 0; i < times.length; i++){
						int startTime = times[i][0];
						int endTime = startTime + times[i][1];
						if(position >= startTime && position <= endTime){
							
							if(oldChecked != i && oldChecked >= 0 && oldChecked < adapter.getCount()){
								lvEntries.setItemChecked(oldChecked, false);
							}
							lvEntries.setItemChecked(i, true);
							break;
						}
					}
				
				}
				else{
					if(actionEndTime != -1 && position >= actionEndTime && action != null){
						action.run();
					}
				}
			}
			else{
				int oldChecked = lvEntries.getCheckedItemPosition();
				if(oldChecked >= 0 && oldChecked < adapter.getCount()){
					lvEntries.setItemChecked(oldChecked, false);
				}
				tPlay.setChecked(false);
			}
		
	    }
		
	}
	
	@Override
	public void onDestroy() {
		cmdStop();
		mediaPlayer.release();
		mediaPlayer = null;
		super.onDestroy();
	}
}
