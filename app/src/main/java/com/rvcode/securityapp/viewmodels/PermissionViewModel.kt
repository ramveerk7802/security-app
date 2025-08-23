package com.rvcode.securityapp.viewmodels

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rvcode.securityapp.model.AppWithRisk
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class PermissionViewModel @Inject constructor(@ApplicationContext private val context: Context) : ViewModel(){

    private val _riskyApps = MutableStateFlow<List<AppWithRisk>>(emptyList())
    val riskyApps = _riskyApps.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        scanForRiskyApps()
    }

    fun scanForRiskyApps() {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val pm = context.packageManager
                val installedApps = pm.getInstalledPackages(PackageManager.GET_PERMISSIONS)
                val appsWithRisk = mutableListOf<AppWithRisk>()

                val dangerousCombo = setOf(
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.READ_SMS,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )

                for (app in installedApps) {
                    val requestedPermissions = app.requestedPermissions ?: continue
                    val packageName = app.packageName

                    // First, an efficient check to see if the app even requests the dangerous combo
                    if (requestedPermissions.toSet().containsAll(dangerousCombo)) {

                        val allPermissionsGranted = dangerousCombo.all { permission ->
                            pm.checkPermission(permission, packageName) == PackageManager.PERMISSION_GRANTED
                        }

                        if (allPermissionsGranted) {
                            val appName = app.applicationInfo?.loadLabel(pm).toString()
                            appsWithRisk.add(
                                AppWithRisk(
                                    appName = appName,
                                    packageName = packageName,
                                    riskDescription = "This app can use the camera, record audio, and track your precise location."
                                )
                            )
                        }
                    }
                }

                // Switch back to the main thread to update the UI state
                withContext(Dispatchers.Main) {
                    _riskyApps.value = appsWithRisk
                }

            } catch (e: Exception) {
                Log.d("PermissionViewModel", "Error scanning for risky apps: ${e.message}")
            } finally {
                // Switch back to the main thread to update the UI state
                withContext(Dispatchers.Main) {
                    _isLoading.value = false
                }
            }
        }
    }
}