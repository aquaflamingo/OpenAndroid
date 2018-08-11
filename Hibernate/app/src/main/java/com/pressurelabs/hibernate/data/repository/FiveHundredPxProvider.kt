package com.pressurelabs.hibernate.data.repository

import com.pressurelabs.hibernate.data.network.FiveHundredPxService
import com.pressurelabs.hibernate.data.repository.datasource.FiveHundredPxSource

object FiveHundredPxProvider {
    fun provide(): FiveHundredPxSource {
        return FiveHundredPxSource(FiveHundredPxService.Factory.create())
    }
}