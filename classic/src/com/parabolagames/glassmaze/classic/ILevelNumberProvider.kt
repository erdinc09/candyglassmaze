package com.parabolagames.glassmaze.classic

internal interface ILevelNumberProvider {
    val levelNumber: Int
    val candyGlassCountToManualBreak: Int
    val spinyGlassCountToManualBreak: Int
    val spinyGlassCount:Int
    val spinyGlassCountCurrent:Int
}