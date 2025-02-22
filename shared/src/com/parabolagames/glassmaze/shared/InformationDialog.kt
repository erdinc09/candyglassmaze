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

class InformationDialog @JvmOverloads constructor(
        assets: Assets,
        private val content: String,
        private val acceptAction: Runnable) : Dialog("", DialogStyle(assets)) {

    init {
        initialize(assets)
        isModal = false
        addListener(DialogCloserFromKeyEvents { result(false) })
    }

    override fun result(`object`: Any) {
        if (`object` as Boolean) {
            hide(null)
            acceptAction.run()
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
        val textureAtlas = assets.getTextureAtlas(Assets.BUTTONS_ACCEPT_DENY)

        Button().apply {
            style = ButtonStyle(
                    TextureRegionDrawable(textureAtlas.findRegion("accept_released")),
                    TextureRegionDrawable(textureAtlas.findRegion("accept_pressed")),
                    null)
        }.also {
            setObject(it, true)
            buttonTable.add(it)
        }

        GM_Label(content, assets, Assets.FONT_COSMIC_SANS_ORANGE, 2.7f)
                .apply {
                    align(Align.center)
                    wrap = false
                }
                .also {
                    contentTable.add(it).pad(0.15f * Constants.DIALOG_SIZE_RATIO)
                }
    }
}