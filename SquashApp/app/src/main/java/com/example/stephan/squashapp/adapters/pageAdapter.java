package com.example.stephan.squashapp.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.stephan.squashapp.fragments.LoginFragment;
import com.example.stephan.squashapp.fragments.RegisterFragment;


/**
 * Created by Stephan on 13-6-2016.
 */
public class pageAdapter extends FragmentPagerAdapter {
    private static int NUM_ITEMS = 2;

    public pageAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: // Fragment # 0 - This will show FirstFragment
                return LoginFragment.newInstance(0, "Log in");
            case 1: // Fragment # 0 - This will show FirstFragment different title
                return RegisterFragment.newInstance(1, "Register");
            default:
                return null;
        }
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Log in";
            case 1:
                return "Register";
            default:
                return "Page title";
        }
    }
}