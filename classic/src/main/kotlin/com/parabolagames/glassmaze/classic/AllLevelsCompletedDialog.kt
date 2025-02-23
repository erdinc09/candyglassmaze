package com.parabolagames.glassmaze.classic

import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle
import com.badlogic.gdx.scenes.scene2d.ui.Dialog
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.parabolagames.glassmaze.framework.ForGame
import com.parabolagames.glassmaze.framework.IDialogDoesNotPauseResume
import com.parabolagames.glassmaze.shared.Assets
import com.parabolagames.glassmaze.shared.Constants
import com.parabolagames.glassmaze.shared.ui.DialogCloserFromKeyEvents
import com.parabolagames.glassmaze.shared.ui.DialogStyle
import javax.inject.Inject
import kotlin.math.roundToInt

internal class
AllLevelsCompletedDialog
private constructor(private val assets: Assets, private val game: IGlassMazeClassic)
    : Dialog("All Levels Completed", DialogStyle(assets)), IDialogDoesNotPauseResume {

    override fun result(`object`: Any) {
        if (`object` as Boolean) {
            hide(null)
            game.setMenuScreenReVisible()
        }
    }

    init {
        initialize()
        addListener(DialogCloserFromKeyEvents { result(true) })
    }

    private fun initialize() {
        with(titleLabel) {
            setFontScale(3.2f)
            titleTable.getCell(this).padTop(-0.1f * Constants.DIALOG_SIZE_RATIO)
        }

        Button()
                .apply {
                    val textureAtlas = assets.getTextureAtlas(Assets.BUTTONS_ACCEPT_DENY)
                    style = ButtonStyle(
                            TextureRegionDrawable(textureAtlas.findRegion("accept_released")),
                            TextureRegionDrawable(textureAtlas.findRegion("accept_pressed")),
                            null)
                }.also {
                    setObject(it, true)
                    buttonTable.add(it)
                }

        Label("New levels will arrive soon!",
                LabelStyle(assets.getBitmapFont(Assets.FONT_COSMIC_SANS_ORANGE), null))
                .apply {
                    setFontScale(
                            if (Gdx.app.type == Application.ApplicationType.iOS) 2.2f else 2.6f)
                }.also {
                    contentTable
                            .add(it)
                            .pad(0.15f * Constants.DIALOG_SIZE_RATIO)
                            .padTop(0.6f * Constants.DIALOG_SIZE_RATIO)
                }
    }

    override fun show(stage: Stage): Dialog {
        super.show(stage, null)
        setPosition(
                ((stage.width - width) / 2).roundToInt().toFloat(),
                ((stage.height - height) / 2).roundToInt().toFloat())
        return this
    }

    @ForGame
    internal class AllLevelsCompletedDialogFactory
    @Inject constructor(private val assets: Assets, private val game: IGlassMazeClassic) {
        fun create(): AllLevelsCompletedDialog = AllLevelsCompletedDialog(assets, game)
    }
}