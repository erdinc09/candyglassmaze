package com.parabolagames.glassmaze.framework

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector2

interface IScreenDrawer {
    fun drawLine(lineData: LineData?)


    data class LineData(
            @JvmField
            val start: Vector2,
            @JvmField
            val end: Vector2,
            @JvmField
            val lineWidth: Float,
            @JvmField
            val colorStart: Color,
            @JvmField
            val colorEnd: Color,
    )
}