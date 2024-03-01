package pl.rosmedia.rozmowkipolnorfull;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {
	
	private static final int CLOSE_WARNING_TIME = 3000;
	public static final String SKU = "full";
	public String token = "";
	
	private ViewPager mainViewPager;
	private MainPagerAdapter mAdapter;
	private View vIntro;
	private View vInfo;
	private View vPhrasebook;
	private View vExtras;
	private long timeStamp;
	
	public SharedPreferences prefs;
	
	public ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage(getResources().getString(R.string.please_wait));
		
		mainViewPager = (ViewPager) findViewById(R.id.pager);			
		mAdapter = new MainPagerAdapter(this, getSupportFragmentManager());
		mainViewPager.setAdapter(mAdapter);
		
		vIntro = (View) findViewById(R.id.itab1);
		vInfo = (View) findViewById(R.id.itab2);
		vPhrasebook = (View) findViewById(R.id.itab3);
		vExtras = (View) findViewById(R.id.itab4);
		
		vIntro.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mainViewPager.setCurrentItem(0);
			}
		});
		vInfo.setOnClickListener(new OnClickListener() {
					
			@Override
			public void onClick(View v) {
				mainViewPager.setCurrentItem(1);
			}
		});
		vPhrasebook.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mainViewPager.setCurrentItem(2);
				
			}
		});
		vExtras.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mainViewPager.setCurrentItem(3);
			}
		});

		mainViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				
				for(int i = 0; i < 4; i++){
					String labelName = "itab" + (i + 1) + "label";
					int resID = getResources().getIdentifier(labelName, "id", getPackageName());
					TextView tv = (TextView) MainActivity.this.findViewById(resID);
					if(i == position){
						tv.setTextColor(Color.WHITE);
					}
					else{
						tv.setTextColor(Color.BLACK);
					}
				}
				
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
		
		mainViewPager.setOffscreenPageLimit(5);
	}

	public FragmentManager getLastFragmentManager(){
		return getSupportFragmentManager();
	}
	
	@Override
	public void onBackPressed() {
		
		if(getSupportFragmentManager().getBackStackEntryCount() == 0){
			
			long now = System.currentTimeMillis();

	        if((now - timeStamp) <= CLOSE_WARNING_TIME){
	        	super.onBackPressed();
	        }
	        else{
	        	Toast.makeText(this, this.getResources().getString(R.string.exit_warning), Toast.LENGTH_LONG).show();
	        	timeStamp = System.currentTimeMillis();
	        }
			
		}
		else{
			getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
		}
		
	}
	
}
