package com.hci.carebase.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.hci.carebase.R;

public class RxImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_rx_image);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (Exception e) {
            /* Ignore up as action */
        }
        Toolbar textView = findViewById(R.id.rx_image_title);
        if(getIntent().hasExtra("drugName")){
            String drugName = getIntent().getStringExtra("drugName");
            textView.setTitle(drugName);
        }else{
            textView.setTitle("Carebase");

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}