package com.pressurelabs.hibernate.ui.adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.pressurelabs.hibernate.ui.fragments.MainAlarmFragment
import com.pressurelabs.hibernate.ui.fragments.StatisticsFragment
import com.pressurelabs.hibernate.ui.fragments.UpgradesFragment

class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            when (position) {
                0 -> return MainAlarmFragment.newInstance(position + 1)
                1 -> return StatisticsFragment.newInstance(position+1)
                2 -> return UpgradesFragment.newInstance(position + 1)

            }
            return Fragment()

        }

        override fun getCount(): Int {
            // Show 3 total pages.
            return 3
        }

        override fun getPageTitle(position: Int): CharSequence? {
            when (position) {
                0 -> return "Alarms"
                1 -> return "Statistics"
                2 -> return "Upgrades"
            }

            return null
        }
}