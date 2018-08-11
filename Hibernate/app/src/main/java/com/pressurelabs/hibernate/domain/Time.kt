package com.pressurelabs.hibernate.domain

// Time = Tempus in Latinw

data class Time(var hour:Int=7, var minutes:Int=0): Comparable<Time> {

    override fun compareTo(other: Time): Int {
        if (this.toMinutes()<other.toMinutes()) {
            return -1
        } else if (this.toMinutes()>other.toMinutes()) {
            return 1
        } else {
            return 0
            // Equal
        }
    }

    private val ONE_DAY_MINUTES = 1440

    override fun toString(): String {
        return String.format("%02d:%02d", hour, (minutes % 60));
    }

    fun toMinutes(): Int {
        return hour*60+minutes
    }



    fun toMillis():Long {
        return ((hour*60+minutes).toLong()*60*1000)
    }

    fun addOneDayAndGetMinutes():Int {
        return (hour*60+minutes)+(24*60)
    }

    fun toTweleveHour():String {
        var ext = "AM"
        var hours12 = this.hour
        var minutes12 = this.minutes

        if (this.toMinutes()>(ONE_DAY_MINUTES/2)) {
            ext="PM"
            hours12-=12
        }

        return String.format("%02d:%02d $ext", hours12, (minutes12 % 60));
    }

}