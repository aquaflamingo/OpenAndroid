package com.hci.carebase.data.datasource;

import android.content.Context;

import com.hci.carebase.data.interfaces.CacheDb;
import com.hci.carebase.data.interfaces.ILocalCache;
import com.hci.carebase.domain.Patient;

/**
 * This is our abstraction layer for our  local cache using Shared Preferences.
 *
 * It is helpful in the case, that we need to quickly access the user ID or patient ID
 * for a specific query purpose.
 */


public class LocalCacheDb extends CacheDb implements ILocalCache{
    // should we implement ILocalCache?

    private class CacheKeys {
        public static final String USER_UID = "user_uid";
        public static final String FIRST_INSTALL = "first_install";
        public static final String IS_LOGGED_IN = "is_logged_in";
        public static final String IS_DEMO = "is_demo";

    }
    public LocalCacheDb(Context c) {
        super(c);
    }

    @Override
    public String getUserId() {
        return getPreferences().getString(CacheKeys.USER_UID,null);
    }

    @Override
    public void storeUserId(String u) {
        getEditor().putString(CacheKeys.USER_UID,u).apply();
    }

    @Override
    public boolean isFirstInstall() {
        return getPreferences().getBoolean(CacheKeys.FIRST_INSTALL,true);
    }

    @Override
    public void setFirstInstall(boolean firstInstall) {
        getEditor().putBoolean(CacheKeys.FIRST_INSTALL,firstInstall).apply();
    }

    @Override
    public boolean isLoggedIn() {
        return getPreferences().getBoolean(CacheKeys.IS_LOGGED_IN, false);
    }

    @Override
    public void setLoggedIn(boolean b) {
        getEditor().putBoolean(CacheKeys.IS_LOGGED_IN,b).commit();
    }

    @Override
    public void setDemo(boolean b) {
        getEditor().putBoolean(CacheKeys.IS_DEMO,b).commit();
    }

    @Override
    public boolean isDemo() {
        return getPreferences().getBoolean(CacheKeys.IS_DEMO,false);
    }
}
