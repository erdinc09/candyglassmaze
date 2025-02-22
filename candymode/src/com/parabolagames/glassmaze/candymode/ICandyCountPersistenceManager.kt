package com.parabolagames.glassmaze.candymode

interface ICandyCountPersistenceManager {
    var bestScore: Int
    var totalScore: Int
    var ironNumberCount: Int
    var handNumberCount: Int
    fun saveCandyMode()
}