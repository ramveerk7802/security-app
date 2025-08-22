package com.rvcode.securityapp.screens

import android.Manifest

import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.rvcode.securityapp.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen (navigateForRequestPermission: () -> Unit, navigateForPermissionScanner: () -> Unit){

val context = LocalContext.current
    LaunchedEffect(key1 = Unit){
        delay(2000)
        val basePermissions = listOf(
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

        val allPotentialPermissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            basePermissions + Manifest.permission.POST_NOTIFICATIONS
        } else {
            basePermissions
        }

        val allGranted = allPotentialPermissions.all {
            context.checkSelfPermission(it) == PackageManager.PERMISSION_GRANTED
        }
        if(allGranted){
            navigateForPermissionScanner()
        }else{
            navigateForRequestPermission()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){

        AsyncImage(
            model = R.drawable.final_security_logo,
            contentDescription = "Splash image",
            modifier = Modifier.size(150.dp).clip(shape = CircleShape)
        )
    }
}