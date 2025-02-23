package com.parabolagames.glassmaze.framework

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Dialog
import com.badlogic.gdx.utils.viewport.Viewport

class DialogStage(viewport: Viewport) : Stage(viewport) {

    private val stageRoot = StageRoot();

    init {
        root = stageRoot
        DialogController.addStage(this)
    }

    override fun addActor(actor: Actor) {
        try {
            super.addActor(actor)
        } finally {
            if (actor is Dialog && actor !is IDialogDoesNotPauseResume) {
                DialogController.checkPauseOrResume()
            }
        }
    }

    private inner class StageRoot : Group() {
        override fun removeActor(actor: Actor, unfocus: Boolean): Boolean {
            try {
                return super.removeActor(actor, unfocus)
            } finally {
                if (actor is Dialog && actor !is IDialogDoesNotPauseResume) {
                    DialogController.checkPauseOrResume()
                }
            }
        }
    }

    override fun dispose() {
        super.dispose()
        DialogController.removeStage(this)
    }

    companion object {
        const val TAG = "DialogStage"
    }
}