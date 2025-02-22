package com.parabolagames.glassmaze.candymode

import com.badlogic.gdx.scenes.scene2d.Action
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions.*
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle
import com.badlogic.gdx.scenes.scene2d.ui.Container
import com.badlogic.gdx.scenes.scene2d.ui.Dialog
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.Align
import com.parabolagames.glassmaze.framework.IDialogDoesNotPauseResume
import com.parabolagames.glassmaze.framework.run2
import com.parabolagames.glassmaze.shared.Assets
import com.parabolagames.glassmaze.shared.Constants
import com.parabolagames.glassmaze.shared.SoundPlayer
import com.parabolagames.glassmaze.shared.ui.DialogStyle
import javax.inject.Inject
import kotlin.math.roundToInt

internal class CandyGainDialog @Inject constructor(
        soundPlayer: SoundPlayer,
        private val assets: Assets,
        private val game: IGlassMazeCandyMode,
        dataPersistenceManager: ICandyCountPersistenceManager,
        candyCounter: ICandyCounter)
    : Dialog("Game Over", DialogStyle(assets, true)),
        IDialogDoesNotPauseResume {

    private val soundPlayer: SoundPlayer = soundPlayer
    private val score: Long = candyCounter.candy.toLong()
    private val bestScore: Long = dataPersistenceManager.bestScore.toLong()
    private val totalScore: Long = dataPersistenceManager.totalScore.toLong()

    init {
        initialize()
    }

    override fun result(`object`: Any) {
        if (`object` as Boolean) {
            hide(null)
            game.setCandyModeScreenVisible()
        }
        if (`object` == false) {
            game.setMenuScreenReVisible()
        }
    }

    private fun initialize() {
        val leftRightPadding = 0.1f * Constants.DIALOG_SIZE_RATIO
        titleLabel.setFontScale(3.2f)
        titleTable.getCell(titleLabel).padTop(-0.1f * Constants.DIALOG_SIZE_RATIO)

        val textureAtlas = assets.getTextureAtlas(Assets.BUTTONS_ACCEPT_DENY)

        val acceptButton = Button().apply {
            style = ButtonStyle(
                    TextureRegionDrawable(textureAtlas.findRegion("accept_released")),
                    TextureRegionDrawable(textureAtlas.findRegion("accept_pressed")), null)
        }.also {
            buttonTable.add(it).padRight(0.08f * Constants.DIALOG_SIZE_RATIO)
            setObject(it, true)
        }
        val denyButton = Button().apply {
            style = ButtonStyle(
                    TextureRegionDrawable(textureAtlas.findRegion("deny_released")),
                    TextureRegionDrawable(textureAtlas.findRegion("deny_pressed")), null)

        }.also {
            buttonTable.add(it)
            setObject(it, false)
        }


        val lblScore = Label(String.format("Score: %d", score),
                LabelStyle(assets.getBitmapFont(Assets.FONT_COSMIC_SANS_GREEN), null)).apply { setFontScale(2.7f) }
        val containerScore = Container(lblScore).apply {
            isTransform = true
            setOrigin(lblScore.width * Constants.DIALOG_SIZE_RATIO, lblScore.height * Constants.DIALOG_SIZE_RATIO)
        }.also {
            contentTable
                    .add(it)
                    .align(Align.right)
                    .padTop(0.2f * Constants.DIALOG_SIZE_RATIO)
                    .padRight(leftRightPadding)
                    .padLeft(leftRightPadding)
        }
        contentTable.row()

        var containerBestScore: Container<Label>? = null
        if (score == bestScore) {
            var lblBestScore = Label("Best : $bestScore",
                    LabelStyle(assets.getBitmapFont(Assets.FONT_COSMIC_SANS_GREEN), null))
                    .apply { setFontScale(2.7f) }
            containerBestScore = Container(lblBestScore).apply {
                isTransform = true
                setOrigin(lblBestScore.width * Constants.DIALOG_SIZE_RATIO, lblBestScore.height * Constants.DIALOG_SIZE_RATIO)
            }.also {
                contentTable
                        .add(it)
                        .align(Align.right)
                        .padRight(leftRightPadding)
                        .padLeft(leftRightPadding)
            }
            contentTable.row()
        }

        val lblTotalScore = Label(String.format("Total : %d", totalScore),
                LabelStyle(assets.getBitmapFont(Assets.FONT_COSMIC_SANS_GREEN), null)).apply { setFontScale(2.7f) }
        var containerTotalScore: Container<Label>? = null
        containerTotalScore = Container(lblTotalScore)
                .apply {
                    isTransform = true
                    setOrigin(lblTotalScore.width * Constants.DIALOG_SIZE_RATIO, lblTotalScore.height * Constants.DIALOG_SIZE_RATIO)
                }.also {
                    contentTable
                            .add(it)
                            .align(Align.right)
                            .padRight(leftRightPadding)
                            .padLeft(leftRightPadding)
                }
        contentTable.row()

        val lblQuestion = Label("Play Again?",
                LabelStyle(assets.getBitmapFont(Assets.FONT_COSMIC_SANS_ORANGE), null)).apply {
            setFontScale(2.7f)
        }.also {
            contentTable
                    .add(it)
                    .pad(0.15f * Constants.DIALOG_SIZE_RATIO)
                    .padTop(0.3f * Constants.DIALOG_SIZE_RATIO)

        }
        var delayTime = 0f

        // first fade out
        containerScore.addAction(fadeOut(0f))
        containerBestScore?.addAction(fadeOut(0f))
        containerTotalScore.addAction(fadeOut(0f))
        lblQuestion.addAction(fadeOut(0f))
        acceptButton.addAction(fadeOut(0f))
        denyButton.addAction(fadeOut(0f))
        containerScore.addAction(createLabelAction(true))
        containerBestScore?.addAction(
                sequence(delay(0.5f.let { delayTime += it; delayTime }), createLabelAction(true)))
        containerTotalScore.addAction(
                sequence(delay(0.5f.let { delayTime += it; delayTime }), createLabelAction(true)))
        lblQuestion.addAction(sequence(delay(0.5f.let { delayTime += it; delayTime }), createLabelAction()))
        acceptButton.addAction(sequence(delay(delayTime), createLabelAction()))
        denyButton.addAction(sequence(delay(delayTime), createLabelAction()))
    }

    private fun createLabelAction(playSound: Boolean = false): Action {
        return if (playSound) {
            parallel(
                    fadeIn(0.5f),
                    run2 { soundPlayer.playRandomBreak() },
                    sequence(
                            scaleBy(0.1f, 0.2f, 0.25f),
                            scaleBy(-0.1f, -0.2f, 0.25f)))
        } else {
            parallel(
                    fadeIn(0.5f),
                    sequence(
                            scaleBy(0.1f, 0.2f, 0.25f),
                            scaleBy(-0.1f, -0.2f, 0.25f)))
        }
    }

    override fun show(stage: Stage): Dialog {
        super.show(stage, null)
        setPosition(((stage.width - width) / 2).roundToInt().toFloat(), ((stage.height - height) / 2).roundToInt().toFloat())
        return this
    }
}