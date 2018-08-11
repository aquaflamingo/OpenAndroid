package com.robertsimoes.conscious.ui.helpers;

import android.content.Context;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Copyright (c) 2017 Pressure Labs.
 */

public class SimpleTwoLineAdapterCreator {

    public SimpleTwoLineAdapterCreator() {
    }

    public SimpleAdapter createAdapter(Context context, String[][] data) {
        SimpleAdapter adapter =
                new SimpleAdapter(
                        context,
                        buildList(data),
                        android.R.layout.simple_list_item_2,
                        new String[] {"line1","line2"},
                        new int[] {android.R.id.text1,android.R.id.text2});
        return adapter;
    }


    private ArrayList<HashMap<String,String>> buildList(String[][] dataArray) {
        ArrayList<HashMap<String,String>> settingsAL = new ArrayList();
        for (int i=0;i<dataArray.length;i++) {
            HashMap<String, String> item = new HashMap<>();
            item.put("line1",dataArray[i][0]);
            item.put("line2",dataArray[i][1]);
            settingsAL.add(item);
        }
        return settingsAL;
    }
}