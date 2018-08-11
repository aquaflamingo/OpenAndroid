package com.pressurelabs.hibernate.ui.presenter

import android.content.Context
import android.support.v7.widget.Toolbar
import com.pressurelabs.hibernate.R
import com.pressurelabs.hibernate.ui.executors.UIFactory
import com.pressurelabs.hibernate.ui.model.ISleepChartFactory
import com.pressurelabs.hibernate.ui.model.SleepChartFactory
import com.pressurelabs.hibernate.ui.view.IStatisticsView
import lecho.lib.hellocharts.model.LineChartData
import lecho.lib.hellocharts.model.Viewport


class StatisticsPresenter(c:Context,view:IStatisticsView): IStatisticsPresenter {
    override fun refresh() {

        var lineChart:LineChartData? = model.CreateLineChart(7)
        if (lineChart==null) {
            mView.setLineChartMsg("No data yet!")
        } else {
            mView.setLineChartData(lineChart!!)
        }

    }

    private var mView = view
    private val model: ISleepChartFactory = SleepChartFactory(c)



    init {
        mView.setLineChartFixedAxis(true)
        mView.setLineChartMaxViewport(FIXED_VIEWPORT)
        mView.setLineChartViewport(FIXED_VIEWPORT)
        mView.setLineChartToolbar(Toolbar.OnMenuItemClickListener {
            item ->
            when (item.itemId) {


            }
        })


        mView.setEntriesToolbar(Toolbar.OnMenuItemClickListener {
            item -> when(item.itemId) {

            }
        })
        refresh()
    }


    companion object Consts{
        val FIXED_VIEWPORT =Viewport(0f,6f,6f,0f)

    }

}