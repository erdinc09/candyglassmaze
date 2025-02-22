package com.parabolagames.glassmaze.classic

import com.parabolagames.glassmaze.classic.glass.GenericCandyGlassBallActorData

internal interface ICandyGainer {
    fun candyGained(candyData: GenericCandyGlassBallActorData, row: Int, col: Int)
    fun candyGlassCrackedButNotGained(row: Int, col: Int)
}