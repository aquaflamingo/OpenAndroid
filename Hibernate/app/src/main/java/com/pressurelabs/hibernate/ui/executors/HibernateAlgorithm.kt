package com.pressurelabs.hibernate.ui.executors

import com.pressurelabs.hibernate.domain.Time
import com.pressurelabs.hibernate.util.SimpleCalendar
import timber.log.Timber

object HibernateAlgorithm {
    fun getCurrentTime():Time {
        var cal = SimpleCalendar()
        var timeNow:Time = Time(cal.getHour(),cal.getMinute())
        return timeNow
    }
    fun isFutureAlarm(alarmInQuestion: Time):Boolean {
        var timeNow = getCurrentTime()

        return alarmInQuestion.toMillis() <= timeNow.toMillis()
    }

    fun minutesUntilThisAlarm(alarmInQuestion: Time):Int {
        var timeNow = getCurrentTime()
        var minsTo:Int = 0

        if (!HibernateAlgorithm.isFutureAlarm(alarmInQuestion)) {
            minsTo=alarmInQuestion.toMinutes()-timeNow.toMinutes()
        } else {
            var oneDayForwardAlarm = alarmInQuestion.addOneDayAndGetMinutes()
            minsTo=oneDayForwardAlarm-timeNow.toMinutes()

        }
        return minsTo
    }

    fun maximumCyclesForAlarm(alarmInQuestion: Time,cycleTime:Int,limit:Int):Int {
        var possibleNumberOfCycles = minutesUntilThisAlarm(alarmInQuestion)/cycleTime

        return if (possibleNumberOfCycles>limit) limit else possibleNumberOfCycles
    }

    fun optimalSleepTime(desiredAlarm:Time,numberOfCycles:Int,minutesPerCycle:Int):Time {
        var totalMinutesSleeping = numberOfCycles*minutesPerCycle
        var currentTime = getCurrentTime()

        var timeToSleep:Int=0
        var optimalTimeToGoToSleepInMinutes:Int=0

        // The current time of calendar is ahead in terms of HOUR_OF_DAY
        // but really the alarm set by user is just early the next day
        // ie. 7AM < 8PM, but 7AM was really targeted at next day

        if (desiredAlarm.compareTo(currentTime)==-1) {
            // desired alarm is less than current time, meaning it is intended to be a day ahead
            var futureAlarmTimeInMinutes = desiredAlarm.addOneDayAndGetMinutes()

            timeToSleep = futureAlarmTimeInMinutes-totalMinutesSleeping

            // Minutes per cycle is padding
            optimalTimeToGoToSleepInMinutes =
                    if (timeToSleep<currentTime.toMinutes())
                        timeToSleep+minutesPerCycle
                    else
                        timeToSleep

            if (optimalTimeToGoToSleepInMinutes>1440) {
                optimalTimeToGoToSleepInMinutes-=1440 //reset back 1 day
            }
            // Add 1 Day To It
        } else {
            var futureAlarmTimeInMinutes = desiredAlarm.toMinutes()
            // The current time of calendar is less
            timeToSleep = futureAlarmTimeInMinutes-totalMinutesSleeping
            optimalTimeToGoToSleepInMinutes =
                    if (timeToSleep<currentTime.toMinutes())
                        timeToSleep+minutesPerCycle
                    else
                        timeToSleep
        }

        var hours:Int = optimalTimeToGoToSleepInMinutes / 60 //since both are ints, you get an int
        var minutes = optimalTimeToGoToSleepInMinutes % 60

        if (hours>23 && minutes>0) {
            hours=0
        }

        Timber.d("\nTotal Sleep Minutes ${totalMinutesSleeping} \nOptimal Sleep Time ${optimalTimeToGoToSleepInMinutes} \nHours and Minutes ${hours}:${minutes}")

        return Time(hours,minutes)
    }
}