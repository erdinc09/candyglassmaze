package com.parabolagames.glassmaze.candymode

internal interface ILifeCounter {
    fun decrementLife(): Int
    val lifeCount: Int
}