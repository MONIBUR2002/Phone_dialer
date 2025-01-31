package com.moniapps.surefydialer.screens.CallScreen

import android.os.Message
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.moniapps.surefydialer.navigation.ScreenGraph
import java.util.Timer
import java.util.TimerTask
import kotlin.concurrent.scheduleAtFixedRate

@Composable
fun CallScreen(modifier: Modifier = Modifier,navController: NavController, number: String) {


    val context = LocalContext.current


    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Blue,
                            Color.Black
                        )
                    )
                )
        ) {
            Text(text = number, fontWeight = FontWeight.SemiBold, fontSize = 20.sp, color = Color.White)
            Spacer(modifier = modifier.height(20.dp))
            TimerComposable()
            Spacer(modifier = Modifier.height(300.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.Red),
                        modifier = Modifier
                            .size(60.dp)
                            .clickable {
                                CallDialog.rejectCall(context = context)
                                navController.navigate(ScreenGraph.RecentScreen.route)

                            },
                        shape = RoundedCornerShape(22.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Icon(modifier = Modifier.rotate(135f), imageVector = Icons.Default.Phone, contentDescription = "Pick up", tint = Color.White)
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = "End call", color = Color.White)
                }
            }

        }
    }
}
@Composable
fun TimerComposable() {
    var timeElapsed by remember { mutableStateOf(0) }
    var timer by remember { mutableStateOf<Timer?>(null) }
    val lifecycleOwner = LocalLifecycleOwner.current
    var isVisible by remember { mutableStateOf(false) }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    isVisible = true
                }

                Lifecycle.Event.ON_PAUSE -> {
                    isVisible = false
                }

                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(isVisible) {
        if (isVisible) {
            timer = Timer()
            val task = object : TimerTask() {
                override fun run() {
                    timeElapsed++
                    Log.d("TimerComposable", "Time elapsed: $timeElapsed")
                }
            }
            timer?.scheduleAtFixedRate(task, 1000L, 1000L)
        } else {
            timer?.cancel()
            timer?.purge()
            timer = null
        }
    }

    Box(modifier = Modifier, contentAlignment = Alignment.Center) {
        Text(text = formatTime(timeElapsed))
    }
}

private fun formatTime(seconds: Int): String {
    val hours = seconds / 3600
    val minutes = (seconds % 3600) / 60
    val remainingSeconds = seconds % 60

    return when {
        hours > 0 -> String.format("%02d:%02d:%02d", hours, minutes, remainingSeconds)
        else -> String.format("%02d:%02d", minutes, remainingSeconds)
    }
}
@Preview
@Composable
private fun PreCompose() {
    CallScreen( navController = rememberNavController(), number = "34224324324")
}