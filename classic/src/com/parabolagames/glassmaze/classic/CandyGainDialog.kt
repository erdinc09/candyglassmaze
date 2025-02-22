package com.parabolagames.glassmaze.classic

import com.badlogic.gdx.scenes.scene2d.Action
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions.*
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.Align
import com.parabolagames.glassmaze.framework.IDialogDoesNotPauseResume
import com.parabolagames.glassmaze.framework.run2
import com.parabolagames.glassmaze.shared.Assets
import com.parabolagames.glassmaze.shared.Constants
import com.parabolagames.glassmaze.shared.IAddsController
import com.parabolagames.glassmaze.shared.SoundPlayer
import com.parabolagames.glassmaze.shared.candy2.CandyActor
import com.parabolagames.glassmaze.shared.ui.DialogCloserFromKeyEvents
import com.parabolagames.glassmaze.shared.ui.DialogStyle
import javax.inject.Inject
import kotlin.math.roundToInt

internal class CandyGainDialog @Inject constructor(
        private val soundPlayer: SoundPlayer,
        private val assets: Assets,
        private val game: IGlassMazeClassic,
        dataPersistenceManager: IClassicMazePersistenceManager,
        candyCounter: ICandyCounter,
        private val subMode: ClassicModeSubMode,
        private val addsController: IAddsController,
        private val bonusCandyHandActorFactory: BonusCandyHandActor.BonusCandyHandActorFactory,
        private val bonusSpinyHandActorFactory: BonusSpinyHandActor.BonusSpinyHandActorFactory)
    : Dialog("Level Completed !", DialogStyle(assets, true)),
        IDialogDoesNotPauseResume {

    private val totalScore: Int = dataPersistenceManager.totalScore
    private val candyCounter: ICandyCounter = candyCounter

    private val pooledList = mutableListOf<Actor>()

    init {
        initialize()
        addListener(DialogCloserFromKeyEvents { result(false) })
    }

    override fun result(`object`: Any) {
        if (`object` as Boolean) {
            hide(null)
            when (subMode) {
                ClassicModeSubMode.RANDOM -> game.setRandomModeScreenVisible()
                ClassicModeSubMode.CLASSIC -> game.setClassicModeScreenVisible()
                else -> throw   IllegalStateException()
            }
            dialogShownWithoutInterstitialAddsShown++
        }
        if (`object` == false) {
            game.setMenuScreenReVisible()
            if (dialogShownWithoutInterstitialAddsShown >= 10) {
                addsController.showInterstitial()
                dialogShownWithoutInterstitialAddsShown = 0
            } else {
                dialogShownWithoutInterstitialAddsShown++
            }
        }
        pooledList.forEach { it.remove() }
        addsController.showBanner(false)
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
            style = ButtonStyle(TextureRegionDrawable(textureAtlas.findRegion("deny_released")),
                    TextureRegionDrawable(textureAtlas.findRegion("deny_pressed")), null)
        }.also {
            buttonTable.add(it)
            setObject(it, false)
        }


        var containerGift: Container<Table>? = null
        if (candyCounter.isCandyGiftForAllGlassCrackedGained) {

            if (subMode == ClassicModeSubMode.CLASSIC) {

                val tableBonus = Table()

                val lblGift = Label("Bonus:",
                        LabelStyle(assets.getBitmapFont(Assets.FONT_COSMIC_SANS_GREEN), null))
                        .apply { setFontScale(2.7f) }

                containerGift = Container(tableBonus).apply {
                    isTransform = true
                    setOrigin(
                            0f,
                            lblGift.height * Constants.DIALOG_SIZE_RATIO)
                }.also {
                    contentTable
                            .add(it)
                            .align(Align.left)
                            .padTop(0.2f * Constants.DIALOG_SIZE_RATIO)
                    pooledList.add(it)
                }
                tableBonus.add(lblGift)
                tableBonus.row()

                bonusCandyHandActorFactory.getBonusCandyHandActor()
                        .apply {
                            setSize(width * Constants.DIALOG_SIZE_RATIO, height * Constants.DIALOG_SIZE_RATIO)
                        }
                        .also {
                            contentTable
                            tableBonus.add(it)
                                    .align(Align.center)
                                    .padRight(leftRightPadding)
                                    .padLeft(leftRightPadding)
                            pooledList.add(it)
                        }

                bonusSpinyHandActorFactory.getBonusSpinyHandActor()
                        .apply {
                            setSize(width * Constants.DIALOG_SIZE_RATIO, height * Constants.DIALOG_SIZE_RATIO)
                        }
                        .also {
                            contentTable
                            tableBonus.add(it)
                                    .align(Align.center)
                                    .padRight(leftRightPadding)
                                    .padLeft(leftRightPadding)
                            pooledList.add(it)
                        }

                val lblBonusScore = Label("5",
                        LabelStyle(assets.getBitmapFont(Assets.FONT_COSMIC_SANS_GREEN), null))
                        .apply {
                            setFontScale(2.7f)
                        }
                val candy = CandyActor.getCandy(assets, Assets.CANDY14_ATLAS, 0.2f * Constants.DIALOG_SIZE_RATIO, true) { 0.01f }
                pooledList.add(candy)
                val bonusCandyGroup = Table()
                bonusCandyGroup.add(lblBonusScore)
                bonusCandyGroup.add(candy).padLeft(0.05f * Constants.DIALOG_SIZE_RATIO)

                with(bonusCandyGroup) {
                    tableBonus.add(this)
                            .align(Align.center)
                            .padRight(leftRightPadding)
                            .padLeft(leftRightPadding)
                }
                contentTable.row()
            } else {

                val lblBonusScore = Label("Bonus: 5",
                        LabelStyle(assets.getBitmapFont(Assets.FONT_COSMIC_SANS_GREEN), null))
                        .apply {
                            setFontScale(2.7f)
                        }
                val candy = CandyActor.getCandy(assets, Assets.CANDY14_ATLAS, 0.2f * Constants.DIALOG_SIZE_RATIO, true) { 0.01f }
                pooledList.add(candy)
                val bonusCandyGroup = Table()
                bonusCandyGroup.add(lblBonusScore)
                bonusCandyGroup.add(candy).padLeft(0.05f * Constants.DIALOG_SIZE_RATIO)

                containerGift = Container(bonusCandyGroup)
                        .apply {
                            isTransform = true
                            setOrigin(
                                    0f,
                                    lblBonusScore.height * com.parabolagames.glassmaze.shared.Constants.DIALOG_SIZE_RATIO)
                        }.also {
                            contentTable
                                    .add(it)
                                    .align(Align.left)
                                    .padRight(leftRightPadding)
                                    .padLeft(leftRightPadding)
                                    .padTop(0.2f * Constants.DIALOG_SIZE_RATIO)
                        }
                contentTable.row()
            }
        }

        val lblTotalScore = Label(String.format("Total : %d", totalScore),
                LabelStyle(assets.getBitmapFont(Assets.FONT_COSMIC_SANS_GREEN), null))
                .apply {
                    setFontScale(2.7f)
                }
        val candy = CandyActor.getCandy(assets, Assets.CANDY14_ATLAS, 0.2f * Constants.DIALOG_SIZE_RATIO, true) { 0.01f }
        pooledList.add(candy)
        val totalGroup = Table()
        totalGroup.add(lblTotalScore)
        totalGroup.add(candy).padLeft(0.05f * Constants.DIALOG_SIZE_RATIO)

        var containerTotalScore = Container(totalGroup)
                .apply {
                    isTransform = true
                    setOrigin(
                            0f,
                            lblTotalScore.height * Constants.DIALOG_SIZE_RATIO)
                }.also {
                    contentTable
                            .add(it)
                            .padTop(0.2f * Constants.DIALOG_SIZE_RATIO)
                            .align(Align.left)
                            .padRight(leftRightPadding)
                            .padLeft(leftRightPadding)
                }


        contentTable.row()

        val lblQuestion = Label(
                "Play Next Level ?",
                LabelStyle(assets.getBitmapFont(Assets.FONT_COSMIC_SANS_ORANGE), null)).apply { setFontScale(2.7f) }
                .also {
                    contentTable
                            .add(it)
                            .pad(0.20f * Constants.DIALOG_SIZE_RATIO)
                            .padTop(0.3f * Constants.DIALOG_SIZE_RATIO)
                }

        var delayTime = 0.0f

        // first fade out
        containerTotalScore.addAction(fadeOut(0f))
        lblQuestion.addAction(fadeOut(0f))
        acceptButton.addAction(fadeOut(0f))
        denyButton.addAction(fadeOut(0f))

        if (candyCounter.isCandyGiftForAllGlassCrackedGained) {
            // first fade out
            containerGift!!.addAction(fadeOut(0f))
            containerGift.addAction(
                    sequence(delay(0.5f.let { delayTime += it; delayTime }), createLabelAction(0.5f, true)))
        }
        containerTotalScore.addAction(
                sequence(delay(0.5f.let { delayTime += it; delayTime }), createLabelAction(0.5f, true)))
        lblQuestion.addAction(
                sequence(delay(0.5f.let { delayTime += it; delayTime }), createLabelAction(0.5f)))

        acceptButton.apply {
            isDisabled = true
            addAction(sequence(delay(delayTime), parallel(createLabelAction(fadeInTime = 0.5f), run2 { isDisabled = false })))
        }
        denyButton.apply {
            isDisabled = true
            addAction(sequence(delay(delayTime), parallel(createLabelAction(fadeInTime = 0.5f), run2 { isDisabled = false })))
        }
    }

    private fun createLabelAction(fadeInTime: Float, playSound: Boolean = false): Action {
        return if (playSound) {
            parallel(
                    parallel(
                            fadeIn(fadeInTime),
                            run2 { soundPlayer.playRandomBreak() }),
                    sequence(scaleBy(0.1f, 0.2f, 0.25f),
                            scaleBy(-0.1f, -0.2f, 0.25f)))
        } else {
            parallel(
                    fadeIn(1f),
                    sequence(scaleBy(0.1f, 0.2f, 0.25f),
                            scaleBy(-0.1f, -0.2f, 0.25f)))
        }
    }

    override fun show(stage: Stage): Dialog {
        dialogShownCount++
        if (dialogShownCount % 3 == 0) {
            addsController.showBanner(true)
        }
        super.show(stage, null)
        setPosition(((stage.width - width) / 2).roundToInt().toFloat(), ((stage.height - height) / 2).roundToInt().toFloat())
        return this
    }

    companion object {
        private var dialogShownCount = 0
        private var dialogShownWithoutInterstitialAddsShown = 0
    }
}