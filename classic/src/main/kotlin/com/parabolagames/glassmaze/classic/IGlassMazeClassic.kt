package com.parabolagames.glassmaze.classic

interface IGlassMazeClassic {

    fun setMenuScreenReVisible()

    fun setClassicModeScreenVisible()

    fun setRandomModeScreenVisible()

    /**
     * @param levelNumber , non null and >0
     */
    fun setClassicModeScreenVisibleForLevelClassicMode(levelNumber: Int)

    /**
     * @param levelNumber , non null and >0
     */
    fun setClassicModeScreenVisibleForLevelRandomMode(levelNumber: Int)
}