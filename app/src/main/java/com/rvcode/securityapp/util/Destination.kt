package com.rvcode.securityapp.util

import kotlinx.serialization.Serializable

@Serializable
sealed class Destination{



    @Serializable
    data object Splash: Destination()
    @Serializable
    data object Permission: Destination()

    @Serializable
    data object Dashboard: Destination()

    @Serializable
    data object RequestPermission: Destination()

}
