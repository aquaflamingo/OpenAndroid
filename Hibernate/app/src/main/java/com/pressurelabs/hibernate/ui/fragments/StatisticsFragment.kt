package com.pressurelabs.hibernate.ui.fragments

import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.pressurelabs.hibernate.BaseFragment
import com.pressurelabs.hibernate.R
import com.pressurelabs.hibernate.ui.presenter.IStatisticsPresenter
import com.pressurelabs.hibernate.ui.presenter.StatisticsPresenter
import com.pressurelabs.hibernate.ui.view.IStatisticsView
import lecho.lib.hellocharts.model.*
import lecho.lib.hellocharts.view.LineChartView
import lecho.lib.hellocharts.model.Viewport


class StatisticsFragment : BaseFragment(), IStatisticsView {
    override fun setEntriesToolbar(tb: Toolbar.OnMenuItemClickListener) {
        entriesCardTb?.inflateMenu(R.menu.menu_entries_toolbar)
        entriesCardTb?.setTitle(R.string.toolbar_title_entries_card)
        entriesCardTb?.setOnMenuItemClickListener(tb)
    }

    override fun setLineChartToolbar(listener: Toolbar.OnMenuItemClickListener) {
        lineChartTb?.inflateMenu(R.menu.menu_line_chart_toolbar)
        lineChartTb?.setTitle(R.string.toolbar_title_line_chart)
        lineChartTb?.setOnMenuItemClickListener(listener)
    }

    override fun setLineChartMsg(s: String) {
        placeHolderChartText?.visibility = View.VISIBLE
        placeHolderChartText?.text=s
    }

    override fun setLineChartFixedAxis(fixed: Boolean) {
        lineChart?.isViewportCalculationEnabled = !fixed
    }

    override fun setLineChartMaxViewport(port: Viewport) {
        lineChart?.maximumViewport = port
    }

    override fun setLineChartViewport(port: Viewport) {
        lineChart?.currentViewport = port
    }

    override fun setLineChartData(data: LineChartData) {
        placeHolderChartText?.visibility = View.GONE
        lineChart?.lineChartData = data
    }

    private var presenter: IStatisticsPresenter? = null;

    private var lineChart:LineChartView?=null
    private var placeHolderChartText:TextView?=null
    private var placeHolderEntryText:TextView?=null
    private var lineChartTb: Toolbar?=null
    private var entriesCardTb: Toolbar? =null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.fragment_statistics, container, false)
        lineChart = rootView.findViewById(R.id.line_chart) as LineChartView
        placeHolderChartText = rootView.findViewById(R.id.placeholder_chart_1_no_data_display) as TextView
        lineChartTb = rootView.findViewById(R.id.toolbar_line_chart) as Toolbar
        placeHolderEntryText = rootView.findViewById(R.id.placeholder_entries_coming_soon) as TextView
        entriesCardTb = rootView.findViewById(R.id.toolbar_list_entries) as Toolbar

        // Set up line chart specifics
        lineChart?.isZoomEnabled = false//disable zoom
        lineChart?.isScrollEnabled = true
        presenter=StatisticsPresenter(context,this)
        return rootView
    }

    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private val ARG_SECTION_NUMBER = "section_number"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        fun newInstance(sectionNumber: Int): StatisticsFragment {
            val fragment = StatisticsFragment()
            val args = Bundle()
            args.putInt(ARG_SECTION_NUMBER, sectionNumber)
            fragment.arguments = args
            return fragment
        }
    }

}