package com.parabolagames.glassmaze.classic

import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.actions.Actions.*
import com.badlogic.gdx.scenes.scene2d.ui.Container
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import com.google.common.base.Preconditions.checkState
import com.parabolagames.glassmaze.framework.ForGame
import com.parabolagames.glassmaze.framework.TableActor
import com.parabolagames.glassmaze.framework.run2
import com.parabolagames.glassmaze.shared.Assets
import com.parabolagames.glassmaze.shared.SingleTextureActor
import javax.inject.Inject

@ForGame
internal class HintActor @Inject constructor(
        assets: Assets,
        levelNumberProvider: ILevelNumberProvider?,
        subMode: ClassicModeSubMode) : TableActor() {

    var touchListener: (() -> Unit)? = null
        set(touchListener) {
            checkState(this.touchListener == null)
            field = touchListener
        }

    init {
        loadAnimationFromTextureRegions(
                assets.getTexturesFromTextureAtlas(Assets.HINT_ICON), 0.05f, true)
        setSize(HEIGHT * (width / height), HEIGHT)
        setOrigin(width / 2, height / 2)
        isAnimationPaused = true
        addAction(sequence(delay(1.75f), Actions.run { isAnimationPaused = false }))
        if (subMode === ClassicModeSubMode.CLASSIC && levelNumberProvider != null
                && levelNumberProvider.levelNumber <= HINT_EXPLANATION_SHOW_LIMIT_LEVEL
                && levelNumberProvider.levelNumber > HINT_EXPLANATION_SHOW_MIN_LEVEL) {
            addExplanationLabel(assets)
        }
        addListener(
                object : ClickListener() {
                    override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                        touchListener?.let { it() }
                        return false
                    }
                })
    }


    private fun addExplanationLabel(assets: Assets) {

        val lblExp = Label(" Touch For Hints!", LabelStyle(assets.getBitmapFont(Assets.FONT_COSMIC_SANS_RED), null))
                .apply {
                    setAlignment(Align.left)
                    wrap = false
                    setFontScale(0.0020f)
                }

        val pad = width * 2 + lblExp.width
        val arrow = SingleTextureActor(assets, Assets.ARROW_LEFT_RED, 0.1f)
                .apply {
                    setPosition(this@HintActor.width + 0.02f, this@HintActor.height * 0.25f)

                    moveBy(-pad, 0f)
                    addAction(
                            sequence(
                                    delay(1f),
                                    moveBy(pad, 0f, 1f),
                                    delay(3f),
                                    moveBy(-pad, 0f, 1f),
                                    removeActor()))

                }
        addActor(arrow)

        val lblExpContainer = Container(lblExp)
                .apply {
                    setOrigin(lblExp.width / 2, lblExp.height / 2)
                    isTransform = true
                    setSize(lblExp.width, lblExp.height)
                    setPosition(arrow.x + 0.1f, lblExp.height * 0.45f)

                    addAction(
                            sequence(
                                    delay(1f),
                                    moveBy(pad, 0f, 1f),
                                    parallel(
                                            delay(3f),
                                            run2 {
                                                addAction(
                                                        sequence(
                                                                scaleBy(0.1f, 0.1f, 0.4f),
                                                                scaleBy(-0.1f, -0.1f, 0.4f),
                                                                scaleBy(0.1f, 0.1f, 0.4f),
                                                                scaleBy(-0.1f, -0.1f, 0.4f)))
                                            }),
                                    moveBy(-pad, 0f, 1f),
                                    removeActor()))
                }
        addActor(lblExpContainer)
    }

    companion object {
        private const val HEIGHT = 0.20f
        private const val HINT_EXPLANATION_SHOW_LIMIT_LEVEL = 10
        private const val HINT_EXPLANATION_SHOW_MIN_LEVEL = 1
    }
}