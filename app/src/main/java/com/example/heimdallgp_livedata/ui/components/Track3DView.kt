package com.example.heimdallgp_livedata.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.sp
import com.example.heimdallgp_livedata.CarState
import com.example.heimdallgp_livedata.Point3D
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

@Composable
fun Track3DView(
    trackPoints: List<Point3D>,
    cars: List<CarState>,
    selectedCar: CarState?,
    onCarClick: (CarState) -> Unit,
    modifier: Modifier = Modifier
) {
    var angleY by remember { mutableFloatStateOf(0.5f) }
    var angleX by remember { mutableFloatStateOf(0.8f) }
    var scale by remember { mutableFloatStateOf(0.6f) }

    val textMeasurer = rememberTextMeasurer()

    val projectPoint: (Point3D, Float, Float) -> Offset = { point, cX, cY ->
        val x1 = point.x * cos(angleY) - point.z * sin(angleY)
        val z1 = point.x * sin(angleY) + point.z * cos(angleY)

        val y2 = point.y * cos(angleX) - z1 * sin(angleX)
        val z2 = point.y * sin(angleX) + z1 * cos(angleX)

        val cameraDist = 3000f
        val zFinal = z2 + cameraDist
        val factor = cameraDist / zFinal

        val x2d = x1 * factor * scale
        val y2d = y2 * factor * scale

        Offset(cX + x2d, cY + y2d)
    }

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures { tapOffset ->
                    val centerX = size.width / 2
                    val centerY = size.height / 2

                    for (car in cars) {
                        val trackPoint = trackPoints.getOrElse(car.positionIndex) { Point3D(0f,0f,0f) }
                        val screenPos = projectPoint(trackPoint, centerX.toFloat(), centerY.toFloat())

                        val distance = sqrt((tapOffset.x - screenPos.x).pow(2) + (tapOffset.y - screenPos.y).pow(2))

                        if (distance < 40f * scale + 30f) {
                            onCarClick(car)
                            return@detectTapGestures
                        }
                    }
                }
            }
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, _ ->
                    scale = (scale * zoom).coerceIn(0.2f, 3.0f)
                    angleY += pan.x * 0.003f
                    angleX = (angleX + pan.y * 0.003f).coerceIn(0.1f, 1.5f)
                }
            }
    ) {
        val centerX = size.width / 2
        val centerY = size.height / 2

        val pathPoints = trackPoints.map { projectPoint(it, centerX, centerY) }

        if (pathPoints.isNotEmpty()) {
            drawPoints(
                points = pathPoints,
                pointMode = PointMode.Polygon,
                color = Color(0xFFFF5722),
                strokeWidth = 30f * scale,
                cap = StrokeCap.Round
            )
            drawPoints(
                points = pathPoints,
                pointMode = PointMode.Polygon,
                color = Color(0xFF1A1A1A),
                strokeWidth = 22f * scale,
                cap = StrokeCap.Round
            )
        }

        cars.forEach { car ->
            val trackPoint = trackPoints.getOrElse(car.positionIndex) { Point3D(0f,0f,0f) }
            val screenPos = projectPoint(trackPoint, centerX, centerY)

            val isSelected = car == selectedCar
            val carColor = if(isSelected) Color.White else Color(0xFF1E88E5)
            val baseRadius = 16f * scale
            val radius = if (isSelected) baseRadius * 1.3f else baseRadius

            drawCircle(color = Color.White, radius = radius + (4f * scale), center = screenPos)
            drawCircle(color = carColor, radius = radius, center = screenPos)

            val textLayout = textMeasurer.measure(
                text = car.driver.shortName,
                style = TextStyle(color = Color.White, fontSize = (10f * scale).sp, fontWeight = FontWeight.Bold)
            )
            drawText(
                textLayoutResult = textLayout,
                topLeft = Offset(screenPos.x - textLayout.size.width / 2, screenPos.y - textLayout.size.height / 2)
            )
        }
    }
}