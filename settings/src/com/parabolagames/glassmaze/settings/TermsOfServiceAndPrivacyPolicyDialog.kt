package com.parabolagames.glassmaze.settings

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Dialog
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.utils.Align
import com.parabolagames.glassmaze.shared.Assets
import com.parabolagames.glassmaze.shared.Constants
import com.parabolagames.glassmaze.shared.SingleTextureActor
import com.parabolagames.glassmaze.shared.Utils
import com.parabolagames.glassmaze.shared.ui.DialogCloserFromKeyEvents
import com.parabolagames.glassmaze.shared.ui.DialogStyle
import com.parabolagames.glassmaze.shared.ui.GM_HyperTextLabel
import com.parabolagames.glassmaze.shared.ui.GM_Label
import kotlin.math.roundToInt

internal class TermsOfServiceAndPrivacyPolicyDialog(
        private val assets: Assets,
        termsChanged: Boolean,
        private val acceptAction: () -> Unit) : Dialog(if (termsChanged) "Terms Changed" else "First Play", DialogStyle(assets)) {
    init {
        isModal = false
        clip = false
        isTransform = true
        initialize()
        addListener(DialogCloserFromKeyEvents { result(true) })
    }

    private fun initialize() {
        titleLabel.setFontScale(3f)
        titleTable.getCell(titleLabel).padTop(-0.1f * Constants.DIALOG_SIZE_RATIO)
        addContent()
        addStartButton()
    }

    private fun addContent() {

        SingleTextureActor(assets, Assets.CANDY_GLASS_HIGH_TEXT, 0.6f * Constants.DIALOG_SIZE_RATIO)
                .apply {
                    contentTable.add(this)
                            .pad(0.05f * Constants.DIALOG_SIZE_RATIO)
                            .padTop(0.25f * Constants.DIALOG_SIZE_RATIO)
                            .padBottom(0.15f * Constants.DIALOG_SIZE_RATIO)
                            .colspan(3)
                }
        contentTable.row()
        GM_HyperTextLabel("by ParabolaGames",
                assets,
                Assets.FONT_COMIC_SANS_MS,
                2f, Color.BROWN,
                "http://www.parabolagames.com")
                .apply {
                    contentTable.add(this)
                            .pad(0.05f * Constants.DIALOG_SIZE_RATIO)
                            .colspan(3)
                }
        contentTable.row()
        GM_Label(
                """By clicking 'Start' you indicate that you have read and agree to:
                    """.trimMargin(), assets, Assets.FONT_COMIC_SANS_MS, 1.1f)
                .apply {
                    setAlignment(Align.left)
                    wrap = true
                    contentTable.add(this)
                            .pad(0.05f * Constants.DIALOG_SIZE_RATIO).fillX().expandX()

                            .padBottom(0.1f * Constants.DIALOG_SIZE_RATIO)
                            .colspan(3)

                }
        contentTable.row()
        GM_HyperTextLabel("terms of service",
                assets,
                Assets.FONT_COMIC_SANS_MS,
                1.1f, Color.BROWN,
                "http://www.parabolagames.com/terms-of-service/")
                .apply {
                    contentTable.add(this).colspan(3)
                }
        contentTable.row()
        GM_Label("and", assets, Assets.FONT_COMIC_SANS_MS, 1.1f)
                .apply {
                    contentTable.add(this).colspan(3)
                }
        contentTable.row()
        GM_HyperTextLabel("privacy policy",
                assets,
                Assets.FONT_COMIC_SANS_MS,
                1.1f, Color.BROWN,
                "http://www.parabolagames.com/privacy-policy/")
                .apply {
                    contentTable.add(this).colspan(3)
                }
    }

    override fun result(obj: Any) {
        if (obj as Boolean) {
            hide(null)
            acceptAction()
        }
    }

    override fun show(stage: Stage): Dialog {
        super.show(stage, null)
        setPosition(
                ((stage.width - width) / 2).roundToInt().toFloat(),
                ((stage.height - height) / 2).roundToInt().toFloat())
        return this
    }

    private fun addStartButton() {
        TextButton("Start", Utils.textButtonStyle(assets))
                .apply {
                    label.setFontScale(2.5f)
                }.also {
                    buttonTable.add(it)
                            .size(0.5f * Constants.DIALOG_SIZE_RATIO, 0.25f * Constants.DIALOG_SIZE_RATIO)
                            .pad(0.02f * Constants.DIALOG_SIZE_RATIO).padTop(0.2f * Constants.DIALOG_SIZE_RATIO)
                    setObject(it, true)
                }
    }
}