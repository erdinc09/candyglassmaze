package com.parabolagames.glassmaze.shared

import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle
import com.badlogic.gdx.scenes.scene2d.ui.Dialog
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.Align
import com.parabolagames.glassmaze.shared.ui.DialogCloserFromKeyEvents
import com.parabolagames.glassmaze.shared.ui.DialogStyle
import com.parabolagames.glassmaze.shared.ui.GM_Label
import kotlin.math.roundToInt

class InformationDialogsWithTitle @JvmOverloads constructor(
        assets: Assets,
        title: String,
        private val content: String,
        private val acceptAction: (() -> Unit)? = null) : Dialog(title, DialogStyle(assets)) {

    init {
        initialize(assets)
        isModal = false
        addListener(DialogCloserFromKeyEvents { result(false) })
    }

    override fun result(`object`: Any) {
        if (`object` as Boolean) {
            hide(null)
            acceptAction?.let { it() }
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
                            .padBottom(0.05f * Constants.DIALOG_SIZE_RATIO)
                }

    }
}