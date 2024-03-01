package pl.rosmedia.rozmowkipolnorfull;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class PhotoFragment extends Fragment {

	public String path = "";
	public String description = "";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_photo, container, false);
		
		ImageView imgPhoto = (ImageView) rootView.findViewById(R.id.imgPhoto);
		TextView tvPhotoIndex = (TextView) rootView.findViewById(R.id.tvPhotoIndex);
		tvPhotoIndex.setText(description);
		new AssetImageLoadTask(this.getActivity(), path, imgPhoto).execute();
		
		return rootView;
	}
}
