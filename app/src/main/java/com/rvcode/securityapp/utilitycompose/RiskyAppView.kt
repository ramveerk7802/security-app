package com.rvcode.securityapp.utilitycompose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rvcode.securityapp.model.AppWithRisk


@Composable
fun RiskyAppView(app: AppWithRisk) {
    ElevatedCard(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp).fillMaxWidth(),
        shape = RoundedCornerShape(20.dp)
    ){
        Column (modifier = Modifier.padding(16.dp)){
            Text(text = app.appName, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = app.riskDescription, style = MaterialTheme.typography.bodyMedium)
        }

    }
}