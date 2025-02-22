package com.parabolagames.glassmaze.classic

import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions.*
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle
import com.badlogic.gdx.scenes.scene2d.ui.Dialog
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.Pools
import com.parabolagames.glassmaze.shared.Assets
import com.parabolagames.glassmaze.shared.Constants
import com.parabolagames.glassmaze.shared.candy2.CandyActor
import com.parabolagames.glassmaze.shared.ui.DialogCloserFromKeyEvents
import com.parabolagames.glassmaze.shared.ui.DialogStyle
import com.parabolagames.glassmaze.shared.ui.GM_Label
import kotlin.math.roundToInt

internal class GoToStoreApprovalDialog constructor(
        assets: Assets,
        private val acceptAction: ()->Unit)
    : Dialog("No Hands Left!", DialogStyle(assets)) {

    private val candyArray: Array<CandyActor> = Pools.get(Array::class.java).obtain() as Array<CandyActor>

    init {
        initialize(assets)
        isModal = false
        clip = false
        isTransform = true
        addListener(DialogCloserFromKeyEvents { result(false) })
    }

    override fun result(`object`: Any) {
        candyArray.forEach { it.remove() }
        Pools.get(Array::class.java).free(candyArray)

        if (`object` as Boolean) {
            hide(null)
            acceptAction()
        }
        if (`object` == false) {
            hide(null)
        }
    }

    override fun show(stage: Stage): Dialog {
        super.show(stage, null)
        setPosition(
                ((stage.width - width) / 2).roundToInt().toFloat(),
                ((stage.height - height) / 2).roundToInt().toFloat())
        return this
    }

    private fun initialize(assets: Assets) {
        titleLabel.setFontScale(3f)
        titleTable.getCell(titleLabel).padTop(-0.1f * Constants.DIALOG_SIZE_RATIO)
        val textureAtlas = assets.getTextureAtlas(Assets.BUTTONS_ACCEPT_DENY)

        Button().apply {
            style = ButtonStyle(
                    TextureRegionDrawable(textureAtlas.findRegion("accept_released")),
                    TextureRegionDrawable(textureAtlas.findRegion("accept_pressed")),
                    null)
            isTransform = true
            addAction(
                    forever(sequence(
                            delay(1f),
                            scaleBy(0.2f, 0.2f, 0.1f),
                            scaleBy(-0.2f, -0.2f, 0.1f),
                            scaleBy(0.2f, 0.2f, 0.2f),
                            scaleBy(-0.2f, -0.2f, 0.2f),
                    )))

        }.also {
            setObject(it, true)
            buttonTable.add(it).padRight(0.08f * Constants.DIALOG_SIZE_RATIO)
        }

        Button().apply {
            style = ButtonStyle(
                    TextureRegionDrawable(textureAtlas.findRegion("deny_released")),
                    TextureRegionDrawable(textureAtlas.findRegion("deny_pressed")),
                    null)
        }.also {
            setObject(it, false)
            buttonTable.add(it)
        }

        GM_Label("Go to Store?", assets, Assets.FONT_COSMIC_SANS_ORANGE, 2.5f)
                .also {
                    contentTable.add(it).pad(0.25f * Constants.DIALOG_SIZE_RATIO)
                }

        CandyActor.getCandy(assets, Assets.CANDY2_ATLAS,
                0.25f * Constants.DIALOG_SIZE_RATIO,
                true) { 0.025f }
                .apply { isAnimationPaused = true }
                .also {
                    addActor(it)
                    candyArray.add(it)
                }
        CandyActor.getCandy(assets, Assets.CANDY1_ATLAS,
                0.25f * Constants.DIALOG_SIZE_RATIO,
                true) { 0.025f }
                .apply {
                    isAnimationPaused = true
                    setPosition(-0.05f * Constants.DIALOG_SIZE_RATIO, 0.15f * Constants.DIALOG_SIZE_RATIO)
                }
                .also {
                    addActor(it)
                    candyArray.add(it)
                }
        CandyActor.getCandy(assets, Assets.CANDY10_ATLAS,
                1f * Constants.DIALOG_SIZE_RATIO,
                true) { 0.025f }
                .apply {
                    isAnimationPaused = true
                    setPosition(1f * Constants.DIALOG_SIZE_RATIO, 0.1f * Constants.DIALOG_SIZE_RATIO)
                }
                .also {
                    addActor(it)
                    candyArray.add(it)
                }
        CandyActor.getCandy(assets, Assets.CANDY9_ATLAS,
                1f * Constants.DIALOG_SIZE_RATIO,
                true) { 0.025f }
                .apply {
                    isAnimationPaused = true
                    setPosition(1f * Constants.DIALOG_SIZE_RATIO, 0.12f * Constants.DIALOG_SIZE_RATIO)
                    rotation = -20f
                }
                .also {
                    addActor(it)
                    candyArray.add(it)
                }
        CandyActor.getCandy(assets, Assets.CANDY13_ATLAS,
                0.25f * Constants.DIALOG_SIZE_RATIO,
                true) { 0.025f }
                .apply {
                    isAnimationPaused = true
                    setPosition(1.1f * Constants.DIALOG_SIZE_RATIO, 0.08f * Constants.DIALOG_SIZE_RATIO)
                }
                .also {
                    addActor(it)
                    candyArray.add(it)
                }
        CandyActor.getCandy(assets, Assets.CANDY14_ATLAS,
                0.25f * Constants.DIALOG_SIZE_RATIO,
                true) { 0.025f }
                .apply {
                    isAnimationPaused = true
                    setPosition(1.2f * Constants.DIALOG_SIZE_RATIO, 0.13f * Constants.DIALOG_SIZE_RATIO)
                }
                .also {
                    addActor(it)
                    candyArray.add(it)
                }
    }
}