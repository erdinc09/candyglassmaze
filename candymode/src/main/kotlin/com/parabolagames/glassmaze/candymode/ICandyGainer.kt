package com.parabolagames.glassmaze.candymode

import com.parabolagames.glassmaze.candymode.glass.GenericCandyGlassBallActorData

internal interface ICandyGainer {
    fun candyGained(candyData: GenericCandyGlassBallActorData)
}