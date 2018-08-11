package com.pressurelabs.hibernate.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pressurelabs.hibernate.BaseFragment
import com.pressurelabs.hibernate.R
import com.pressurelabs.hibernate.ui.presenter.IUpgradesPresenter
import com.pressurelabs.hibernate.ui.presenter.UpgradesPresenter
import com.pressurelabs.hibernate.ui.view.IUpgradesView
import com.robertlevonyan.views.chip.Chip

class UpgradesFragment : BaseFragment(), IUpgradesView {
    private var presenter: IUpgradesPresenter? = null;


    private var firstButton: Chip?=null
    private var secondButton:Chip?=null
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.fragment_upgrades, container, false)

        presenter = UpgradesPresenter(this,context)

        firstButton = rootView.findViewById(R.id.upgrade_1_chip) as Chip
        secondButton = rootView.findViewById(R.id.upgrade_2_chip) as Chip
        firstButton?.setOnClickListener (View.OnClickListener {
            var intent:Intent? = presenter?.onFirstButtonClicked()
            if (intent!=null)
                startActivity(intent!!)
        })

        secondButton?.setOnClickListener (View.OnClickListener{
            var intent:Intent? = presenter?.onSecondButtonClicked()
            if (intent!=null)
                startActivity(intent!!)
        })
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
        fun newInstance(sectionNumber: Int): UpgradesFragment {
            val fragment = UpgradesFragment()
            val args = Bundle()
            args.putInt(ARG_SECTION_NUMBER, sectionNumber)
            fragment.arguments = args
            return fragment
        }
    }

}