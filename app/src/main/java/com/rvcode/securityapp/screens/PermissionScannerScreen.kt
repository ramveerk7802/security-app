package com.rvcode.securityapp.screens

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rvcode.securityapp.model.AppWithRisk
import com.rvcode.securityapp.sevices.HardwareMonitorService
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

    LaunchedEffect(key1 = Unit){
        monitoringViewModel.startMonitoring()
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
        Box(modifier = Modifier.fillMaxSize().padding(it),
            contentAlignment = Alignment.Center){
            if(isLoading){
                showLoading()
            }else if (riskyApps.isEmpty()){
                Text(text = "No apps with high-risk permission combinations found.")
            }else{
                App(riskyApps)
            }
        }


    }
}


@Composable
private fun App( riskyApps : List<AppWithRisk>){

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(5.dp)

    ){
        if(riskyApps.isEmpty()){
            item {
                Text(
                    text = "No apps with high-risk permission combinations found.",
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
        else{
            items(riskyApps){app->
                RiskyAppView(app = app)
            }
        }
    }

}