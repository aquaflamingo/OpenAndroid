/*
 * Copyright (c) 2017. Pressure Labs. All Rights Reserved.
 */

package com.robertsimoes.conscious.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.robertsimoes.conscious.R;
import com.pressurelabs.swissarmyknife.GTools;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShowThoughtActivity extends AppCompatActivity {

    @BindView(R.id.tv_show_thought_body)
    TextView tvBody;

    @BindView(R.id.tv_show_thought_title)
    TextView tvTitle;

    @BindView(R.id.tv_toolbar_show_thought_timestamp)
    TextView tvTimeStamp;

    private final String TAG =  getClass().getName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_thought);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_show_thought);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        if (getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }

        initThoughtDisplay((String[])getIntent().getExtras().get(MainRVAdapter.ThoughtViewHolder.EXTRA_THOUGHT_DETAILS));
    }

    private void initThoughtDisplay(String[] thoughtDetails) {
        try {
            tvTimeStamp.setText(thoughtDetails[0]);
        } catch (Exception e) {
            tvTimeStamp.setVisibility(View.INVISIBLE);
        }

        try {
            tvTitle.setText(thoughtDetails[1]);
        } catch (Exception e) {
            tvTitle.setVisibility(View.INVISIBLE);
        }

        try {
            tvBody.setText(thoughtDetails[2]);
        } catch (Exception e) {
           tvBody.setVisibility(View.INVISIBLE);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_show_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
