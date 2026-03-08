package com.example.app_firebase.components

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection

class BottomCurveShape(private val curveHeight: Dp) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val curveHeightPx = with(density) { curveHeight.toPx() }
        val path = Path().apply {
            moveTo(0f, 0f)
            lineTo(size.width, 0f)
            lineTo(size.width, size.height - curveHeightPx)


            quadraticTo(
                x1 = size.width / 2f,
                y1 = size.height + curveHeightPx,
                x2 = 0f,
                y2 = size.height - curveHeightPx
            )
            close()
        }
        return Outline.Generic(path)
    }
}