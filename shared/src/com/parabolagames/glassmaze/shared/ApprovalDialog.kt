package com.parabolagames.glassmaze.shared

import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions.*
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle
import com.badlogic.gdx.scenes.scene2d.ui.Dialog
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.parabolagames.glassmaze.framework.run2
import com.parabolagames.glassmaze.shared.ui.DialogCloserFromKeyEvents
import com.parabolagames.glassmaze.shared.ui.DialogStyle
import com.parabolagames.glassmaze.shared.ui.GM_Label
import kotlin.math.roundToInt

class ApprovalDialog @JvmOverloads constructor(
        assets: Assets,
        private val title: String,
        private val acceptAction: (() -> Unit)?,
        private val denyAction: (() -> Unit)?,
        private val addsController: IAddsController?,
        private var lateButtons: Boolean = false) : Dialog("", DialogStyle(assets)) {

    init {
        isModal = false
        initialize(assets)
        addListener(DialogCloserFromKeyEvents { result(false) })
    }

    override fun result(`object`: Any) {
        if (`object` as Boolean) {
            hide(null)
            acceptAction?.let { it() }
        }
        if (`object` == false) {
            hide(null)
            denyAction?.let { it() }
        }
        addsController?.showBanner(false)
    }

    override fun show(stage: Stage): Dialog {
        addsController?.showBanner(true)
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
            addButtonAction()
        }.also {
            setObject(it, true)
            buttonTable.add(it).padRight(0.08f * Constants.DIALOG_SIZE_RATIO)
        }

        Button().apply {
            style = ButtonStyle(
                    TextureRegionDrawable(textureAtlas.findRegion("deny_released")),
                    TextureRegionDrawable(textureAtlas.findRegion("deny_pressed")),
                    null)
            addButtonAction()
        }.also {
            setObject(it, false)
            buttonTable.add(it)
        }

        GM_Label(title, assets, Assets.FONT_COSMIC_SANS_ORANGE, 2.7f).also {
            contentTable.add(it).pad(0.15f * Constants.DIALOG_SIZE_RATIO)
        }
    }

    private fun Button.addButtonAction() {
        if (lateButtons) {
            isDisabled = true
            addAction(sequence(fadeOut(0f), delay(0.65f), fadeIn(0.5f), run2 { isDisabled = false }))
        }
    }
}