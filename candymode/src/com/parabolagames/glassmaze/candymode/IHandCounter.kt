package com.parabolagames.glassmaze.candymode

internal interface IHandCounter {
    fun decrementHandCount(): Long
    var handCount: Long
    fun pressedWhenHandCountIsZero()
}