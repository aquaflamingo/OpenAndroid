package com.hci.carebase.data.datasource.interfaces;

/**
 * This contains the collection names for each of the Firebase level 1 nodes.
 * User so that in the case of refactoring we don't break everything.
 */
public abstract class FirebaseCollections {
    public static final String REF_PATIENTS = "patients";
    public static final String REF_STORAGE_PROFILE_PHOTOS = "profiles_photos";
    public static final String REF_STORAGE_APPOINTMENTS = "appointments";
    public static final String REF_STORAGE_HEALTHCARDS = "healthcards";
}
