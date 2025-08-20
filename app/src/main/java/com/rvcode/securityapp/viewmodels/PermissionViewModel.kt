package com.rvcode.securityapp.viewmodels

import android.content.Context
import android.content.pm.PackageManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rvcode.securityapp.model.AppWithRisk
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PermissionViewModel @Inject constructor(@ApplicationContext private val context: Context) : ViewModel(){

    private val _riskyApps = MutableStateFlow<List<AppWithRisk>>(emptyList())

    val riskyApps = _riskyApps.asStateFlow()

    init {
        scanForRiskyApps()
    }

    fun scanForRiskyApps(){
        viewModelScope.launch {
            val pm = context.packageManager
            val installedApps = pm.getInstalledPackages(PackageManager.GET_PERMISSIONS)

            val appsWithRisk = mutableListOf<AppWithRisk>()

            val dangerousCombo = setOf(
                "android.permission.RECORD_AUDIO",
                "android.permission.READ_SMS",
                "android.permission.ACCESS_FINE_LOCATION"
            )

            for (app in installedApps){
                val requestedPermissions = app.requestedPermissions?:continue

                val permissionSet = requestedPermissions.toSet()

                if(permissionSet.containsAll(dangerousCombo)){
                    val appName = app.applicationInfo?.loadLabel(pm).toString()
                    val packageName = app.packageName
                    appsWithRisk.add(AppWithRisk(
                        appName = appName,
                        packageName = packageName,
                        riskDescription = "This app can record audio, read your SMS, and access your precise location, which is a high-risk combination.")
                    )

                }


            }

            _riskyApps.value = appsWithRisk
        }
    }
}