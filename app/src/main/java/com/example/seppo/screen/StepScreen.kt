package com.example.seppo.screen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.seppo.viewModel.StepViewModel



@Composable
fun StepScreen(
    viewModel: StepViewModel = hiltViewModel()
) {
    val todaySteps by viewModel.todaySteps.collectAsState()

    val dailyGoal = 10000
    val progress = (todaySteps / dailyGoal.toFloat()).coerceIn(0f, 1f)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 16.dp)
    ) {

        // 🔹 Top Bar
        Spacer(modifier = Modifier.height(24.dp))
        TopBar()

        Spacer(modifier = Modifier.height(24.dp))

        // 🔹 Circular Progress
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressBr(
                steps = todaySteps,
                percentage = progress
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 🔹 Stats Row
        StatsRow(
            distanceKm = 6.43,
            calories = 3.43,
            time = "1h 10m"
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 🔹 Schedule
        ScheduleRow()

        Spacer(modifier = Modifier.weight(1f))

    }
}

@Composable
fun CircularProgressBr(
    steps: Int,
    percentage: Float,
    radius: Dp = 120.dp,
    strokeWidth: Dp = 14.dp
) {
    val animatedProgress by animateFloatAsState(
        targetValue = percentage,
        animationSpec = tween(1000)
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(radius * 2)
    ) {
        Canvas(modifier = Modifier.size(radius * 2)) {

            // Background ring
            drawArc(
                color = Color(0xFFE6EEF6),
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = strokeWidth.toPx())
            )

            // Progress ring
            drawArc(
                color = Color(0xFF1E88E5),
                startAngle = -90f,
                sweepAngle = 360f * animatedProgress,
                useCenter = false,
                style = Stroke(
                    width = strokeWidth.toPx(),
                    cap = StrokeCap.Round
                )
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = steps.toString(),
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Steps",
                fontSize = 16.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun TopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Pedometer",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Text(text = "👑")
    }
}


@Composable
fun StatsRow(
    distanceKm: Double,
    calories: Double,
    time: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        StatItem("📍", "$distanceKm km", "Location")
        StatItem("🔥", "$calories kcal", "Calories")
        StatItem("⏱️", time, "Time")
    }
}

@Composable
fun StatItem(icon: String, value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = icon, fontSize = 22.sp)
        Text(text = value, fontWeight = FontWeight.Bold)
        Text(text = label, fontSize = 12.sp, color = Color.Gray)
    }
}

@Composable
fun ScheduleRow() {
    Column {
        Text(
            text = "Schedule",
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            listOf("M", "T", "W", "T", "F", "S", "S").forEach {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(36.dp)
                        .background(
                            if (it == "T") Color(0xFF1E88E5) else Color.Transparent,
                            shape = CircleShape
                         )
                ) {
                    Text(
                        text = it,
                        color = if (it == "T") Color.White else Color.Gray
                    )
                }
            }
        }
    }
}



