package com.pressurelabs.hibernate.ui.executors

import com.pressurelabs.hibernate.domain.Time
import timber.log.Timber

class SleepOptimizer(desiredAlarmTime: Time){
    data class Optimal(var calculatedFullCycles:Int, var idealTimeToSleep:Time)

    fun getOptimized(): Optimal {
        return Optimal(cycleCount,recommendedTimeToGoSleep)
    }

    private var cycleCount:Int = 0;
    private var recommendedTimeToGoSleep: Time = Time(7,0);
    private val DESIRED_CYCLE_TIME: Int = 90

    init {
        Timber.d("\nSeed time is ${desiredAlarmTime.hour} : ${desiredAlarmTime.minutes}")
        cycleCount = HibernateAlgorithm.maximumCyclesForAlarm(desiredAlarmTime,DESIRED_CYCLE_TIME,6)
            // Hibernate Algorithm is fairly generic, so Sleep Optimizer is a wrapper on top with some specifics
        recommendedTimeToGoSleep = HibernateAlgorithm.optimalSleepTime(desiredAlarmTime,cycleCount,DESIRED_CYCLE_TIME)
    }




}