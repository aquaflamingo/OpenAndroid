package com.robertsimoes.conscious.data.stores.schema;

import android.provider.BaseColumns;

/**
 * Copyright (c) 2017 Pressure Labs.
 *
 */


public class ThoughtContract {
    private ThoughtContract() {

    }

    public static class ThoughtEntry implements BaseColumns {
        public static final String TABLE_NAME = "user_thoughts";
        public static final String _ID = "_id";
        public static final String COLUMN_TIME_STAMP = "time_stamp";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_BODY = "body";
        public static final String COLUMN_HASH = "hash";
            // SHA-256
    }
}
