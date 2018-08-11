/*
 * Copyright (c) 2017. Pressure Labs. All Rights Reserved.
 */

package com.robertsimoes.conscious.data.stores;

import com.robertsimoes.conscious.data.models.Thought;

import java.util.List;

/**
 * Copyright (c) 2017 Pressure Labs.
 *
 */


public interface ThoughtDAO<T> extends BaseDAO<T> {
    void think(String stamp, String title, String body);
    List<Thought> simpleQuery(String[] returnColumns, String selection, String[] selectionArgs, String columnToSortBy);
}
