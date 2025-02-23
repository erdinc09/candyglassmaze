package com.parabolagames.glassmaze.framework

import com.badlogic.gdx.scenes.scene2d.Stage

interface IPauseResumeFromGame {
    fun pauseFromGame()
    fun resumeFromGame()
    val dialogUiStage: Stage
    val dialogUiStageForInformation: Stage
}