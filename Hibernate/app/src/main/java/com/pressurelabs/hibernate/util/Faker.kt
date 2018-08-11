package com.pressurelabs.hibernate.util

import com.pressurelabs.hibernate.data.models.SleepEntry
import java.sql.Timestamp
import java.util.*

object Faker {
    fun fake_sleep_entries(daysToFake: Int): MutableList<SleepEntry> {
        val myCal = Calendar.getInstance()
        var myMillis = myCal.timeInMillis - Conversion.d_to_ms(daysToFake.toLong())
        var myList = mutableListOf<SleepEntry>()
        var x=0
        while (x<daysToFake) {
            var myRandom = random_long(10800000,28800000)
            myList.add(
                    SleepEntry(Timestamp(myMillis-myRandom), Timestamp((myMillis)))
            )
            myMillis+=(Conversion.d_to_ms(1)+ random_long(10800000,28800000)) // Go back 1 day
            x++
        }
        return myList
    }

    private fun random_long(min:Long, max:Long):Long {
        val rand: Random=Random()

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        val randomNum:Long = (min+(rand.nextDouble()*(max-min))).toLong()

        return randomNum
    }
}