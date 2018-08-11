/*
 * Copyright (c) 2017. Pressure Labs. All Rights Reserved.
 */

package com.robertsimoes.conscious.ui;

import android.app.SearchManager;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import com.robertsimoes.conscious.R;
import com.robertsimoes.conscious.data.stores.local.ThoughtSource;
import com.robertsimoes.conscious.data.models.Thought;
import com.pressurelabs.swissarmyknife.GTools;

import java.util.List;

public class SearchResultsActivity extends AppCompatActivity {

    private final String TAG = getClass().getName();
    private static final String FRAGMENT_TAG = ".SearchResultsActivity.FRAGMENT_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        handleIntent(getIntent());
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            searchDb(query);
            //use the query to search your data somehow
        }
    }

    private void searchDb(String queryText) {
        GTools logger = GTools.getInstance();
        logger.log(TAG, "Querying..");
        ThoughtSource search = null;
        try {
            search = new ThoughtSource(SearchResultsActivity.this);
            search.open();
        } catch (Exception e) {
            logger.error(TAG, "Could not open for search " + e.getMessage());
        }

        if (search!=null) {
            List<Thought> hits = search.getMatches(queryText, ThoughtSource.ALL_COLUMNS);
            if (hits == null) {
                logger.log(TAG, "No matches..");
                return;
            }

            MainRVFragment fg = addFragmentToView(0,false,hits);
        }

        if (search!=null && search.isOpen()) {
            search.close();
        }
    }


    private MainRVFragment addFragmentToView(int position, boolean restrictFragment, List<Thought> dataset) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        MainRVFragment fragment = MainRVFragment.newInstance(0,false);
        MainRVAdapter adapter = new MainRVAdapter(dataset);
        fragment.setAdapter(adapter);

        if (fragment != null) {
            transaction.add(R.id.container_search_fragment,fragment,FRAGMENT_TAG);
            transaction.addToBackStack(null);
            transaction.commit();
        }

        return fragment;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
       finish();
    }
}
