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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
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

    val distanceKm = (todaySteps * 0.70f) / 1000
    val timeMinutes = todaySteps / 120f
    val calories = todaySteps * 0.04

    Scaffold(
        containerColor = Color(0xFF1C1C1E),
        topBar = {
            TopBarDark()
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)   // 🔥 VERY IMPORTANT
                .padding(16.dp)
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF2C2C2E)
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressImageStyle(
                        steps = todaySteps,
                        percentage = progress
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            CardContainer {
                StatsRowImage(
                    distanceKm = distanceKm,
                    time = timeMinutes,
                    calories = calories
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            WeeklyGoals()
        }
    }
}

@Composable
fun TopBarDark() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Pedometer",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}


@Composable
fun CardContainer(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF2C2C2E), shape = androidx.compose.foundation.shape.RoundedCornerShape(20.dp))
            .padding(20.dp)
    ) {
        content()
    }
}


@Composable
fun CircularProgressImageStyle(
    steps: Int,
    percentage: Float
) {
    val animatedProgress by animateFloatAsState(
        targetValue = percentage,
        animationSpec = tween(1200)
    )

    Box(
        modifier = Modifier.size(240.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(240.dp)) {

            drawArc(
                color = Color(0xFF2F2F31),
                startAngle = 140f,
                sweepAngle = 260f,
                useCenter = false,
                style = Stroke(18.dp.toPx(), cap = StrokeCap.Round)
            )

            drawArc(
                color = Color(0xFF3CE6C2),
                startAngle = 140f,
                sweepAngle = 260f * animatedProgress,
                useCenter = false,
                style = Stroke(18.dp.toPx(), cap = StrokeCap.Round)
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = steps.toString(),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = "Steps",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
    }
}


@Composable
fun StatsRowImage(
    distanceKm: Float,
    time: Float,
    calories: Double
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        StatItemImage("📍", String.format("%.2f km", distanceKm))
        StatItemImage("⏱️", "${time.toInt()} min")
        StatItemImage("🔥", "${calories.toInt()} kcal")
    }
}

@Composable
fun StatItemImage(icon: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = icon, fontSize = 22.sp)
        Text(text = value, color = Color.White, fontWeight = FontWeight.Bold)
    }
}


@Composable
fun WeeklyGoals() {
    Column {
        Text(
            text = "Weekly Goals",
            color = Color.White,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            listOf(
                "Mon" to true,
                "Tue" to false,
                "Wed" to true,
                "Thu" to true,
                "Fri" to false,
                "Sat" to false,
                "Sun" to null
            ).forEach { (day, status) ->
                GoalDay(day, status)
            }
        }
    }
}

@Composable
fun GoalDay(day: String, status: Boolean?) {
    val color = when (status) {
        true -> Color(0xFF3CE6C2)
        false -> Color(0xFFFF5252)
        null -> Color(0xFFFFC107)
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .background(color, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (status == true) "✓" else if (status == false) "✕" else "⟳",
                color = Color.Black,
                fontSize = 14.sp
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = day, fontSize = 12.sp, color = Color.Gray)
    }
}
