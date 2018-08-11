package com.pressurelabs.hibernate.ui.view

import android.support.v7.widget.Toolbar
import lecho.lib.hellocharts.model.Axis
import lecho.lib.hellocharts.model.LineChartData
import lecho.lib.hellocharts.model.Viewport

interface IStatisticsView:IBaseView {
    fun setLineChartData(data: LineChartData)
    fun setLineChartMaxViewport(port:Viewport)
    fun setLineChartViewport(port:Viewport)
    fun setLineChartFixedAxis(fixed:Boolean)
    fun setLineChartMsg(s: String)
    fun setLineChartToolbar(tb:Toolbar.OnMenuItemClickListener)
    fun setEntriesToolbar(tb:Toolbar.OnMenuItemClickListener)
}