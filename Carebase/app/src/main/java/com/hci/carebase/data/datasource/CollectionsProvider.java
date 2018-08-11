package com.hci.carebase.data.datasource;

import com.hci.carebase.data.interfaces.PatientSource;
import com.hci.carebase.data.interfaces.PhotoSource;

/**
 * This class acts as entry point for more explicit instances of the
 * particular collections that we use in our datasource.
 */

public class CollectionsProvider {
    public static PatientSource patients() {
        return new CarebaseDatabase();
    }

    public static PhotoSource photos() {
        return new CarebaseDatabase();
    }
}
