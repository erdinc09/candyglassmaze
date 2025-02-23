package com.parabolagames.glassmaze.loading

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.parabolagames.glassmaze.shared.Constants
import javax.inject.Inject

internal class LoadingBar @Inject constructor() : ProgressBar(0f, 1f, 0.01f, false, ProgressBarStyle()) {
    fun setPositionAfterStageSet(textActorHeight: Float) = setPosition(Constants.WORLD_WIDTH / 2 - width / 2,
            Constants.WORLD_HEIGHT / 2 - height / 2 - textActorHeight / 2 - 0.07f)

    init {
        setRound(false)
        with(style) {

            val progressbarUi = TextureAtlas(Gdx.files.internal("ui/progressbar_ui.atlas"))
            background = TextureRegionDrawable(progressbarUi.findRegion("background"))
            knob = TextureRegionDrawable(progressbarUi.findRegion("knob"))
            knobBefore = TextureRegionDrawable(progressbarUi.findRegion("knob"))

            knob.minWidth = 0.1f
            knob.minHeight = 0.1f

            knobBefore.minWidth = 0.1f
            knobBefore.minHeight = 0.1f

            background.minWidth = 0.1f
            background.minHeight = 0.1f
        }
        value = 0.0f
        setAnimateDuration(0.25f)
        setBounds(0.1f, 0.1f, WIDTH.toFloat(), HEIGHT * SCALE_Y)
    }


    companion object {
        const val WIDTH = 1
        const val HEIGHT = 1
        const val SCALE_Y = 0.1f
    }
}