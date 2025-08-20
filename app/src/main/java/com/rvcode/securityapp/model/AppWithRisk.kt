package com.rvcode.securityapp.model

data class AppWithRisk(
    val appName: String,
    val packageName: String,
    val riskDescription: String,
)
