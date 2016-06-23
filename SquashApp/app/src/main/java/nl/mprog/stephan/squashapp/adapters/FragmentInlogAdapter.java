package nl.mprog.stephan.squashapp.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import nl.mprog.stephan.squashapp.fragments.ForgotPasswordFragment;
import nl.mprog.stephan.squashapp.fragments.LoginFragment;
import nl.mprog.stephan.squashapp.fragments.RegisterFragment;


/**
 * Manage the fragments of the LoginActivity.
 */
public class FragmentInlogAdapter extends FragmentPagerAdapter {

    private static int NUM_FRAGMENTS = 3;   // Number of pages.

    public FragmentInlogAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    /**
     * Number of pages.
     */
    @Override
    public int getCount() {
        return NUM_FRAGMENTS;
    }

    /**
     * Return the Fragment to display.
     */
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return RegisterFragment.newInstance();
            case 1:
                return LoginFragment.newInstance();
            case 2:
                return ForgotPasswordFragment.newInstance();
            default:
                return null;
        }
    }

    /**
     * Return the title corresponding with the Fragment.
     */
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Register";
            case 1:
                return "Sign in";
            case 2:
                return "Forgot password";
            default:
                return "Page title";
        }
    }
}