package com.pressurelabs.hibernate.data.repository.interfaces

import android.content.Context
import android.content.SharedPreferences
import com.pressurelabs.hibernate.data.repository.interfaces.ICacheDb

abstract class CacheDb(context: Context): ICacheDb {
    var instance:SharedPreferences=CreateInstance(context)

    // Convience methods
    override fun getPreferences(c: android.content.Context): android.content.SharedPreferences {
        instance = CreateInstance(c)
        return instance
    }

    // Convience methods
    override fun getEditor(c: android.content.Context): android.content.SharedPreferences.Editor {
        instance = CreateInstance(c)
        return instance.edit()
    }



    private fun CreateInstance(c: android.content.Context): android.content.SharedPreferences {
        if (this.instance==null) {
            return c.getSharedPreferences(c.getString(com.pressurelabs.hibernate.R.string.file_groggy_preferences), android.content.Context.MODE_PRIVATE)
        } else {
            return instance;
        }
    }

}


