package com.parabolagames.glassmaze.classic

import com.badlogic.gdx.Application.ApplicationType
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.actions.Actions.*
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Timer
import com.google.common.base.Preconditions.checkState
import com.google.common.eventbus.EventBus
import com.google.common.eventbus.Subscribe
import com.parabolagames.glassmaze.framework.CurrentScreen
import com.parabolagames.glassmaze.framework.ForGame
import com.parabolagames.glassmaze.framework.labelStyle
import com.parabolagames.glassmaze.framework.proguard.Keep
import com.parabolagames.glassmaze.framework.run2
import com.parabolagames.glassmaze.shared.Assets
import com.parabolagames.glassmaze.shared.Constants
import com.parabolagames.glassmaze.shared.GameScreen
import com.parabolagames.glassmaze.shared.IPlatformUiFix
import com.parabolagames.glassmaze.shared.ui.GM_Container
import javax.inject.Inject
import javax.inject.Named

@ForGame
internal class ClassicModeScreen @Inject constructor(
        @Named(ClassicModeModule2.CLASSIC_MAIN_STAGE_0) mainStage0: Stage,
        @Named(ClassicModeModule2.CLASSIC_MAIN_STAGE_1) mainStage1: Stage,
        @Named(ClassicModeModule2.CLASSIC_MAIN_STAGE__1) mainStage_1: Stage,
        @Named(ClassicModeModule2.CLASSIC_UI_STAGE) uiStage: Stage,
        @Named(ClassicModeModule2.CLASSIC_UI_STAGE_PLAY_PAUSE) uiStagePlayPause: Stage,
        @Named(ClassicModeModule2.CLASSIC_DIALOG_UI_STAGE) dialogUiStage: Stage,
        @Named(ClassicModeModule2.CLASSIC_DIALOG_UI_STAGE_FOR_INFORMATION) dialogUiStageForInformation: Stage,
        @Named(ClassicModeModule2.CLASSIC_EXPLANATION_CANDY_STAGE_1) expCandyStage: Stage,
        @Named(ClassicModeModule2.CLASSIC_WORLD) world: World,
        @param:Named(ClassicModeModule2.CLASSIC_RANDOM_PLAY_LEVEL_LIMIT) private val levelLimitForRandomLevel: Int,
        private val backGroundActor: GameBackGroundActor,
        assets: Assets,
        private val classicModeGameController: ClassicModeGameController,
        private val candyCountActor: CandyCountActor,
        private val levelNumberActor: LevelNumberActor,
        private val hintActor: HintActor,
        private val handActorSpinyGlass: HandActorSpinyGlass,
        private val handActorCandyGlass: HandActorCandyGlass,
        private val platformFix: IPlatformUiFix,
        private val subMode: ClassicModeSubMode,
        private val classicModeScreenDialogController: ClassicModeScreenDialogController,
        private val classicModeScreenInputController: ClassicModeScreenInputController,
        private val level: Level?,
        private val lastLevelNumberProvider: ILastLevelNumberProvider,
        private val eventBus: EventBus) : GameScreen(
        mainStage_1,
        mainStage0,
        mainStage1,
        uiStage,
        uiStagePlayPause,
        dialogUiStage,
        dialogUiStageForInformation,
        expCandyStage,
        world,
        assets),
        IClassicModeScreenGameFinishedListener,
        IClassicModeInputController,
        ITouchCoordinatesInGameAreaController {

    private val uiTable: Table = Table()
    private var gameInputEnabled = false
    private var gameFinished = false
    private var gameStarted = false
    private var backButton: Button? = null
    private lateinit var replayButton: Button

    override val currentScreen: CurrentScreen = CurrentScreen.CLASSIC_MAZE

    override fun initialize() {
        eventBus.register(this)
        Gdx.app.debug(TAG, "initialize()")
        addBackground()
        if (subMode === ClassicModeSubMode.CLASSIC) {
            if (level != null) {
                classicModeScreenDialogController.showExplanationDialog(::initializeClassicMode)
            } else {
                allLevelsCompleted()
            }
        } else {
            checkState(subMode === ClassicModeSubMode.RANDOM)
            if (lastLevelNumberProvider.levelNumber <= levelLimitForRandomLevel) {
                classicModeScreenDialogController.showRandomModeNotReadyDialog()
            } else {
                initializeClassicMode()
            }
        }
        hintActor.touchListener = classicModeScreenDialogController::showHintDialog
    }

    @Subscribe
    @Keep
    private fun eedubl(event: EventEnableDisableUIButtons) {

        if (event.isDisabled) {
            replayButton.remove()
            backButton?.remove()
        } else {
            uiStagePlayPause.addActor(replayButton)
            backButton?.let {
                uiStagePlayPause.addActor(it)
            }
        }
    }

    private fun addBackground() {
        mainStage_1.addActor(backGroundActor)
    }

    override fun dispose() {
        eventBus.unregister(this)
        super.dispose()
        classicModeGameController.dispose()
        classicModeScreenInputController.dispose()
    }

    override fun update(dt: Float) {
        super.update(dt)
        if (gameStarted) {
            classicModeGameController.update(dt)
        }
    }

    override fun pause() {
        super.pause()
        classicModeGameController.saveGame()
    }

    private fun initializeClassicMode() {
        checkState(level != null)
        val gameStartDelay = 0.5f
        initUI(gameStartDelay)
        Timer.schedule(
                object : Timer.Task() {
                    override fun run() {
                        gameStarted = true
                        classicModeScreenInputController.gameStarted = true
                    }
                },
                gameStartDelay)
        classicModeGameController.initialize()
    }

    private fun initUI(gameStartDelay: Float) {
        val topPad = 1f
        var iosPad = 0f
        var iosCandyPad = 0f
        var iosLevelPad = 0f
        createAndAddReplayButton()
        when {
            platformFix.isUiAlignmentFromTopIsNecessary -> {
                createAndAddBackButton()
                backButton!!.y = backButton!!.y + topPad
                backButton!!.addAction(
                        sequence(
                                delay(gameStartDelay), moveBy(0f, -topPad, gameStartDelay)))
                replayButton.setPosition(
                        Constants.WORLD_WIDTH - replayButton.width - 0.03f,
                        Constants.WORLD_HEIGHT - replayButton.height - 0.02f)
                replayButton.y = replayButton.y + topPad
                replayButton.addAction(
                        sequence(
                                delay(gameStartDelay), moveBy(0f, -topPad, gameStartDelay)))
                iosPad = 0.3f
                iosCandyPad = 0.4f
                iosLevelPad = 0.4f
            }
            Gdx.app.type == ApplicationType.iOS -> {
                createAndAddBackButton()
                backButton!!.setPosition(0.02f, -topPad + 0.02f)
                backButton!!.addAction(
                        sequence(
                                delay(gameStartDelay), moveBy(0f, topPad, gameStartDelay)))
                replayButton.y = replayButton.y - topPad
                replayButton.addAction(
                        sequence(
                                delay(gameStartDelay), moveBy(0f, topPad, gameStartDelay)))
            }
            else -> {
                replayButton.y = replayButton.y - topPad
                replayButton.addAction(sequence(delay(gameStartDelay), moveBy(0f, topPad, gameStartDelay)))
            }
        }
        with(uiTable) {
            setFillParent(true)
            uiTable.setRound(false)
            add(candyCountActor).left().top().padLeft(0.01f).padTop(0.0f) /*.expandY()*/
            add(levelNumberActor)
                    .padLeft(candyCountActor.candyCountWidth * 2f)
                    .center()
                    .top()
                    .padTop(0.0f)
                    .expandX()
            add(handActorCandyGlass).center().padRight(0.40f).top()
            add(handActorSpinyGlass).top().right().padRight(0.50f) /*.expandY()*/
            row()
            add(hintActor).left().top().padLeft(0.02f).expandY()
            getCell(candyCountActor).padTop(-topPad)
            getCell(levelNumberActor).padTop(-topPad)
            getCell(handActorCandyGlass).padTop(-topPad)
            getCell(handActorSpinyGlass).padTop(-topPad)
            getCell(hintActor).padTop(-topPad)
        }
        uiStage.addActor(uiTable)
        candyCountActor.addAction(
                sequence(
                        delay(gameStartDelay),
                        moveBy(0f, -topPad - iosCandyPad, gameStartDelay)))
        handActorCandyGlass.addAction(
                sequence(
                        delay(gameStartDelay),
                        moveBy(0f, -topPad - 0.25f - iosPad, gameStartDelay)))
        handActorSpinyGlass.addAction(
                sequence(
                        delay(gameStartDelay),
                        moveBy(0f, -topPad - 0.25f - iosPad, gameStartDelay)))
        levelNumberActor.addAction(
                sequence(
                        delay(gameStartDelay),
                        moveBy(0f, -topPad - 0.04f - iosLevelPad, gameStartDelay)))
        hintActor.addAction(
                sequence(
                        delay(gameStartDelay),
                        moveBy(0f, -topPad - 0.24f - iosCandyPad, gameStartDelay)))
    }

    private fun createAndAddBackButton() {
        backButton = Button().apply {
            val textureAtlas = assets.getTextureAtlas(Assets.BUTTONS_BACK)
            setColor(1f, 1f, 1f, 0.8f)
            style = ButtonStyle(
                    TextureRegionDrawable(textureAtlas.findRegion("back_released")),
                    TextureRegionDrawable(textureAtlas.findRegion("back_pressed")),
                    null)
            setSize(0.3f, 0.3f)
            setPosition(0.03f, Constants.WORLD_HEIGHT - 0.02f - height)
            addListener(
                    object : ChangeListener() {
                        override fun changed(event: ChangeEvent, actor: Actor) {
                            if (classicModeScreenDialogController.isDialogOpen || gameFinished || !gameStarted) {
                                return
                            }
                            classicModeScreenDialogController.showQuitModeDialog()
                        }
                    })
        }.also {
            uiStagePlayPause.addActor(it)
        }
    }

    private fun createAndAddReplayButton() {
        replayButton = Button().apply {
            val textureAtlas = assets.getTextureAtlas(Assets.BUTTONS_REPLAY)
            setColor(1f, 1f, 1f, 0.8f)
            style = ButtonStyle(
                    TextureRegionDrawable(textureAtlas.findRegion("replay_released")),
                    TextureRegionDrawable(textureAtlas.findRegion("replay_pressed")),
                    null)
            setSize(0.3f, 0.3f)
            setPosition(Constants.WORLD_WIDTH - width - 0.02f, 0.02f)
            addListener(
                    object : ChangeListener() {
                        override fun changed(event: ChangeEvent, actor: Actor) {
                            if (classicModeScreenDialogController.isDialogOpen || gameFinished || !gameStarted) {
                                return
                            }
                            classicModeScreenDialogController.showReplayDialog()
                        }
                    })
        }.also {
            uiStagePlayPause.addActor(it)
        }
    }

    override fun addAdditionalInputProcessors() {
        checkState(!gameInputEnabled)
        val im = Gdx.input.inputProcessor as InputMultiplexer
        im.addProcessor(0, classicModeScreenInputController)
        gameInputEnabled = true
        Gdx.app.debug(TAG, "game input enabled")
    }

    override fun gameFinished() {
        dialogUiStage.addAction(
                sequence(
                        delay(1f),
                        Actions.run { classicModeScreenDialogController.showCandyGainDialog() }))
        uiStagePlayPause.clear()
        mainStage0.clear()
        mainStage1.clear()
        uiStage.addAction(sequence(delay(0.5f), run2 { uiStage.clear() }))
        gameFinished = true
        classicModeScreenInputController.gameFinished = true
    }

    private fun allLevelsCompleted() {
        Gdx.app.debug(TAG, "allLevelsCompleted()")
        classicModeScreenDialogController.showAllLevelsCompletedDialog()
        uiStage.clear()
    }

    override fun allGlassCracked() {
        val giftLabel = Label("All Glass Cracked Bonus !", labelStyle(assets.getBitmapFont(Assets.FONT_COSMIC_SANS_RED)))
                .apply {
                    setAlignment(Align.center, Align.center)
                    setFontScale(0.0025f)
                }
        GM_Container(giftLabel).apply {
            setPosition(Constants.WORLD_WIDTH / 2 - giftLabel.width / 2, giftLabel.height / 2)
            addAction(sequence(
                    scaleBy(0.1f, 0.1f, 0.3f),
                    scaleBy(-0.1f, -0.1f, 0.3f),
                    scaleBy(0.1f, 0.1f, 0.3f),
                    scaleBy(-0.1f, -0.1f, 0.3f)))
            addAction(sequence(delay(2f), fadeOut(3f), removeActor()))
        }.also {
            uiStage.addActor(it)
        }
    }

    override fun gameWillBeFinished() {
        hintActor.isVisible = false
        replayButton.isVisible = false
        backButton?.isVisible = false
    }

    override fun removeAdditionalInputProcessors() {
        val im = Gdx.input.inputProcessor as InputMultiplexer
        im.removeProcessor(classicModeScreenInputController)
        gameInputEnabled = false
        Gdx.app.debug(TAG, "game input disabled")
    }

    override fun enableGameInput(enable: Boolean) {
        if (enable == gameInputEnabled) {
            return
        }
        if (enable) {
            addAdditionalInputProcessors()
        } else {
            removeAdditionalInputProcessors()
        }
    }

    companion object {
        private const val TAG = "com.parabolagames.glassmaze.ClassicModeScreen"
    }

    private val crushersLineScreenCoordinates = Vector2(0f, 0f)
    override fun isTouchCoordinatesInGameArea(screenX: Int, screenY: Int): Boolean {
        crushersLineScreenCoordinates.set(0f, handActorCandyGlass.bottomPosition)
        handActorCandyGlass.localToScreenCoordinates(crushersLineScreenCoordinates)
        //screen coordinates y is up
        return screenY > crushersLineScreenCoordinates.y
    }
}