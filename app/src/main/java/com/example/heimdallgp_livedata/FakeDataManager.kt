package com.example.heimdallgp_livedata

import androidx.compose.ui.graphics.Color
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

object FakeDataManager {

    fun getTrackPoints(): List<Point3D> {
        val points = mutableListOf<Point3D>()
        val segments = 800
        for (i in 0 until segments) {
            val t = (i.toDouble() / segments) * 2 * Math.PI
            val x = 1400 * sin(t) + 300 * sin(3 * t)
            val z = 1000 * cos(t) + 200 * cos(2 * t)
            points.add(Point3D(x.toFloat(), 0f, z.toFloat()))
        }
        return points
    }

    private val drivers = listOf(
        Driver("1", "Max Verstappen", "Red Bull Racing", Color(0xFF1E41FF), "1"),
        Driver("2", "Lando Norris", "McLaren F1 Team", Color(0xFFFF8700), "4"),
        Driver("3", "Charles Leclerc", "Scuderia Ferrari", Color(0xFFFF0000), "16"),
        Driver("4", "Lewis Hamilton", "Mercedes-AMG", Color(0xFF00D2BE), "44"),
        Driver("5", "George Russell", "Mercedes-AMG", Color(0xFF00D2BE), "63"),
        Driver("6", "Fernando Alonso", "Aston Martin", Color(0xFF006F62), "14")
    )

    fun getInitialCarsState(trackLength: Int): List<CarState> {
        return drivers.mapIndexed { index, driver ->
            CarState(
                driver = driver,
                positionIndex = (index * (trackLength / drivers.size)).coerceIn(0, trackLength - 1),
                telemetry = generateFakeTelemetry(),
                gapToLeader = if (index == 0) "Interval" else "+${String.format("%.3f", index * 2.1)}s",
                positionText = "P${index + 1}",
                tireCompound = listOf("Soft", "Medium", "Hard").random()
            )
        }
    }

    fun moveCars(currentCars: List<CarState>, trackLength: Int): List<CarState> {
        return currentCars.map { car ->
            val speed = Random.nextInt(2, 5)
            val nextIndex = (car.positionIndex + speed) % trackLength

            car.copy(
                positionIndex = nextIndex,
                telemetry = if (Random.nextInt(10) > 8) generateFakeTelemetry() else car.telemetry
            )
        }
    }

    private fun generateFakeTelemetry(): CarTelemetry {
        return CarTelemetry(
            speedKmh = Random.nextInt(200, 340),
            gear = Random.nextInt(5, 9),
            rpm = Random.nextInt(9000, 12500),
            throttle = Random.nextFloat(),
            brake = 0f
        )
    }
}