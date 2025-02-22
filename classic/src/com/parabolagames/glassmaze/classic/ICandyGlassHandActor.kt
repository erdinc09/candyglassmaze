package com.parabolagames.glassmaze.classic

internal interface ICandyGlassHandActor {
    fun decrementHandCount(): Int
    val handCount: Int
    fun makePassive()
    fun updateFromDataPersistenceManager()
    val isBreakingEnabled: Boolean
}