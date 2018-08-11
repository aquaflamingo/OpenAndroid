package com.hci.carebase.data.interfaces;

import com.hci.carebase.domain.Patient;

/**
 * This class acts as a more explicit interface for what functions
 * the LocalCacheDb has.
 *
 * It should be able to cache and retrieve the local User credentials, and acts
 * as an abstraction layer ontop of SharedPreferences.
 */
public interface ILocalCache {
    String getUserId();
    void storeUserId(String u);
    boolean isFirstInstall();
    void setFirstInstall(boolean firstInstall);
    boolean isLoggedIn();

    void setLoggedIn(boolean b);

    void setDemo(boolean b);

    boolean isDemo();
}
