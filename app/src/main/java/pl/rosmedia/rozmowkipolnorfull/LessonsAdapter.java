package pl.rosmedia.rozmowkipolnorfull;

import java.util.List;

import android.content.Context;
import androidx.fragment.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class LessonsAdapter extends ArrayAdapter<Lesson> {
	
	public Context context;
	public List<Lesson> lessons;
	public FragmentActivity act;
	
	public LessonsAdapter(Context context, int resource, List<Lesson> lessons, FragmentActivity act) {
		super(context, resource, lessons);
		
		this.context = context;
		this.lessons = lessons;
		this.act = act;
	}
	
	@Override
	  public View getView(int position, View convertView, ViewGroup parent) {
	    LayoutInflater inflater = (LayoutInflater) context
	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    
	    final Lesson lesson = lessons.get(position);
	    View rowView = inflater.inflate(R.layout.lesson_row, parent, false);
	    
	    final TextView tvLessonName = (TextView) rowView.findViewById(R.id.tvLessonName);
	    tvLessonName.setText("• " + lesson.name);

	    return rowView;
	  }

}
