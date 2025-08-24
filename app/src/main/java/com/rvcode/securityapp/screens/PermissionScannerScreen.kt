package com.rvcode.securityapp.screens

import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
<<<<<<< HEAD
=======
import androidx.compose.material3.Card
>>>>>>> 0eccd0173672455d20b0efd96dc0e9f84904c9dd
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import com.rvcode.securityapp.model.AppWithRisk
<<<<<<< HEAD
import com.rvcode.securityapp.services.ClipboardAccessibilityService
=======
import com.rvcode.securityapp.sevices.ClipboardAccessibilityService

import com.rvcode.securityapp.sevices.HardwareMonitorService
>>>>>>> 0eccd0173672455d20b0efd96dc0e9f84904c9dd
import com.rvcode.securityapp.utilitycompose.RiskyAppView
import com.rvcode.securityapp.utilitycompose.showLoading

import com.rvcode.securityapp.viewmodels.MonitoringViewModel
import com.rvcode.securityapp.viewmodels.PermissionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PermissionScannerScreen(viewModel: PermissionViewModel = hiltViewModel()){
    Log.d("SCREEN","PermissionScannerScreen")
    val riskyApps = viewModel.riskyApps.collectAsState().value

    val isLoading = viewModel.isLoading.collectAsState().value

    val monitoringViewModel: MonitoringViewModel = hiltViewModel()

    val context = LocalContext.current
    val isClipboardServiceEnable = isAccessibilityServiceEnabled(context, ClipboardAccessibilityService::class.java)

//    val isNotificationAccessEnabled =
//        isNotificationServiceEnabled(context, "com.rvcode.securityapp/.sevices.PhishingNotificationListener")




    LaunchedEffect(key1 = Unit){
        monitoringViewModel.startMonitoring()

<<<<<<< HEAD
=======

>>>>>>> 0eccd0173672455d20b0efd96dc0e9f84904c9dd
    }



    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Permission Abuse Scanner")
                }
            )
        }
    ) {

        Column(
            modifier = Modifier.fillMaxSize().padding(it),
            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center

        ) {
            if(!isClipboardServiceEnable){
                EnableServicePrompt()
            }

            // 🔹 Show prompt if Notification listener not enabled
//            if (!isNotificationAccessEnabled) {
//                EnableNotificationServicePrompt()
//            }

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                if(isLoading){
                    showLoading()
                }else if(riskyApps.isEmpty()){
                    Text(
                        text = "No apps with high-risk permission combinations found.",
                        modifier = Modifier.padding(16.dp),
                        textAlign = TextAlign.Center
                    )
                }else {
                    AppList(riskyApps)
                }

            }
        }

    }



}

private fun isAccessibilityServiceEnabled(context: Context, service: Class<*>): Boolean {
    val prefString = Settings.Secure.getString(context.contentResolver, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES)
    return prefString != null && prefString.contains(context.packageName + "/" + service.name)
}

private fun isNotificationServiceEnabled(context: Context, service: String): Boolean {
    val enabledListeners = Settings.Secure.getString(
        context.contentResolver,
        "enabled_notification_listeners"
    )
    return enabledListeners != null && enabledListeners.contains(service)
}

@Composable
private fun AppList(riskyApps: List<AppWithRisk>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        items(riskyApps) { app ->
            RiskyAppView(app = app)
        }
    }
}


@Composable
private fun EnableServicePrompt(){
    val context = LocalContext.current
    OutlinedCard(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(2.dp, Color.Blue)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        )
        {
            Text(
                text = "For phishing protection, you must enable the clipboard monitoring service.",
                style = MaterialTheme.typography.labelLarge,

                )
            Spacer(modifier = Modifier.height(8.dp))
            ElevatedButton(onClick = {
                // This intent takes the user directly to the Accessibility settings page
                val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                context.startActivity(intent)
            }) {
                Text("Enable in Settings")
            }
        }
    }
}

@Composable
private fun EnableNotificationServicePrompt() {
    val context = LocalContext.current
    OutlinedCard(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(2.dp, Color.Blue)
    ){
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            Text(
                text = "Enable Notification Monitoring to detect phishing in incoming notifications.",
                style = MaterialTheme.typography.labelLarge,
            )
            Spacer(Modifier.height(8.dp))
            ElevatedButton(onClick = {
                val intent = Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS")
                context.startActivity(intent)
            }) {
                Text("Enable Notification Service")
            }
        }
    }
}
