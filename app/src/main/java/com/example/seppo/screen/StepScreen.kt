package com.example.seppo.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.seppo.viewModel.StepViewModel

@Composable
fun StepScreen(
    viewModel: StepViewModel = hiltViewModel()
) {
    val todaySteps by viewModel.todaySteps.collectAsState()
    val last7Days by viewModel.last7Days.collectAsState()

    Column (modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally){
        Text(text = "Steps Today: $todaySteps")

        Button(onClick = { viewModel.resetTodaySteps() }) {
            Text("Reset Today")
        }

        last7Days.forEach {
            Text("${it.date} → ${it.steps}")
        }
    }
}
