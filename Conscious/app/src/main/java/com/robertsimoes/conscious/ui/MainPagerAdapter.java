/*
 * Copyright (c) 2017. Pressure Labs. All Rights Reserved.
 */

package com.robertsimoes.conscious.ui;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.robertsimoes.conscious.data.stores.local.ThoughtSource;
import com.pressurelabs.swissarmyknife.GTools;

import java.sql.SQLException;

public class MainPagerAdapter extends FragmentPagerAdapter {
    private final String TAG = this.getClass().getName();
    public static final int CARDS_PER_PAGE = 20;
    private Context c;
    private MainRVFragment current;

    public MainPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.c = context;
    }


    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return MainRVFragment.newInstance(position+1,true);
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        ThoughtSource s = new ThoughtSource(c);
        int app;
        try {
            s.open();
            app=(int)Math.ceil((double)s.size()/CARDS_PER_PAGE);
            s.close();
        } catch (SQLException e) {
            GTools.getInstance().error(TAG,"Could not open and caluclate database size.");
            app=5;
        }

        return app;
    }

}
