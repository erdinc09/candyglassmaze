package com.parabolagames.glassmaze.classic

internal interface IMazeCubeControl {
    fun moveMazeCube(direction: MazeCubeDirection)
    val isMazeBallMoving: Boolean
}