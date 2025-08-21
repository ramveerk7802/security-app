package com.rvcode.securityapp.screens

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

@Composable
fun PermissionRequestScreen(onAllPermissionGranted: () -> Unit,notAllPermissionGranted:()->Unit) {

    Log.d("SCREEN","RequestHardwarePermissions")
    val context = LocalContext.current

    val permissionsToRequest = remember {
        val basePermissions = listOf(
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_SMS,
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            basePermissions + Manifest.permission.POST_NOTIFICATIONS
        } else {
            basePermissions
        }
    }



    val multiplePermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ){permissionMap->
         val allGranted = permissionMap.values.all { it }
        if (allGranted) {
            // ✅ Correct: Navigate only after permissions are confirmed
            onAllPermissionGranted()
        }else{
            notAllPermissionGranted()
        }
    }


    LaunchedEffect(Unit) {
        val allPermissionsAlreadyGranted = permissionsToRequest.all {
            context.checkSelfPermission(it) == PackageManager.PERMISSION_GRANTED
        }

        if (allPermissionsAlreadyGranted) {
            // If we already have permission, navigate immediately
            onAllPermissionGranted()
        } else {
            // Otherwise, launch the permission request dialog
            multiplePermissionLauncher.launch(permissionsToRequest.toTypedArray())
        }
    }



}
