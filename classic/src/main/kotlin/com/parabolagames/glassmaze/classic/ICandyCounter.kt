package com.parabolagames.glassmaze.classic

internal interface ICandyCounter {
    fun incrementCandy(): Int
    val candy: Int
    fun giftCandiesWillBeGained()
    val candyInLevel: Int
    val isCandyGiftForAllGlassCrackedGained: Boolean
    fun updateFromDataPersistenceManager()
}