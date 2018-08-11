/*
 * Copyright (c) 2017. Pressure Labs. All Rights Reserved.
 */

package com.robertsimoes.conscious.ui;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.robertsimoes.conscious.R;
import com.robertsimoes.conscious.data.stores.local.ThoughtSource;
import com.robertsimoes.conscious.data.models.Thought;
import com.pressurelabs.swissarmyknife.GTools;

import java.sql.SQLException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


@SuppressWarnings("WeakerAccess")
public class DebuggerActivity extends AppCompatActivity {

    private final String TAG = getClass().getName();

    @BindView(R.id.button_debug_read_db)
    Button butRead;

    @BindView(R.id.button_debug_seed_db)
    Button butSeed;

    @BindView(R.id.et_debug_seed_amt)
    EditText seedText;

    @BindView(R.id.et_debug_search_q)
    EditText queryText;


    private String FILE_NAME_VIRTUAL_TABLE = "searchable_thoughts.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debugger);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_debug);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        if (getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId()==android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    @OnClick(R.id.button_debug_seed_db)
    public void seed(View v) {

        AlertDialog.Builder b = new AlertDialog.Builder(DebuggerActivity.this);
        b.setTitle("All data will be erased");
        b.setPositiveButton("I'm an adult",  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(seedText!=null && !seedText.getText().toString().equals("") && Integer.parseInt(seedText.getText().toString())>0) {
                    ThoughtSource source = new ThoughtSource(DebuggerActivity.this);
                    try {
                        source.open();
                        /* 1 less then real amount because rows are performed by id look up */
                        source.seed(DebuggerActivity.this,Integer.parseInt(seedText.getText().toString())-1);

                    } catch (SQLException e) {
                        GTools.getInstance().error(TAG,"Could not open.. " +e.getMessage());
                    } finally {
                        if (source.isOpen()) {
                            source.close();
                        }
                    }
                } else {
                    Toast.makeText(DebuggerActivity.this, "Must have valid value to seed.", Toast.LENGTH_SHORT).show();
                }

            }
        }).show();


    }

    /**
     * Seeds the databased with 30 random thoughts
     */


    @OnClick(R.id.button_debug_read_db)
    public void read(View v) {
        GTools logger = GTools.getInstance();
        ThoughtSource source = new ThoughtSource(DebuggerActivity.this);
        logger.log(TAG, "Attempting to open database");

        try {
            source.open();
        } catch (SQLException e) {
            logger.error(TAG, "Could not open database..." + e.getMessage());
        }
        List<Thought> all = source.getAll();

        for (Thought tho : all) {
            logger.log(TAG, tho.toString());
        }
        source.close();
    }

    @OnClick(R.id.button_debug_search)
    public void search(View v) {
        GTools logger = GTools.getInstance();
        logger.log(TAG, "Querying..");
        ThoughtSource search = null;
        try {
            search = new ThoughtSource(DebuggerActivity.this);
            search.open();
        } catch (Exception e) {
            logger.error(TAG, "Could not open for search " + e.getMessage());
        }

        if (queryText != null && !queryText.getText().toString().equals("")) {
            List<Thought> hits = search.getMatches(queryText.getText().toString(), ThoughtSource.ALL_COLUMNS);
            if (hits == null) {
                logger.log(TAG, "No matches..");
                return;
            }

            for (Thought th : hits) {
                logger.log(TAG, th.toString());
            }

        } else {
            Toast.makeText(DebuggerActivity.this, "Enter a query", Toast.LENGTH_SHORT).show();
        }

        if (search!=null && search.isOpen()) {
            search.close();
        }

    }
}
