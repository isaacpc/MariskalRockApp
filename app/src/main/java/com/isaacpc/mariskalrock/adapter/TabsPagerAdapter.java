package com.isaacpc.mariskalrock.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.isaacpc.mariskalrock.fragment.NoticiasFragment;
import com.isaacpc.mariskalrock.fragment.PodcastFragment;
import com.isaacpc.mariskalrock.fragment.RadioFragment;
import com.isaacpc.mariskalrock.fragment.VideoFragment;

public class TabsPagerAdapter extends FragmentPagerAdapter {

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {


        switch (index) {
            case 0:
                return new NoticiasFragment();
            case 1:
                return new PodcastFragment();

            /*case 2:
                return new VideoFragment();*/
            case 2:
                return new RadioFragment();

        }

        return null;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 3;
    }

}