package pl.rosmedia.rozmowkipolnorfull;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
 
public class MainPagerAdapter extends FragmentPagerAdapter {
 
	public FragmentActivity act;
	
    public MainPagerAdapter(FragmentActivity act, FragmentManager fm) {
        super(fm);
        this.act = act;
    }
 
    @Override
    public Fragment getItem(int index) {
 
        switch (index) {
        case 0:
            return new IntroFragment();
        case 1:
            return new InfoFragment();
        case 2:
            return new PhrasebookFragment();
	    case 3:
	        return new ExtrasFragment();
        }
 
        return null;
    }
 
    @Override
    public int getCount() {
        return 4;
    }
 
}