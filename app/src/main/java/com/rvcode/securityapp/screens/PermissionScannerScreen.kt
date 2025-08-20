package com.rvcode.securityapp.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rvcode.securityapp.model.AppWithRisk
import com.rvcode.securityapp.utilitycompose.RiskyAppView
import com.rvcode.securityapp.viewmodels.PermissionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PermissionScannerScreen(viewModel: PermissionViewModel = hiltViewModel()){
    val riskyApps = viewModel.riskyApps.collectAsState().value

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Permission Scanner")
                }
            )
        }
    ) {
        App(modifier = Modifier.padding(it),riskyApps)

    }
}


@Composable
private fun App(modifier: Modifier, riskyApps : List<AppWithRisk>){

    LazyColumn(
        modifier = modifier
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