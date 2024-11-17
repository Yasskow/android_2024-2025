package fr.uge.colorflags.model

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

data class Sketch(val id: Long, val paths: List<ColoredPath> = listOf()) {

    operator fun plus(color: Color) =
        Sketch(id, paths + ColoredPath.create(color))

    operator fun plus(position: Offset) =
        Sketch(id, paths.subList(0, paths.size-1) + (paths.last() + position))

    companion object {
        private var SERIAL: Long = 0L

        fun createEmpty() = Sketch(SERIAL++)
    }
}
