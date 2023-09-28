package com.ezinwa.compose

import android.graphics.Color
import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun Scale(
    modifier: Modifier = Modifier,
    style: ScaleStyle = ScaleStyle(),
    minWeight: Int = 50,
    maxWeight: Int = 100,
    initialWeight: Int = 68,
    onWeightChange: (Int) -> Unit
) {
    val radius = style.radius
    val scaleWidth = style.scaleWidth

    var center by remember { mutableStateOf(Offset.Zero) }
    var circleCenter by remember { mutableStateOf(Offset.Zero) }
    var angle by remember { mutableStateOf(0f) }


    Canvas(modifier = modifier) {
        center = this.center
        circleCenter = Offset(center.x, scaleWidth.toPx() / 2f + radius.toPx())
        val outerRadius = radius.toPx() + scaleWidth.toPx() / 2f
        val innerRadius = radius.toPx() - scaleWidth.toPx() / 2f

        drawContext.canvas.nativeCanvas.apply {
            drawCircle(
                circleCenter.x,
                circleCenter.y,
                radius.toPx(),
                Paint().apply {
                    strokeWidth = scaleWidth.toPx()
                    color = Color.WHITE
                    setStyle(Paint.Style.STROKE)
                    setShadowLayer(60f, 0f, 0f, Color.argb(50, 0, 0, 0))
                }
            )
        }

        //Draw lines
        for (i in minWeight..maxWeight) {
            val angleInRadius = (i - initialWeight + angle - 90) * (PI / 180f).toFloat()

            val lineType = when {
                i % 10 == 0 -> LineType.TenStep
                i % 5 == 0 -> LineType.FiveStep
                else -> LineType.Normal
            }

            val lineLength = when (lineType) {
                LineType.FiveStep -> style.fiveStepLineLength.toPx()
                LineType.Normal -> style.normalLineLength.toPx()
                LineType.TenStep -> style.tenStepLineLength.toPx()
            }

            val lineColor = when (lineType) {
                LineType.FiveStep -> style.fiveStepLineColor
                LineType.Normal -> style.normalLineColor
                LineType.TenStep -> style.tenStepLineColor
            }

            val lineStart = Offset(
                x = (outerRadius - lineLength) * cos(angleInRadius) + circleCenter.x,
                y = (outerRadius - lineLength) * sin(angleInRadius) + circleCenter.y
            )

            val lineEnd = Offset(
                x = (outerRadius) * cos(angleInRadius) + circleCenter.x,
                y = (outerRadius) * sin(angleInRadius) + circleCenter.y
            )

            drawLine(
                color = lineColor,
                start = lineStart,
                end = lineEnd,
                strokeWidth = 1.dp.toPx(),
            )

            // Define the length and angle of the arrowhead
            val arrowheadLength = 10.dp.toPx() // Adjust the length as needed
            val arrowheadAngle = PI / 6.0 // Adjust the angle as needed

            val lineAngle = atan2(lineEnd.y - lineStart.y, lineEnd.x - lineStart.x)

            // Calculate the points for the rotated arrowhead at the start of the line
            val rotatedArrowheadPoint1 = Offset(
                x = lineStart.x + arrowheadLength * cos(lineAngle + arrowheadAngle).toFloat(),
                y = lineStart.y + arrowheadLength * sin(lineAngle + arrowheadAngle).toFloat()
            )

            val rotatedArrowheadPoint2 = Offset(
                x = lineStart.x + arrowheadLength * cos(lineAngle - arrowheadAngle).toFloat(),
                y = lineStart.y + arrowheadLength * sin(lineAngle - arrowheadAngle).toFloat()
            )

            // Draw the rotated arrowhead at the start of the line
            drawPath(
                path = Path().apply {
                    moveTo(lineStart.x, lineStart.y)
                    lineTo(rotatedArrowheadPoint1.x, rotatedArrowheadPoint1.y)
                    lineTo(rotatedArrowheadPoint2.x, rotatedArrowheadPoint2.y)
                    close()
                },
                color = androidx.compose.ui.graphics.Color.Black,
            )
        }
    }
}