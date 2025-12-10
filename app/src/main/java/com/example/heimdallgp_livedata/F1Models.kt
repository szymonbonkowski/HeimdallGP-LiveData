package com.example.heimdallgp_livedata

import androidx.compose.ui.graphics.Color

data class Point3D(val x: Float, val y: Float, val z: Float)

data class Driver(
    val id: String,
    val name: String,
    val team: String,
    val teamColor: Color,
    val shortName: String
)

data class CarTelemetry(
    val speedKmh: Int,
    val gear: Int,
    val rpm: Int,
    val throttle: Float,
    val brake: Float
)

data class CarState(
    val driver: Driver,
    val positionIndex: Int,
    val telemetry: CarTelemetry,
    val gapToLeader: String = "+0.000",
    val tireCompound: String = "Soft",
    val lapTime: String = "1:12.462",
    val bestLap: String = "1:12.909",
    val positionText: String = "P1"
)

enum class BottomNavTab {
    LEADERBOARD, TEAM_RADIO, RACE_DATA
}

enum class RaceFlag {
    GREEN, YELLOW, RED, SAFETY_CAR
}