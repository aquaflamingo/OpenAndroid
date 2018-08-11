/*
 * Copyright (c) 2017. Pressure Labs. All Rights Reserved.
 */

package com.robertsimoes.conscious.data.stores.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;


import com.robertsimoes.conscious.R;
import com.robertsimoes.conscious.data.helpers.ThoughtDbHelper;
import com.robertsimoes.conscious.data.models.Thought;
import com.robertsimoes.conscious.data.stores.ThoughtDAO;
import com.robertsimoes.conscious.data.stores.schema.ThoughtContract;
import com.robertsimoes.conscious.utility.SimpleSecurity;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Copyright (c) 2017 Pressure Labs.
 *
 */


public class ThoughtSource implements ThoughtDAO<Thought> {
    private final String TAG = getClass().getName();
    private SQLiteDatabase database;
    private ThoughtDbHelper dbHelper;

    public static final String[] ALL_COLUMNS = {
            ThoughtContract.ThoughtEntry._ID,
            ThoughtContract.ThoughtEntry.COLUMN_TIME_STAMP,
            ThoughtContract.ThoughtEntry.COLUMN_TITLE,
            ThoughtContract.ThoughtEntry.COLUMN_BODY,
            ThoughtContract.ThoughtEntry.COLUMN_HASH
    };

    public ThoughtSource(Context ctx) {
        dbHelper=new ThoughtDbHelper(ctx);
    }

    /**
     * Takes in the specified params and create a new entry
     * of a thought into clients' database.
     * @param stamp Time stamp of this thought
     * @param title The Title of this thought
     * @param body The body of this thought
     */
    @Override
    public void think(String stamp, String title, String body) {
        ContentValues values = new ContentValues();
        values.put(ThoughtContract.ThoughtEntry.COLUMN_TIME_STAMP, stamp);
        values.put(ThoughtContract.ThoughtEntry.COLUMN_TITLE, title);
        values.put(ThoughtContract.ThoughtEntry.COLUMN_BODY, body);
        values.put(ThoughtContract.ThoughtEntry.COLUMN_HASH, sha256(body+" "+title+" "+stamp));
        // Insert the new row, returning the primary key value of the new row
        long newRowId = database.insert(ThoughtContract.ThoughtEntry.TABLE_NAME, null, values);

    }

    public List<Thought> simpleQuery(String[] returnColumns, String selection, String[] selectionArgs, String columnToSortBy) {
        List<Thought> thoughts = new ArrayList<>();
        String order = columnToSortBy + " DESC";
        Cursor cusor = database.query(
                ThoughtContract.ThoughtEntry.TABLE_NAME,
                returnColumns,
                selection,
                selectionArgs,
                null,
                null,
                order
        );

        cusor.moveToFirst();
        while(!cusor.isAfterLast()) {
            Thought t = fromCursor(cusor);
            thoughts.add(t);
            cusor.moveToNext();
        }

        cusor.close();
        return thoughts;
    }

    /**
     * Attempts to open or create the database if does not exist.
     * @throws SQLException
     */
    @Override
    public void open() throws SQLException {
        database= dbHelper.getWritableDatabase();
        if (database==null) {
            throw new SQLException("Could not get writable database.. ");
        }
    }

    @Override
    public void close() {
        dbHelper.close();
    }

    @Override
    public List<Thought> getAll() {
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.

        List<Thought> thoughts = new ArrayList<>();

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                ThoughtContract.ThoughtEntry.COLUMN_TIME_STAMP + " DESC"; // Order by descending

        Cursor cursor = database.query(
                ThoughtContract.ThoughtEntry.TABLE_NAME,                     // The table to query
                ALL_COLUMNS,                                   // The columns to return
                null,                                        // The columns for the WHERE clause
                null,                                       // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Thought snp = fromCursor(cursor);
            thoughts.add(snp);
            cursor.moveToNext();
        }

        cursor.close();
        return thoughts;
    }

    private Thought fromCursor(Cursor cursor) {

        return new Thought(cursor.getLong(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4));
    }



    @Override
    public void delete(Thought item) {
        String[] selectionArgs = {String.valueOf(item.getId())};
        database.delete(ThoughtContract.ThoughtEntry.TABLE_NAME,
                ThoughtContract.ThoughtEntry._ID + " LIKE ?",selectionArgs);
    }

    @Override
    public int nuke() {
        return database.delete(ThoughtContract.ThoughtEntry.TABLE_NAME,null,null);
    }

    @Override
    public boolean isOpen() {
        return database.isOpen();
    }

    @Override
    public long size() {
        SQLiteStatement s = database.compileStatement("SELECT COUNT(*) from "+ ThoughtContract.ThoughtEntry.TABLE_NAME);
        return s.simpleQueryForLong();
    }

    /**
     * Seeds database with random data from string resources
     * @param c context
     * @return true if successful, false otherwise
     * @throws SQLException if unable to open.
     */
    public boolean seed(Context c,int count) throws SQLException{

        Log.w(TAG, "Seeding database.. erasing all entries");

        this.nuke();

        String[] names = c.getResources().getStringArray(R.array.random_names);
        String[] sentences = c.getResources().getStringArray(R.array.random_sentences);

        for (int i=0;i<=count;i++) {
            long offset = Timestamp.valueOf("2006-01-01 00:00:00").getTime();
            long end = Timestamp.valueOf("2020-01-01 00:00:00").getTime();
            long diff = end - offset + 1;
            Timestamp rand = new Timestamp(offset + (long)(Math.random() * diff));
            Random rnd = new Random();
            int f = rnd.nextInt(49);
            this.think(rand.toString(),names[f],sentences[f]);
        }

        return true;
    }

    public List<Thought> getMatches(String query, String[] columns) {
        String selection = ThoughtContract.ThoughtEntry.COLUMN_BODY + " LIKE ?";
//         + " OR " + VirtContract.VirtEntry.COL_TITLE + " MATCH ?";
        String[] selectionArgs = new String[] {"%"+query+"%"};
        /* % == wild cards */

        List<Thought> thoughts = new ArrayList<>();
        Cursor cursor = query(selection, selectionArgs, columns);
        if (cursor!=null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Thought snp = fromCursor(cursor);
                thoughts.add(snp);
                cursor.moveToNext();
            }

            cursor.close();
            return thoughts;
        } else {
            return null;
        }
    }

    private Cursor query(String selection, String[] selectionArgs, String[] columns) {
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(ThoughtContract.ThoughtEntry.TABLE_NAME);

        Cursor cursor = builder.query(dbHelper.getReadableDatabase(),
                columns, selection, selectionArgs, null, null, null);

        if (cursor == null) {
            return null;
       } else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }
        return cursor;
    }

    /**
     * Hashes and returns txt to SHA-256 string
     * @param txtToHash the text String to one way hashPBK
     * @return the one way hashed string
     */
    private String sha256(String txtToHash) throws RuntimeException {

        SimpleSecurity securitize = SimpleSecurity.getInstance();
        try {
         byte[] hash = securitize.sha256(txtToHash);
            return securitize.stringify256(hash);

            } catch(Exception ex){
                throw new RuntimeException(ex);
            }
        }
    }
