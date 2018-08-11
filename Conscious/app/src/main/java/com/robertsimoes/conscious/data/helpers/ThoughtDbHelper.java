/*
 * Copyright (c) 2017. Pressure Labs. All Rights Reserved.
 */

package com.robertsimoes.conscious.data.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.robertsimoes.conscious.data.stores.schema.ThoughtContract;

/**
 * Copyright (c) 2017 Pressure Labs.
 *
 */


public class ThoughtDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "UserThoughts.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ThoughtContract.ThoughtEntry.TABLE_NAME + " (" +
                    ThoughtContract.ThoughtEntry._ID + " INTEGER PRIMARY KEY," +
                    ThoughtContract.ThoughtEntry.COLUMN_TIME_STAMP + " TEXT," +
                    ThoughtContract.ThoughtEntry.COLUMN_TITLE + " TEXT," +
                    ThoughtContract.ThoughtEntry.COLUMN_BODY + " TEXT,"+
                    ThoughtContract.ThoughtEntry.COLUMN_HASH + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ThoughtContract.ThoughtEntry.TABLE_NAME;


    public ThoughtDbHelper (Context ct) {
        super(ct,DATABASE_NAME,null,DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(ThoughtDbHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    /**
     * Downgrade database from the old version to new version
     * @param db database to downgrade
     * @param oldVersion old verison in use
     * @param newVersion new version to use
     */
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
