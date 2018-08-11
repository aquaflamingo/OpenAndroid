/*
 * Copyright (c) 2017. Pressure Labs. All Rights Reserved.
 */

package com.robertsimoes.conscious.data.stores;

import java.sql.SQLException;
import java.util.List;

/**
 * Copyright (c) 2017 Pressure Labs.
 *
 */

public interface BaseDAO<T> {
    void open() throws SQLException;
    void close();
    List<T> getAll();
    void delete(T item);
    int nuke();
    boolean isOpen();
    long size();
}
