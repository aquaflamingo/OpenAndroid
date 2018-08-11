/*
 * Copyright (c) 2017. Pressure Labs. All Rights Reserved.
 */

package com.robertsimoes.conscious;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Small wrapper class to tidy code
 */
public class UserSettings {
    private static final String FILE_USER_SETTINGS = "conscious.UserSettings.FILE_USER_SETTINGS";
    public static final String KEY_SHOW_COMMIT_WARNINGS = "conscious.UserSettings.KEY_SHOW_COMMIT_WARNINGS";
    public static final String KEY_USER_NAME = "conscious.UserSettings.KEY_USER_NAME";
    public static final String KEY_FIRST_INSTALL = "conscious.UserSettings.KEY_FIRST_INSTALL";
    public static final String KEY_OPT_IN = "conscious.UserSettings.KEY_RESEARCH_OPT_IN";
    public static final String KEY_FIRST_COMMIT = "conscious.UserSettings.KEY_FIRST_COMMIT";
    public static final String KEY_USER_PIN = "conscious.UserSettings.KEY_USER_PIN";
    public static final String DEFAULT_PIN = "no_pin";
    public static final String KEY_PIN_LOCK_ENABLED = "conscious.UserSettings.KEY_PIN_LOCK_ENABLED";;
    public static final String KEY_EXPRESS_SILENCE_MILLIS = "conscious.UserSettings.KEY_EXPRESS_SILENCE_MILLIS";
    public static final String KEY_EXPRESS_MIN_INPUT_MILLIS = "conscious.UserSettings.KEY_EXPRESS_MIN_INPUT_MILLIS";
    private static UserSettings instance = new UserSettings();

    public UserSettings() {
    }

    public static UserSettings getInstance() {
        return instance;
    }

    public SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(
                FILE_USER_SETTINGS,
                Context.MODE_PRIVATE
        );
    }

    public SharedPreferences.Editor getEditor(Context context) {
        return context.getSharedPreferences(
                FILE_USER_SETTINGS,
                Context.MODE_PRIVATE
        ).edit();
    }

}
