package com.pressurelabs.hibernate.util

import com.pressurelabs.hibernate.R
import com.pressurelabs.hibernate.domain.UIAttributeBundle

object UIBundles {
    val twelve_hour_clock: UIAttributeBundle = UIAttributeBundle.Builder()
            .drawable(R.drawable.ic_12_hour_half_sun)
            .text("12 Hour")
            .build()
    val twenty_four_hour_clock: UIAttributeBundle = UIAttributeBundle.Builder()
            .drawable(R.drawable.ic_24_hour_full_sun)
            .text("24 Hour")
            .build()

    val alarm_is_on: UIAttributeBundle = UIAttributeBundle.Builder()
            .drawable(R.drawable.ic_alarm_on_24dp)
            .text("Alarm On")
            .build()

    val alarm_is_off: UIAttributeBundle = UIAttributeBundle.Builder()
            .drawable(R.drawable.ic_alarm_off_24dp)
            .text("Alarm Off")
            .build()
}