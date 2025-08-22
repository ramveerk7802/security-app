package com.rvcode.securityapp.utilitycompose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.rvcode.securityapp.model.AppWithRisk


@Composable
fun RiskyAppView(app: AppWithRisk) {
    ElevatedCard(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp).fillMaxWidth(),
        shape = RoundedCornerShape(20.dp)
    ){
        Column (modifier = Modifier.padding(16.dp)){
            Text(
                text = app.appName,
                style = MaterialTheme.typography.titleMedium,
                color = Color.Red
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = app.riskDescription, style = MaterialTheme.typography.bodyMedium)
        }

    }
}


@Composable
fun showLoading(){

    Dialog(
        onDismissRequest = {}
    ){
        Box(modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,

        ){
            CircularProgressIndicator()
        }

    }

}