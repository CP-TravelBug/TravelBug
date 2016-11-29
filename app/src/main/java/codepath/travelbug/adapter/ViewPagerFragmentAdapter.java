package codepath.travelbug.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.widget.Toast;

import codepath.travelbug.fragments.ViewPagerFragment;

/**
 * @author Pragyan
 */

public class ViewPagerFragmentAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 2;
    private String tabTitles[] = new String[] { "Home", "My Timelines"};
    private ViewPagerFragment fragmentArray[] = new ViewPagerFragment[PAGE_COUNT];


    public ViewPagerFragmentAdapter(FragmentManager fm) {
        super(fm);
    }
    @Override
    public Fragment getItem(int position) {
        Log.d("VPFragmentAdapter", Integer.toString(position));
        if (position > PAGE_COUNT) {
            return null;
        }
        if (fragmentArray[position] == null) {
            fragmentArray[position] = ViewPagerFragment.newInstance(position + 1);
        }
        return fragmentArray[position]; // Page 1 or 2
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    public void refreshMyTimeline() {

        ViewPagerFragment fragment = fragmentArray[1];
        if (fragment != null) {
            fragment.refreshTimeline();
        }

        // Also refresh the hometimeline
        fragment = fragmentArray[0];
        if (fragment != null) {
            fragment.refreshHomeTimeline();
        }
    }
}
