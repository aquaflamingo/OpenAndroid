package com.pressurelabs.hibernate.util

object Conversion {
    fun hr_to_ms(hours:Long):Long {
        return m_to_ms(hours*60)
    }

    fun d_to_ms(days:Long):Long {
        return hr_to_ms(days*24)
    }

    fun m_to_ms(mins:Long):Long {
        return s_to_ms(mins*60)
    }

    fun s_to_ms(sec:Long):Long {
        return sec*1000
    }

    fun add_one_day(ms:Long):Long {
        return ms+86400000
    }
 }