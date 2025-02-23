package com.parabolagames.glassmaze.classic

internal interface IMazeBallBreakController {
    fun mazeBallPositionUpdated(x: Float, y: Float, ballDirection: MazeCubeDirection, size: Float, rowCurrent: Int, colCurrent: Int);
}