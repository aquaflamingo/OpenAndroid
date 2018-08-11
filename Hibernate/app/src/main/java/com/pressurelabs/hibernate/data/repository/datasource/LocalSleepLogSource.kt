package com.pressurelabs.hibernate.data.repository.datasource

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.pressurelabs.hibernate.data.models.SleepEntry
import com.pressurelabs.hibernate.data.repository.interfaces.ISleepLogSource
import com.pressurelabs.hibernate.util.Faker
import org.jetbrains.anko.db.*
import timber.log.Timber
import java.sql.Timestamp

class LocalSleepLogSource(ctx: Context) : ManagedSQLiteOpenHelper(ctx, "LocalSleepTracking", null, 1), ISleepLogSource {
    override fun debug() {
        seedDb()
    }

    override fun insert(entry: SleepEntry) {
        Timber.d("Inserting into database: \n$entry")
        use {
            // TODO WHERE within range update
            this.insert(Schema.Sleep.TABLE,
                    Schema.Sleep.START to entry.start.time,
                    Schema.Sleep.END to entry.end.time)
        }
    }

    //TODO how to get most recent entries
    override fun getRecent(numberEntries:Int): List<SleepEntry>? {
        val sleepEntryRowParser = rowParser {
            _:Int, start:Long, end:Long ->
                SleepEntry(Timestamp(start), Timestamp(end))
        }

        var recentEntries:List<SleepEntry>?=null

        use {
            recentEntries = select(Schema.Sleep.TABLE)
                    .limit(numberEntries)
                    .orderBy(Schema.Sleep.END,SqlOrderDirection.ASC)
                    .parseList(sleepEntryRowParser)

        }
        return recentEntries
    }

    companion object {
        private var instance: LocalSleepLogSource? = null

        @Synchronized
        fun getInstance(ctx: Context): LocalSleepLogSource {
            if (instance == null) {
                instance = LocalSleepLogSource(ctx.applicationContext)
            }
            return instance!!
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Here you create tables
        db?.createTable("Sleep", true,
                "id" to INTEGER + PRIMARY_KEY,
                "start_time" to INTEGER,
                "end_time" to INTEGER
                )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Here you can upgrade tables, as usual
        db?.dropTable("Sleep", true)
    }

    private fun seedDb() {
        var list = Faker.fake_sleep_entries(6)
        list.forEach {
            v-> this.insert(v)
            Timber.d("Seeding: $v")
        }
    }

    private object Schema {
        object Sleep {
            val TABLE = "Sleep"
            val START = "start_time"
            val END = "end_time"
        }

    }
}
