package com.parabolagames.glassmaze.store.classicmaze

import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions.*
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle
import com.badlogic.gdx.scenes.scene2d.ui.Dialog
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.Align
import com.parabolagames.glassmaze.shared.Assets
import com.parabolagames.glassmaze.shared.Constants
import com.parabolagames.glassmaze.shared.SingleTextureActor
import com.parabolagames.glassmaze.shared.ui.DialogCloserFromKeyEvents
import com.parabolagames.glassmaze.shared.ui.DialogStyle
import com.parabolagames.glassmaze.shared.ui.GM_Container
import com.parabolagames.glassmaze.shared.ui.GM_Label
import kotlin.math.roundToInt

internal class CandyHandAndSpinyHandPurchaseResultDialog constructor(
        assets: Assets,
        title: String,
        private val content: String,
        private val spinyCount: Int,
        private val candyCount: Int,
        private val closeAction: () -> Unit) : Dialog(title, DialogStyle(assets)) {

    init {
        initialize(assets)
        isModal = false
        addListener(DialogCloserFromKeyEvents { result(false) })
    }

    override fun result(`object`: Any) {
        hide(null)
        closeAction()
    }

    override fun show(stage: Stage): Dialog {
        super.show(stage, null)
        setPosition(
                ((stage.width - width) / 2).roundToInt().toFloat(),
                ((stage.height - height) / 2).roundToInt().toFloat())
        return this
    }

    private fun initialize(assets: Assets) {
        titleLabel.setFontScale(4f)
        titleTable.getCell(titleLabel).padTop(-0.1f * Constants.DIALOG_SIZE_RATIO)

        Button().apply {
            val textureAtlas = assets.getTextureAtlas(Assets.BUTTONS_ACCEPT_DENY)
            style = ButtonStyle(
                    TextureRegionDrawable(textureAtlas.findRegion("accept_released")),
                    TextureRegionDrawable(textureAtlas.findRegion("accept_pressed")),
                    null)
        }.also {
            setObject(it, true)
            buttonTable.add(it)
        }

        GM_Label(content, assets, Assets.FONT_COSMIC_SANS_ORANGE, 2f)
                .apply {
                    align(Align.center)
                    wrap = false
                }
                .also {
                    contentTable.add(it)
                            .pad(0.15f * Constants.DIALOG_SIZE_RATIO)
                            .padTop(0.2f * Constants.DIALOG_SIZE_RATIO)
                            .padBottom(0.15f * Constants.DIALOG_SIZE_RATIO)
                            .colspan(3)
                }

        contentTable.row()

        if (candyCount != 0) {
            SingleTextureActor(assets, Assets.STORE_CANDY_GLASS_HAND, 500f)
                    .also {
                        contentTable.add(it).right()
                        it.addAction(createCandyAnimation())
                    }
            SingleTextureActor(assets, Assets.ARROW_RIGHT_GREEN, 200f)
                    .also {
                        contentTable.add(it)
                    }

            GM_Label(if (candyCount == Constants.INFINITY) Constants.INFINITY_SYMBOL else "$candyCount",
                    assets,
                    Assets.FONT_CURRENCY_MONTSERRAT,
                    2.6f).also {
                contentTable.add(GM_Container(it, createCandyAnimation())).left()
            }
            contentTable.row()
        }

        if (spinyCount != 0) {
            SingleTextureActor(assets, Assets.STORE_SPINY_HAND, 500f)
                    .also {
                        contentTable.add(it).right()
                        it.addAction(createSpinyAnimation())
                    }
            SingleTextureActor(assets, Assets.ARROW_RIGHT_RED, 200f)
                    .also {
                        contentTable.add(it)
                    }

            GM_Label(if (spinyCount == Constants.INFINITY) Constants.INFINITY_SYMBOL else "$spinyCount",
                    assets,
                    Assets.FONT_CURRENCY_MONTSERRAT,
                    2.6f)
                    .also {
                        contentTable.add(GM_Container(it, createSpinyAnimation())).left()
                    }
        }
    }

    private fun createCandyAnimation(): RepeatAction = forever(sequence(
            scaleBy(0.2f, 0.2f, 0.25f),
            scaleBy(-0.2f, -0.2f, 0.25f),
            scaleBy(0.2f, 0.2f, 0.25f),
            scaleBy(-0.2f, -0.2f, 0.25f),
            delay(2f)))


    private fun createSpinyAnimation(): RepeatAction = forever(sequence(
            delay(2f),
            scaleBy(0.2f, 0.2f, 0.25f),
            scaleBy(-0.2f, -0.2f, 0.25f),
            scaleBy(0.2f, 0.2f, 0.25f),
            scaleBy(-0.2f, -0.2f, 0.25f)))

}