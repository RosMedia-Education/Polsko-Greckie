package pl.rosmedia.rozmowkipolnorfull;

import java.util.ArrayList;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class PhrasebookFragment extends Fragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_phrasebook, container, false);
		
		final ListView lvLessons = (ListView) rootView.findViewById(R.id.lvLessons);
		
		ArrayList<Lesson> lessons = new ArrayList<Lesson>();
		for(String[] lessonData : Data.LESSONS){
			lessons.add(new Lesson(lessonData[1]));
		}
		
		LessonsAdapter adapter = new LessonsAdapter(getActivity(), android.R.layout.simple_list_item_1, lessons, getActivity());
		lvLessons.setAdapter(adapter);
		lvLessons.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				LessonFragment fragment = new LessonFragment();
				fragment.lessonIndex = position;

		        FragmentManager fm = PhrasebookFragment.this.getActivity().getSupportFragmentManager();
		        FragmentTransaction ft = fm.beginTransaction();
		        ft.add(R.id.mainView, fragment);
		        ft.addToBackStack(null);
		        ft.commit();
				
			}
			
		});
		
		return rootView;
	}
}
