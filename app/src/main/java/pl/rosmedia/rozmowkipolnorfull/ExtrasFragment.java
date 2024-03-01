package pl.rosmedia.rozmowkipolnorfull;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ExtrasFragment extends Fragment {

	private ImageView imgTwitter;
	private ImageView imgFacebook;
	private ImageView imgRate;
	private ImageView imgEmail;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_extras, container, false);
		
		imgTwitter = (ImageView) rootView.findViewById(R.id.imgTwitter);
		imgFacebook = (ImageView) rootView.findViewById(R.id.imgFacebook);
		imgRate = (ImageView) rootView.findViewById(R.id.imgRate);
		imgEmail = (ImageView) rootView.findViewById(R.id.imgEmail);
		
		imgTwitter.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
					openURL(Data.TWITTER_APP_URL);
				}
				catch (Exception e) {
				    openURL(Data.TWITTER_URL);
				}
			}
		});
		
		imgFacebook.setOnClickListener(new OnClickListener() {
					
			@Override
			public void onClick(View v) {
				try {
					openURL(Data.FACEBOOK_APP_URL);
				}
				catch (Exception e) {
				    openURL(Data.FACEBOOK_URL);
				}
			}
		});
		
		imgRate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				openURL(Data.GOOGLE_PLAY);
			}
		});
		
		imgEmail.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try{
					openURL("mailto:" + Data.EMAIL + "?subject=" + getActivity().getResources().getString(R.string.app_name));
				}
				catch(Exception e){
					Log.e("Extras", "Brak klienta e-mail");
				}
			}
		});
		
		return rootView;
	}
	
	private void openURL(String url){
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		startActivity(browserIntent);
	}
}
