package com.pressurelabs.hibernate.data.repository.interfaces

interface ICacheDb {

    fun getPreferences(c: android.content.Context): android.content.SharedPreferences
    fun getEditor(c: android.content.Context): android.content.SharedPreferences.Editor
}