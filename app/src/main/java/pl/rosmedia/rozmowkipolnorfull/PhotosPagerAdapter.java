package pl.rosmedia.rozmowkipolnorfull;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
 
public class PhotosPagerAdapter extends FragmentPagerAdapter {
	
	private String[] paths;
	private String dir;
	
    public PhotosPagerAdapter(FragmentManager fm, String dir, String[] paths) {
        super(fm);
        this.paths = paths;
        this.dir = dir;
    }
 
    @Override
    public Fragment getItem(int index) {
    	
    	PhotoFragment frag = new PhotoFragment();
    	frag.path = dir + "/" + paths[index];
    	frag.description = (index + 1) + "/" + paths.length;
    	return frag;
    }
 
    @Override
    public int getCount() {
        return paths.length;
    }
 
}