package com.amador.androidbox;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import layout.FragmentLocal;
import layout.FragmentCloud;

/**
 * @author Amador Fernandez Gonzalez
 *         <p>
 *         Simple adaptador para los fragments
 */

public class AdapterPager extends FragmentStatePagerAdapter {

    public AdapterPager(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment = null;

        switch (position) {

            case 0:
                fragment = new FragmentLocal();
                break;
            case 1:
                fragment = new FragmentCloud();
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
