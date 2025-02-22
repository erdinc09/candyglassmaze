package com.parabolagames.glassmaze.classic

import com.parabolagames.glassmaze.shared.IBreakable

internal interface IBreakableInGrid : IBreakable {
    val centerX: Float
    val centerY: Float
    val radius: Float
}
