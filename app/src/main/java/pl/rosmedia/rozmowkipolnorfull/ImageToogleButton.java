package pl.rosmedia.rozmowkipolnorfull;

import pl.rosmedia.rozmowkipolnorfull.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class ImageToogleButton extends ImageView {

	public Drawable imgOn;
	public Drawable imgOff;
	private boolean checked = false;
	
	public ImageToogleButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		TypedArray a = context.getTheme().obtainStyledAttributes(
		        attrs,
		        R.styleable.ImageToogleButton,
		        0, 0);

	   try {
		   imgOn = a.getDrawable(R.styleable.ImageToogleButton_imgOn);
		   imgOff = a.getDrawable(R.styleable.ImageToogleButton_imgOff);
		   checked = a.getBoolean(R.styleable.ImageToogleButton_checked, false);
	   } finally {
	       a.recycle();
	   }
	   
	   this.setImageDrawable((checked ? imgOn : imgOff));
	
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
		refreshDrawable();
	}
	
	public boolean isChecked(){
		return checked;
	}
	
	public void refreshDrawable(){
		this.setImageDrawable((checked ? imgOn : imgOff));
	}

}
