/*
 * Copyright (c) 2017. Pressure Labs. All Rights Reserved.
 */

package com.robertsimoes.conscious.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.robertsimoes.conscious.R;
import com.robertsimoes.conscious.data.stores.local.ThoughtSource;
import com.robertsimoes.conscious.data.models.Thought;
import com.robertsimoes.conscious.data.stores.schema.ThoughtContract;
import com.pressurelabs.swissarmyknife.GTools;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainRVFragment extends Fragment {

    @BindView(R.id.fragment_mainrv)
    RecyclerView mRecyclerView;

    private RecyclerView.Adapter mAdapter;
    private String TAG = getClass().getName();

    private static final String KEY_FRAGMENT_POSITION = "conscious.KEY_FRAGMENT_POSITION";
    private static final String KEY_FRAGMENT_RESTRICTED = "conscious.KEY_FRAGMENT_RESTRICTED";

    public MainRVFragment() {
    }


    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static MainRVFragment newInstance(int sectionNumber, boolean restrictCardsPerFragment) {
        MainRVFragment fragment = new MainRVFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_FRAGMENT_POSITION, sectionNumber);
        args.putBoolean(KEY_FRAGMENT_RESTRICTED, restrictCardsPerFragment);
        fragment.setArguments(args);

        return fragment;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_rec_view_thoughts, container, false);
        ButterKnife.bind(this,rootView);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        /**
         * If the fragment should display on restricted number of
         * cards per fragment?
         */
        if ((getArguments().getBoolean(KEY_FRAGMENT_RESTRICTED))) {
             /* ====== Init Recycle View ====== */
            ThoughtSource src = new ThoughtSource(getContext());

            try {
                src.open();
            } catch (Exception e) {
                GTools.getInstance().error(TAG,"Could not open database...");
            }

            int CARDS_PER_PAGE = MainPagerAdapter.CARDS_PER_PAGE;
            int lastId = CARDS_PER_PAGE *getArguments().getInt(KEY_FRAGMENT_POSITION);
            int firstId = lastId- CARDS_PER_PAGE;
            if(firstId<0) {
                firstId=0;
            }
        /* There are CARDS_PER_PAGE number of Card View Widgets per page. Each fragment page is identified,
            with KEY_FRAGMENT_POSITION. to determine the next set of ids to display on this page
         */
            String where = ThoughtContract.ThoughtEntry._ID+" BETWEEN ? AND ?";

            List<Thought> dataSet =
                    src.simpleQuery(ThoughtSource.ALL_COLUMNS,
                            where,
                            new String[] {String.valueOf(firstId), String.valueOf(lastId)},
                            ThoughtContract.ThoughtEntry._ID);

            mAdapter = new MainRVAdapter(dataSet);
            mRecyclerView.setAdapter(mAdapter);

            if(src!=null && src.isOpen()) {
                src.close();
            }
        } else {
            mRecyclerView.setAdapter(mAdapter);
        }
        // Set textviews etc

        return rootView;
    }



    public void setAdapter(MainRVAdapter adapter) {
        mAdapter = adapter;
    }

}
