package com.ezinwa.weight_wheel_slider

import android.graphics.Color
import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.core.graphics.withRotation
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin


@Composable
fun Scale(
    modifier: Modifier = Modifier,
    minWeight: Int = 10,
    maxWeight: Int = 100,
    initialWeight: Int = 68,
    style: ScaleStyle = ScaleStyle(),
    onWeightChange: (Int) -> Unit,
) {
    val radius = style.radius
    val scaleWidth = style.scaleWidth

    var center by remember { mutableStateOf(Offset.Zero) }

    var circleCenter by remember { mutableStateOf(Offset.Zero) }
    var angle by remember { mutableStateOf(0f) }
    var dragAngleStarted by remember { mutableStateOf(0f) }
    var oldAngle by remember { mutableStateOf(angle) }

    Canvas(modifier = modifier
        .pointerInput(true){
           detectDragGestures(
               onDragStart = { offset ->
                   dragAngleStarted = atan2(
                       x = circleCenter.y - offset.y,
                       y = circleCenter.x - offset.x
                   ) * (180f / PI).toFloat()
               },

               onDragEnd = {
                   oldAngle = angle
               }
           ) { change, _ ->

               val touchAngle = atan2(
                   circleCenter.x - change.position.x,
                   circleCenter.y - change.position.y
               ) * (180f / PI).toFloat()

               val newAngle = oldAngle + (touchAngle - dragAngleStarted)
               angle = newAngle.coerceIn(
                   minimumValue = initialWeight - maxWeight.toFloat(),
                   maximumValue = initialWeight - minWeight.toFloat()
               )
               onWeightChange((initialWeight - angle).toInt())
           }
    }) {

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
            val arrowheadLength = 5.dp.toPx() // Adjust the length as needed
            val arrowheadAngle = PI / 5.0 // Adjust the angle as needed

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
            if (lineType == LineType.TenStep){
                drawPath(
                    path = Path().apply {
                        moveTo(lineStart.x, lineStart.y)
                        lineTo(rotatedArrowheadPoint1.x, rotatedArrowheadPoint1.y)
                        lineTo(rotatedArrowheadPoint2.x, rotatedArrowheadPoint2.y)
                        close()
                    },
                    color = lineColor,
                )
            }

            drawContext.canvas.nativeCanvas.apply {
                if (lineType == LineType.TenStep) {
                    val textRadius = (outerRadius - lineLength - 5.dp.toPx() - style.textSize.toPx())
                    val x = textRadius * cos(angleInRadius) + circleCenter.x
                    val y = textRadius * sin(angleInRadius) + circleCenter.y

                    withRotation (
                        degrees = angleInRadius * (180f / PI.toFloat()) + 90f,
                        pivotX = x,
                        pivotY = y
                    ) {
                        drawText(
                            abs(i).toString(),
                            x,
                            y,
                            Paint().apply {
                                textSize = style.textSize.toPx()
                                textAlign = Paint.Align.CENTER
                            }
                        )
                    }
                }
            }

            //Draw Indicator
            val middleTop = Offset(
                x = circleCenter.x,
                y = circleCenter.y - innerRadius - style.scaleIndicatorLength.toPx()
            )

            val bottomLeft = Offset(
                x = circleCenter.x - 4f,
                y = circleCenter.y - innerRadius
            )

            val bottomRight = Offset(
                x = circleCenter.x + 4f,
                y = circleCenter.y - innerRadius
            )

            drawPath(
                path = Path().apply {
                    moveTo(middleTop.x, middleTop.y)
                    lineTo(bottomLeft.x, bottomLeft.y)
                    lineTo(bottomRight.x, bottomRight.y)
                },
                color = style.scaleIndicatorColor
            )
        }
    }
}