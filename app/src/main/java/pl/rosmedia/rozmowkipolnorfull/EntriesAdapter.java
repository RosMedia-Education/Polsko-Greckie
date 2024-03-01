package pl.rosmedia.rozmowkipolnorfull;

import java.util.List;

import android.content.Context;
import androidx.fragment.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class EntriesAdapter extends ArrayAdapter<Entry> {
	
	public Context context;
	public List<Entry> entries;
	public FragmentActivity act;
	
	public EntriesAdapter(Context context, int resource, List<Entry> entries, FragmentActivity act) {
		super(context, resource, entries);
		
		this.context = context;
		this.entries = entries;
		this.act = act;
	}
	
	@Override
	  public View getView(int position, View convertView, ViewGroup parent) {
	    LayoutInflater inflater = (LayoutInflater) context
	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    
	    final Entry entry = entries.get(position);
	    View rowView = inflater.inflate(R.layout.entry_row, parent, false);
	    
	    final TextView tvLeftWord = (TextView) rowView.findViewById(R.id.tvLeftWord);
	    tvLeftWord.setText(entry.first);
	    
	    final TextView tvRightWord = (TextView) rowView.findViewById(R.id.tvRightWord);
	    tvRightWord.setText(entry.second);
	    
//	    final LinearLayout entryLayout = (LinearLayout) rowView.findViewById(R.id.entryLayout);

	    return rowView;
	  }

}
