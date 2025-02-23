package com.parabolagames.glassmaze.classic

import com.badlogic.gdx.scenes.scene2d.Action
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions.*
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle
import com.badlogic.gdx.scenes.scene2d.ui.Dialog
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.parabolagames.glassmaze.shared.IAddsController
import com.parabolagames.glassmaze.shared.Assets
import com.parabolagames.glassmaze.shared.Constants
import com.parabolagames.glassmaze.shared.SingleTextureActor
import com.parabolagames.glassmaze.shared.ui.DialogCloserFromKeyEvents
import com.parabolagames.glassmaze.shared.ui.DialogStyle
import com.parabolagames.glassmaze.shared.ui.GM_Container
import com.parabolagames.glassmaze.shared.ui.GM_Label
import kotlin.math.roundToInt

internal class HintDialog(
        private val assets: Assets,
        private val candyGlassCountToManualBreak: Int,
        private val spinyGlassCountToManualBreak: Int,
        private val addsController: IAddsController) : Dialog("Hints", DialogStyle(assets)) {

    init {
        isModal = false
        initialize()
        addListener(DialogCloserFromKeyEvents { result(true) })
    }

    override fun result(`object`: Any) {
        if (`object` as Boolean) {
            hide(null)
        }
    }

    private fun initialize() {
        titleLabel.setFontScale(3.2f)
        titleTable.getCell(titleLabel).padTop(-0.1f * Constants.DIALOG_SIZE_RATIO)
        contentTable
                .add(GM_Label(
                        "To complete level\nat least you need:", assets, Assets.FONT_COSMIC_SANS_ORANGE, 2.6f))
                .pad(0.15f * Constants.DIALOG_SIZE_RATIO)
                .padTop(0.2f * Constants.DIALOG_SIZE_RATIO)
                .padBottom(0.4f * Constants.DIALOG_SIZE_RATIO)
                .top()
                .colspan(3)
                .center()
        contentTable.row()

        SingleTextureActor(assets, Assets.STORE_CANDY_GLASS_HAND, 500f).also {
            contentTable.add(it).right()
            it.addAction(createCandyAnimation())
        }
        SingleTextureActor(assets, Assets.ARROW_RIGHT_GREEN, 200f).also {
            contentTable.add(it)
        }

        GM_Label(candyGlassCountToManualBreak.toString(),
                assets,
                Assets.FONT_COSMIC_SANS_GREEN,
                2.6f).also {
            contentTable.add(GM_Container(it, createCandyAnimation())).left()
        }
        contentTable.row()

        SingleTextureActor(assets, Assets.STORE_SPINY_HAND, 500f).also {
            contentTable.add(it).right()
            it.addAction(createSpinyAnimation())
        }
        SingleTextureActor(assets, Assets.ARROW_RIGHT_RED, 200f).also {
            contentTable.add(it)
        }

        GM_Label(spinyGlassCountToManualBreak.toString(),
                assets,
                Assets.FONT_COSMIC_SANS_RED,
                2.6f).also {
            contentTable.add(GM_Container(it, createSpinyAnimation())).left()
        }

        Button().apply {
            val textureAtlas = assets.getTextureAtlas(Assets.BUTTONS_ACCEPT_DENY)
            style = ButtonStyle(
                    TextureRegionDrawable(textureAtlas.findRegion("accept_released")),
                    TextureRegionDrawable(textureAtlas.findRegion("accept_pressed")),
                    null)

            setObject(this, true)
        }.also {
            buttonTable.add(it).padTop(0.2f * Constants.DIALOG_SIZE_RATIO)
        }
    }

    private fun createCandyAnimation(): RepeatAction {
        return forever(
                sequence(
                        scaleBy(0.2f, 0.2f, 0.25f),
                        scaleBy(-0.2f, -0.2f, 0.25f),
                        scaleBy(0.2f, 0.2f, 0.25f),
                        scaleBy(-0.2f, -0.2f, 0.25f),
                        delay(2f)))
    }

    private fun createSpinyAnimation(): RepeatAction {
        return forever(
                sequence(
                        delay(2f),
                        scaleBy(0.2f, 0.2f, 0.25f),
                        scaleBy(-0.2f, -0.2f, 0.25f),
                        scaleBy(0.2f, 0.2f, 0.25f),
                        scaleBy(-0.2f, -0.2f, 0.25f)))
    }

    override fun show(stage: Stage): Dialog {
        addsController.showBanner(true)
        super.show(stage, null)
        setPosition(
                ((stage.width - width) / 2).roundToInt().toFloat(),
                ((stage.height - height) / 2).roundToInt().toFloat())
        return this
    }

    override fun hide(action: Action?) {
        super.hide(action)
        addsController.showBanner(false)
    }
}