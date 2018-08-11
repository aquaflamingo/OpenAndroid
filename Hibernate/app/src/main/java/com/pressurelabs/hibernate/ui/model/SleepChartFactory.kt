package com.pressurelabs.hibernate.ui.model

import android.content.Context
import com.pressurelabs.hibernate.data.models.SleepEntry
import com.pressurelabs.hibernate.data.repository.SleepLogProvider
import lecho.lib.hellocharts.model.*

class SleepChartFactory(c:Context) : ISleepChartFactory {
    override fun CreateLineChart(numEntries: Int):LineChartData? {
        var data: List<SleepEntry>? = repository.getRecent(numEntries)?:null ?: return null

        return HibernateLineChart(data!!).Create(numEntries)
    }

    override fun CreatePieChart(numEntries: Int):PieChartData? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private val repository = SleepLogProvider.provide(c)
}
