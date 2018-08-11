package com.pressurelabs.hibernate.ui.model

import lecho.lib.hellocharts.model.LineChartData
import lecho.lib.hellocharts.model.PieChartData

interface ISleepChartFactory {
    fun CreateLineChart(numEntries:Int):LineChartData?
    fun CreatePieChart(numEntries: Int):PieChartData?
}