package com.example.heimdallgp_livedata.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.heimdallgp_livedata.FakeDataManager
import com.example.heimdallgp_livedata.CarState
import com.example.heimdallgp_livedata.BottomNavTab
import com.example.heimdallgp_livedata.ui.components.DriverDetailsPanel
import com.example.heimdallgp_livedata.ui.components.TopRaceBar
import com.example.heimdallgp_livedata.ui.components.Track3DView
import kotlinx.coroutines.delay

@Composable
fun MainScreen() {
    val trackPoints = remember { FakeDataManager.getTrackPoints() }

    var cars by remember {
        mutableStateOf(FakeDataManager.getInitialCarsState(trackPoints.size))
    }

    var selectedCar by remember {
        mutableStateOf<CarState?>(cars.firstOrNull())
    }

    var currentTab by remember { mutableStateOf(BottomNavTab.LEADERBOARD) }

    LaunchedEffect(Unit) {
        while(true) {
            val updatedCars = FakeDataManager.moveCars(cars, trackPoints.size)
            cars = updatedCars

            val currentSelectedId = selectedCar?.driver?.id
            if (currentSelectedId != null) {
                val foundCar = updatedCars.find { it.driver.id == currentSelectedId }
                if (foundCar != null) {
                    selectedCar = foundCar
                }
            }

            delay(50)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F0F0F))
    ) {
        Track3DView(
            trackPoints = trackPoints,
            cars = cars,
            selectedCar = selectedCar,
            onCarClick = { clickedCar ->
                selectedCar = clickedCar
                currentTab = BottomNavTab.LEADERBOARD
            },
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(250.dp)
                .background(Brush.verticalGradient(listOf(Color.Transparent, Color(0xFF0F0F0F))))
        )

        TopRaceBar(modifier = Modifier.align(Alignment.TopCenter))

        if (selectedCar != null) {
            DriverDetailsPanel(
                car = selectedCar!!,
                currentTab = currentTab,
                onTabSelected = { newTab -> currentTab = newTab },
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}