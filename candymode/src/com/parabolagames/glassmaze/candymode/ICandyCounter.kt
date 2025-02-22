package com.parabolagames.glassmaze.candymode

internal interface ICandyCounter {
    fun incrementCandy(): Int
    val candy: Int
    fun giftCandiesGained(count: Int)
}