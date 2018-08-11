package com.hci.carebase.data.interfaces;

import android.content.Context;
import android.content.SharedPreferences;

import com.hci.carebase.R;

/**
 * This is an abstraction ontop of Shared Preferences, to make our time
 * dealing with local caching of credentials easier.
 *
 */


public abstract class CacheDb {
    private SharedPreferences instance;

    public CacheDb(Context c) {
        instance = CreateInstance(c);
    }

    private SharedPreferences CreateInstance(Context c) {
        return c.getSharedPreferences(c.getString(R.string.file_local_cache), Context.MODE_PRIVATE);
    }

    public SharedPreferences getPreferences() {
        return instance;
    }

    public SharedPreferences.Editor getEditor() {
        return instance.edit();
    }
}
