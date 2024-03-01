package pl.rosmedia.rozmowkipolnorfull;

import java.io.IOException;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

public class AssetImageLoadTask extends AsyncTask<Void, Integer, Boolean> {

    private Context context;
	private Drawable drawable;
	private String path;
	private ImageView imageView;

    public AssetImageLoadTask(Context context, String path, ImageView imageView) {
        this.context = context;
    	this.path = path;
    	this.imageView = imageView;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
    	
    	drawable = null;
		try {
			drawable = Drawable.createFromStream(context.getAssets().open(path), null);
		} catch (IOException e) {
			e.printStackTrace();
			Log.e("AssetImageLoadTask", e.getMessage());
			return false;
		}
        
        return true;
    }

    @Override
    protected void onPostExecute(Boolean result) {
    	if(result){
    		if(imageView != null && drawable != null){
    			imageView.setImageDrawable(drawable);
    		}
    	}
    }
}