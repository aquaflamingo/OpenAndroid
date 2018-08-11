package com.pressurelabs.hibernate.util

import java.util.*

class SimpleCalendar {
    private val calendar: Calendar = Calendar.getInstance()

    fun getHour():Int {
        return calendar.get(Calendar.HOUR_OF_DAY)
    }

    fun getMinute():Int {
        return calendar.get(Calendar.MINUTE)
    }

    fun setHour(hr:Int) {
        calendar.set(Calendar.HOUR_OF_DAY,hr)
    }

    fun setMin(min:Int) {
        calendar.set(Calendar.MINUTE,min)
    }

    fun millis():Long {
        return calendar.timeInMillis
    }

    fun getWeekDays():List<String> {
        var weekDays = mutableListOf<String>()
        var currentDay = calendar.get(Calendar.DAY_OF_WEEK)

        var index = 0;
        while (index<7) {
            weekDays.add(parseDay(currentDay))
            currentDay+=1
            currentDay %= 7 // Reset back to
            index++
        }
        return weekDays.toList()
    }

    private fun parseDay(day: Int):String {
        when (day) {
            Calendar.MONDAY -> return "Mon"
            Calendar.TUESDAY -> return "Tues"
            Calendar.WEDNESDAY -> return "Wed"
            Calendar.THURSDAY -> return "Thurs"
            Calendar.FRIDAY -> return "Fri"
            Calendar.SATURDAY -> return "Sat"
            Calendar.SUNDAY -> return "Sun"
        }
        return ""
    }

}