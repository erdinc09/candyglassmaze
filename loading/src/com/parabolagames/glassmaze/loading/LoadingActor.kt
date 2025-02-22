package com.parabolagames.glassmaze.loading

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.actions.Actions.*
import com.google.common.base.Preconditions.checkState
import com.google.common.base.Supplier
import com.parabolagames.glassmaze.framework.BaseActor
import com.parabolagames.glassmaze.framework.run2
import com.parabolagames.glassmaze.shared.Constants
import javax.inject.Inject

internal class LoadingActor @Inject constructor() : BaseActor(0f, 0f) {

    init {
        loadTexture("candyGlassHighTEXT.png")
        val imageHeight = 0.8f
        setSize(widthHeightRatio * imageHeight, imageHeight)
        setOrigin(width / 2, 0f)
    }

    private fun setPositionAfterStageSet() = setPosition(Constants.WORLD_WIDTH / 2 - width / 2, Constants.WORLD_HEIGHT / 2 - height / 2)

    fun startAnimation(
            isLoadingFinished: Supplier<Boolean>, runActionAfterLoadingFinished: Runnable) {
        checkState(actions.size == 0)
        checkState(scaleX == scaleY)
        setPositionAfterStageSet()
        val scale = scaleX
        val scaleUp = scale * 1.05f
        addAction(forever(sequence(
                scaleTo(scaleUp, scaleUp, ANIMATION_TIME),
                scaleTo(scale, scale, ANIMATION_TIME),
                run2 {
                    if (isLoadingFinished.get()) {
                        runActionAfterLoadingFinished.run()
                        clearActions()
                    }
                })))
    }

    fun stopAnimation() {
        checkState(actions.size == 1)
        clearActions()
        animation.keyFrames.forEach { (it as TextureRegion).texture.dispose() }
        Gdx.app.debug("LoadingActor", "loading actor disposed")
    }

    companion object {
        private const val ANIMATION_TIME = 0.5f
    }
}