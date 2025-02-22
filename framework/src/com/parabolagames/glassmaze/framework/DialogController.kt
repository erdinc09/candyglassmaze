package com.parabolagames.glassmaze.framework

import com.badlogic.gdx.scenes.scene2d.ui.Dialog
import com.badlogic.gdx.utils.Array

object DialogController {
    private val stages: Array<DialogStage> = Array()

    var pauseResumeFromGame: IPauseResumeFromGame? = null
    val isDialogOpen: Boolean
        get() = stages.fold(0, { acc, next -> acc + next.actors.filterIsInstance<Dialog>().count() }) != 0

    internal fun addStage(stage: DialogStage) = stages.add(stage)
    internal fun removeStage(stage: DialogStage) = stages.removeValue(stage, true)

    internal fun checkPauseOrResume() = pauseResumeFromGame?.let {
        if (stages.fold(0, { acc, next -> acc + next.actors.filter { it is Dialog && it !is IDialogDoesNotPauseResume }.count() }) == 0) {
            pauseResumeFromGame!!.resumeFromGame()
        } else {
            pauseResumeFromGame!!.pauseFromGame()
        }
    }
}