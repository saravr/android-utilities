package com.sandymist.android.common.utilities

import javax.inject.Inject

class DeviceUtils @Inject constructor(private val tabletCriteria: TabletCriteria) {
    fun getDeviceType(): String {
        return if (tabletCriteria.shouldBeReferredToAsTablet()) TABLET else PHONE
    }

    companion object {
        const val PHONE = "phone"
        const val TABLET = "tablet"
    }
}
