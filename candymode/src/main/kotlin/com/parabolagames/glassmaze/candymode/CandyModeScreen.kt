package com.parabolagames.glassmaze.candymode

import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions.*
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.Timer
import com.parabolagames.glassmaze.framework.CurrentScreen
import com.parabolagames.glassmaze.framework.ForGame
import com.parabolagames.glassmaze.framework.run2
import com.parabolagames.glassmaze.shared.*
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Provider

@ForGame
internal class CandyModeScreen @Inject constructor(
        @Named(CandyModeModule.CANDY_MAIN_STAGE_0) mainStage0: Stage,
        @Named(CandyModeModule.CANDY_MAIN_STAGE_1) mainStage1: Stage,
        @Named(CandyModeModule.CANDY_MAIN_STAGE__1) mainStage_1: Stage,
        @Named(CandyModeModule.CANDY_UI_STAGE) uiStage: Stage,
        @Named(CandyModeModule.CANDY_UI_STAGE_PLAY_PAUSE) uiStagePlayPause: Stage,
        @Named(CandyModeModule.CANDY_DIALOG_UI_STAGE) dialogUiStage: Stage,
        @Named(CandyModeModule.CANDY_DIALOG_UI_STAGE_FOR_INFORMATION) dialogUiStageForInformation: Stage,
        private val game: IGlassMazeCandyMode,
        @Named(CandyModeModule.CANDY_WORLD) world: World,
        private val backgroundActor: GameBackGroundActor,
        assets: Assets,
        private val candyModeGameController: CandyModeGameController,
        private val candyCountActor: CandyCountActor,
        private val handActor: HandActorCandyGlass,
        private val lifeActor: LifeActor,
        private val candyGainDialogProvider: Provider<CandyGainDialog>,
        private val pauseDialogFactory: PauseDialog.PauseDialogFactory,
        private val gameExplanationDialogFactory: GameExplanationDialog.GameExplanationDialogFactory,
        private val gameExplanationDialogAppearanceController: IGameExplanationDialogAppearanceController,
        private val platformUiFix: IPlatformUiFix,
        private val addsController: IAddsController) : GameScreen(
        mainStage_1,
        mainStage0,
        mainStage1,
        uiStage,
        uiStagePlayPause,
        dialogUiStage,
        dialogUiStageForInformation,
        null,
        world,
        assets), ICandyModeScreenGameFinishedListener {

    private val uiTable: Table = Table()
    private lateinit var pauseButton: Button
    private var isQuitDialogOpen = false
    private var gameFinished = false
    private var gameStarted = false
    private var backButton: Button? = null

    override val currentScreen: CurrentScreen = CurrentScreen.CANDY_MODE

    override fun initialize() {
        Gdx.app.debug(TAG, "initialize()")
        mainStage_1.addActor(backgroundActor)
        if (gameExplanationDialogAppearanceController.isItTimeToShowGameExplanationDialog) {
            val gameExplanationDialog = gameExplanationDialogFactory.create { initializeCandyMode() }
            gameExplanationDialog.show(dialogUiStage)
            gameExplanationDialogAppearanceController.isItTimeToShowGameExplanationDialog = false
        } else {
            initializeCandyMode()
        }
    }

    private fun initializeCandyMode() {
        candyModeGameController.initialize()
        with(uiTable) {
            setFillParent(true)
            setRound(false)
            add(candyCountActor).left().top().padLeft(0.01f).padTop(0.01f)
            add(handActor).top().right().padRight(0.25f).padTop(0.01f).expand()
            add(lifeActor).right().top().padRight(0.01f)
            uiStage.addActor(this)
        }

        createAndAddPlayPauseButton()
        val topPad = 0.5f
        val duration = 0.5f
        var iosPad = 0f
        var candyPad = 0f
        if (Gdx.app.type == Application.ApplicationType.iOS) {
            createAndAddBackButton()
            with(backButton!!) {
                setPosition(Constants.WORLD_WIDTH - pauseButton.width - 0.02f - width - 0.02f, 0.02f)
                moveBy(0f, -topPad)
                addAction(moveBy(0f, topPad, duration))
            }

        }
        if (platformUiFix.isUiAlignmentFromTopIsNecessary) {
            with(backButton!!) {
                setPosition(0.08f, Constants.WORLD_HEIGHT - 0.02f - height)
                moveBy(0f, topPad)
                clearActions()
                addAction(moveBy(0f, -topPad, duration))
            }
            iosPad = 0.30f
            candyPad = 0.45f
        }
        uiTable.getCell(candyCountActor).padTop(-topPad)
        candyCountActor.addAction(moveBy(0f, -topPad - candyPad, duration))
        uiTable.getCell(handActor).padTop(-topPad)
        handActor.addAction(moveBy(0f, -topPad - 0.1f - iosPad, duration))
        uiTable.getCell(lifeActor).padTop(-topPad)
        lifeActor.addAction(moveBy(0f, -topPad - iosPad, duration))
        pauseButton.moveBy(0f, -topPad)
        pauseButton.addAction(moveBy(0f, topPad, duration))
        Timer.schedule(
                object : Timer.Task() {
                    override fun run() {
                        gameStarted = true
                    }
                },
                duration)
    }

    override fun update(dt: Float) {
        super.update(dt)
        if (gameStarted) {
            candyModeGameController.update(dt)
        }
    }

    override fun keyDown(keycode: Int): Boolean {
        if (keycode == Input.Keys.BACK || keycode == Input.Keys.DEL || keycode == Input.Keys.SPACE) {
            if (isQuitDialogOpen || gameFinished || !gameStarted) {
                return false
            }
            Gdx.app.postRunnable { showQuitDialog() }
        }
        return false
    }

    private fun showQuitDialog() {
        isQuitDialogOpen = true
        pauseButton.isVisible = false
        backButton?.isVisible = false


        ApprovalDialog(
                assets,
                "Quit Candy Mode?",
                {
                    candyModeGameController.candyModeWillBeQuit()
                    game.setMenuScreenReVisible()
                },
                {
                    backButton?.isVisible = true
                    isQuitDialogOpen = false
                },
                addsController).show(dialogUiStage)
    }

    private fun createAndAddPlayPauseButton() {
        pauseButton = Button().apply {
            val textureAtlas = assets.getTextureAtlas(Assets.BUTTONS_PLAY_PAUSE)
            setColor(1f, 1f, 1f, 0.8f)
            style = ButtonStyle(
                    TextureRegionDrawable(textureAtlas.findRegion("pause_released")),
                    TextureRegionDrawable(textureAtlas.findRegion("pause_pressed")), null)
            setSize(0.3f, 0.3f)
            setPosition(Constants.WORLD_WIDTH - width - if (platformUiFix.isUiAlignmentFromTopIsNecessary) 0.04f else 0.02f,
                    0.02f)
            addListener(object : ChangeListener() {
                override fun changed(event: ChangeEvent, actor: Actor) {
                    check(!isPaused)
                    pauseFromGame()
                    showPauseDialog()
                }
            })
        }.also { uiStagePlayPause.addActor(it) }
    }

    private fun createAndAddBackButton() {
        backButton = Button().apply {
            setColor(1f, 1f, 1f, 0.8f)
            val textureAtlas = assets.getTextureAtlas(Assets.BUTTONS_BACK)
            style = ButtonStyle(TextureRegionDrawable(textureAtlas.findRegion("back_released")),
                    TextureRegionDrawable(textureAtlas.findRegion("back_pressed")), null)
            setSize(0.3f, 0.3f)
            addListener(object : ChangeListener() {
                override fun changed(event: ChangeEvent, actor: Actor) {
                    if (isQuitDialogOpen || gameFinished || !gameStarted) {
                        return
                    }
                    showQuitDialog()
                }
            })
        }.also { uiStagePlayPause.addActor(it) }
    }

    override fun pause() {
        if (isPaused || gameFinished || !gameStarted) {
            return
        }
        pauseFromGame()
        showPauseDialog()
    }

    override fun pauseFromGame() {
        super.pauseFromGame()
        pauseButton.isVisible = false
        backButton?.isVisible = false
    }

    override fun resumeFromGame() {
        super.resumeFromGame()
        pauseButton.isVisible = true
        backButton?.isVisible = true
    }

    private fun showPauseDialog() {
        pauseDialogFactory.create {
            pauseButton.isVisible = true
        }.show(dialogUiStage)
    }

    override fun gameFinished() {
        dialogUiStage.addAction(sequence(delay(1f), run2 { candyGainDialogProvider.get().show(dialogUiStage) }))
        uiStagePlayPause.clear()
        mainStage0.clear()
        mainStage1.clear()
        uiStage.addAction(sequence(delay(0.5f), run2 { uiStage.clear() }))
        gameFinished = true
    }

    override fun dispose() {
        super.dispose()
        candyModeGameController.dispose()
    }

    companion object {
        private const val TAG = "CandyModeScreen"
    }
}