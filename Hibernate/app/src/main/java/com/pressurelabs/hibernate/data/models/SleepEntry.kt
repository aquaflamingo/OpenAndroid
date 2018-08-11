package com.pressurelabs.hibernate.data.models

import java.sql.Timestamp
import java.text.SimpleDateFormat

class SleepEntry(val start:Timestamp, val end:Timestamp) {
    var cycles:Float = getTimeMinutesSlept()/90 // NOTE depends on users sleep cycle
    fun getTimeMinutesSlept():Float {
        var millisDifference = (end.time - start.time)
        var minutes:Float = millisDifference/(1000f*60f)
        return minutes
    }

    override fun toString(): String {
        var sdf = SimpleDateFormat("d MMM yyyy HH:mm:ss")

        return "Sleep Entry: ${sdf.format(start)} - ${sdf.format(end)}, cycles: $cycles"
    }

}