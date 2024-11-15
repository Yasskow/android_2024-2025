package fr.uge.colorflags.model

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

class ColoredPath internal constructor(val color: Color, private val points: FloatArray, val size: Int) {
    operator fun plus(position: Offset): ColoredPath {
        val newPoints = if (2 * (size + 1) >= points.size) {
            points.copyOf(points.size * 2)
        } else {
            points
        }
        newPoints[2 * size] = position.x
        newPoints[2 * size + 1] = position.y
        return ColoredPath(color, newPoints, size+1)
    }

    operator fun get(index: Int): Offset =
        Offset(points[2*index], points[2*index+1])

    companion object {
        fun create(color: Color) = ColoredPath(color, FloatArray(32), 0)
    }
}

