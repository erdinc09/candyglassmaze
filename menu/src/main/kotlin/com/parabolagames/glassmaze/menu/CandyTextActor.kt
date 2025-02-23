package com.parabolagames.glassmaze.menu

import com.badlogic.gdx.scenes.scene2d.actions.Actions.*
import com.google.common.base.Preconditions.checkState
import com.parabolagames.glassmaze.framework.BaseActor
import com.parabolagames.glassmaze.framework.ForApp
import com.parabolagames.glassmaze.shared.Assets
import javax.inject.Inject

@ForApp
internal class CandyTextActor @Inject constructor(private val backgroundScaleProvider: IBackgroundScaleProvider, assets: Assets)
    : BaseActor(0f, 0f) {

    init {
        loadTexture(assets.getTexture(Assets.CANDY_GLASS_HIGH_TEXT))
        setOrigin(0f, 0f)
        setScale(backgroundScaleProvider.scale)
    }

    fun setPositionAfterStageSet() {
        val yOffset = height * scaleY / 5
        setPosition(0f, stage.height - height * scaleY - yOffset)
    }

    fun startAnimation() {
        checkState(actions.size == 0)
        checkState(scaleX == scaleY)
        setScale(backgroundScaleProvider.scale)
        setPositionAfterStageSet()
        val scale = scaleX
        val scaleUp = scale * 1.2f
        addAction(forever(
                        sequence(
                                delay(1f),
                                scaleTo(scaleUp, scaleUp, ANIMATION_TIME),
                                scaleTo(scale, scale, ANIMATION_TIME),
                                scaleTo(scaleUp, scaleUp, ANIMATION_TIME),
                                scaleTo(scale, scale, ANIMATION_TIME),
                                delay(5f))))
    }

    fun stopAnimation() = clearActions()

    companion object {
        private const val ANIMATION_TIME = 0.25f
    }
}