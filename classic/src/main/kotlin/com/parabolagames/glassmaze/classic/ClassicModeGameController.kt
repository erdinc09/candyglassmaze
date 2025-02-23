package com.parabolagames.glassmaze.classic

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions.*
import com.badlogic.gdx.utils.Timer
import com.google.common.base.Preconditions.checkState
import com.google.common.eventbus.EventBus
import com.google.common.eventbus.Subscribe
import com.parabolagames.glassmaze.classic.glass.GenericCandyGlassBallActorData
import com.parabolagames.glassmaze.classic.glass.GenericCandyGlassFactory
import com.parabolagames.glassmaze.framework.ForGame
import com.parabolagames.glassmaze.framework.proguard.Keep
import com.parabolagames.glassmaze.framework.run2
import com.parabolagames.glassmaze.shared.AbstractGameController
import com.parabolagames.glassmaze.shared.Constants
import com.parabolagames.glassmaze.shared.SoundPlayer
import com.parabolagames.glassmaze.shared.event.EventSpinyAndCandyHandPurchased
import dagger.Lazy
import javax.inject.Inject
import javax.inject.Named

@ForGame
internal class ClassicModeGameController
@Inject constructor(
        @param:Named(ClassicModeModule2.CLASSIC_MAIN_STAGE_0) private val mainStage0: Stage,
        @param:Named(ClassicModeModule2.CLASSIC_MAIN_STAGE_1) private val mainStage1: Stage,
        private val soundPlayer: SoundPlayer,
        private val classicMazePersistenceManager: IClassicMazePersistenceManager,
        private val candyCounter: ICandyCounter,
        private val level: Level?,
        private val levelNumberActor: LevelNumberActor,
        private val gameFinishedCandyGenerator: GameFinishedCandyGenerator,
        private val gameFinishedListener: Lazy<IClassicModeScreenGameFinishedListener>,
        private val spinyGlassHandActor: ISpinyGlassHandActor,
        private val candyGlassHandActor: ICandyGlassHandActor,
        private val subMode: ClassicModeSubMode,
        private val mazeController: MazeController,
        @param:Named(ClassicModeModule2.CLASSIC_WORLD) private val world: World,
        private val classicModeScreenDialogController: Lazy<ClassicModeScreenDialogController>,
        private val eventBus: EventBus,
        private val genericCandyGlassFactory: Lazy<GenericCandyGlassFactory>,
        private val bonusCandyHandActorFactory: BonusCandyHandActor.BonusCandyHandActorFactory,
        private val bonusSpinyHandActorFactory: BonusSpinyHandActor.BonusSpinyHandActorFactory) : AbstractGameController(world),
        ICandyGainer,
        IMazeCubeControl,
        IGlassBallBreakListener,
        INoHandCountListener {

    private var candyGainedUntilNow = 0
    private var candyCrackedButNotGainedUntilNow = 0
    private var glassBrokenInLevel = 0
    private var gameFinished = false
    private var allCandiesGained = false
    private var targetCandyCount = 10000

    init {
        eventBus.register(this)
    }

    fun update(dt: Float) {
        if (gameFinished) {
            gameFinishedCandyGenerator.generateCandies(dt)
        }
        updateWorld(dt)
        if (Constants.DEBUG_STAGE_CHILDREN_SIZE) {
            Gdx.app.debug(TAG, String.format("mainstage0 actors size = %d", mainStage0.actors.size))
            Gdx.app.debug(TAG, String.format("mainstage1 actors size = %d", mainStage1.actors.size))
        }
    }

    fun doBeforeQuitJobs() {
        soundPlayer.stopBackGroundSound1()
        saveGame()
    }

    fun saveGame() = classicMazePersistenceManager.saveClassicMaze()

    fun initialize() {
        checkState(level != null, "cannot be called while level is null")
        targetCandyCount = level!!.candyCount
        levelNumberActor.setLevelNumber(level!!.levelNumber)
        soundPlayer.loopBackGroundSound1()
        mazeController.generateMaze()
        mainStage0.addAction(fadeOut(0.0f))
        mainStage0.addAction(sequence(delay(0.25f), fadeIn(0.75f)))
    }

    override val isMazeBallMoving: Boolean get() = mazeController.isMazeBallMoving

    override fun candyGlassCrackedButNotGained(row: Int, col: Int) {
        candyCrackedButNotGainedUntilNow++
        if (!allCandiesGained) {
            level!!.clearGridValue(row, col)
            if (candyCrackedButNotGainedUntilNow == level!!.candyCount) {
                allCandiesGained = true
                mazeController.gameWillBeFinished()
                checkAllGlassCracked()
            }
        }
    }

    override fun candyGained(candyData: GenericCandyGlassBallActorData, row: Int, col: Int) {
        checkState(level != null, "cannot be called while level is null")
        candyCounter.incrementCandy()
        candyGainedUntilNow++
        gameFinishedCandyGenerator.addGenericCandyGlassBallActorData(candyData)
        if (candyGainedUntilNow == targetCandyCount) {
            gameFinishedListener.get().gameWillBeFinished()
            Timer.schedule(
                    object : Timer.Task() {
                        override fun run() {
                            gameFinishedListener.get().gameFinished()
                            gameFinished = true
                            if (subMode == ClassicModeSubMode.CLASSIC) {
                                classicMazePersistenceManager.levelNumber = level!!.levelNumber + 1
                            }
                            doBeforeQuitJobs()
                        }
                    },
                    1f)
        }
    }

    override fun moveMazeCube(direction: MazeCubeDirection) = mazeController.moveMazeCube(direction)

    fun dispose() {
        eventBus.unregister(this)
        Gdx.app.debug(TAG, "disposed")
        disposeWorld()
        mazeController.dispose()
        level?.freeToPool()
    }

    override fun genericGlassBroken(row: Int, col: Int) {
        checkState(level != null, "cannot be called while level is null")
        level!!.clearGridValue(row, col)
        glassBrokenInLevel++
        if (allCandiesGained) {
            checkAllGlassCracked()
        }
    }

    private var allGlassCrackedChecked = false
    private fun checkAllGlassCracked() {
        if (!allGlassCrackedChecked && glassBrokenInLevel == level!!.glassCount) {
            allGlassCrackedChecked = true
            gameFinishedListener.get().allGlassCracked()
            targetCandyCount += 5
            createGiftCandyGlass(-2)
            createGiftCandyGlass(-1)
            createGiftCandyGlass(0)
            createGiftCandyGlass(1)
            createGiftCandyGlass(2)
            candyCounter.giftCandiesWillBeGained()
            if (subMode == ClassicModeSubMode.CLASSIC) {
                createGiftCrushers()
            }
        }
    }

    private fun createGiftCrushers() {
        bonusCandyHandActorFactory.getBonusCandyHandActor().apply {
            setPosition(Constants.WORLD_WIDTH * 0.25f - width / 2, -height)
            addBonusAction()
            mainStage0.addActor(this)
        }
        bonusSpinyHandActorFactory.getBonusSpinyHandActor().apply {
            setPosition(Constants.WORLD_WIDTH * 0.75f - width / 2, -height)
            addBonusAction()
            mainStage0.addActor(this)
        }
    }

    private fun createGiftCandyGlass(col: Int) =
            genericCandyGlassFactory.get().getRandomGenericCandyGlassBallActor(0, col, Constants.WORLD_WIDTH / 2 - GridActor.CELL_SIZE / 2, 0f)
                    .apply {
                        addAction(sequence(
                                moveTo(x + col * width / 4, 0.2f, 1f, Interpolation.pow3),
                                run2 { markForBreak(true) }))
                        mainStage0.addActor(this)
                    }


    @Subscribe
    @Keep
    private fun esachpl(event: EventSpinyAndCandyHandPurchased) {
        spinyGlassHandActor.updateFromDataPersistenceManager()
        candyGlassHandActor.updateFromDataPersistenceManager()
        candyCounter.updateFromDataPersistenceManager()
    }

    override fun handCountIsZeroAndUserPressedToHandSpinyOrCandy() = classicModeScreenDialogController.get().showHandCountIsZeroAndUserPressedToHandSpinyOrCandyDialog()

    companion object {
        private const val TAG = "com.parabolagames.glassmaze.ClassicModeGameController"
    }
}