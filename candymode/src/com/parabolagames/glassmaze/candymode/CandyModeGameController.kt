package com.parabolagames.glassmaze.candymode

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Stage
import com.google.common.base.Preconditions.checkState
import com.parabolagames.glassmaze.candymode.glass.*
import com.parabolagames.glassmaze.framework.ForGame
import com.parabolagames.glassmaze.shared.*
import dagger.Lazy
import java.lang.Boolean
import javax.inject.Inject
import javax.inject.Named

@ForGame
internal class CandyModeGameController @Inject constructor(
        @param:Named(CandyModeModule.CANDY_WORLD) private val world: World,
        @param:Named(CandyModeModule.CANDY_MAIN_STAGE_0) private val mainStage0: Stage,
        @param:Named(CandyModeModule.CANDY_MAIN_STAGE_1) private val mainStage1: Stage,
        @param:Named(CandyModeModule.CANDY_UI_STAGE) private val uiStage: Stage,
        private val soundPlayer: SoundPlayer,
        private val assets: Assets,
        // Lazy is for cycles :-(
        private val candyGlassBallFactory: Lazy<CandyGlassBallFactory>,
        private val glassBallFactory: Lazy<GlassBallFactory>,
        private val fireGlassBallFactory: Lazy<FireGlassBallFactory>,
        private val candyCounter: ICandyCounter,
        private val dataPersistenceManager: ICandyCountPersistenceManager,
        private val bestScoreCounter: IBestScoreCounter,
        private val lifeCounter: ILifeCounter,
        private val candyModeScreenGameFinishedListener: Lazy<ICandyModeScreenGameFinishedListener>,
        private val sceneCreator: SceneCreator,
        private val gameFinishedCandyGenerator: GameFinishedCandyGenerator)
    : AbstractGameController(world),
        ICandyGainer,
        IGlassMissedListener,
        IIronBallGiftListener {

    private var timePassed = 4f
    private var gameFinished = false
    fun update(dt: Float) {
        if (gameFinished) {
            doFinishedGameJobs(dt)
        } else {
            throwTestGlasses(dt)
        }
        updateWorld(dt)
        if (Constants.DEBUG_STAGE_CHILDREN_SIZE) {
            Gdx.app.debug(TAG, String.format("mainstage0 actors size = %d", mainStage0.actors.size))
            Gdx.app.debug(TAG, String.format("mainstage1 actors size = %d", mainStage1.actors.size))
        }
    }

    private fun doFinishedGameJobs(dt: Float) {
        soundPlayer.stopBackGroundSound1()
        gameFinishedCandyGenerator.generateCandies(dt)
    }

    private fun throwTestGlasses(dt: Float) {
        timePassed += dt
        if (timePassed > 5) {
            timePassed = 0f
            throwFireBall(2f, 0f, 0f, 5f, GenericCandyGlassBallActor.DEFAULT_BALL_SIZE)
//            throwBall(2f, 0f, 0f, 5f, GenericCandyGlassBallActor.DEFAULT_BALL_SIZE)
//            throwBall(1f, 0f, 0f, 5f, GenericCandyGlassBallActor.DEFAULT_BALL_SIZE)
//            throwSpinyGlass(2f, 1f, 0f, 4f, SpinyBallActor.DEFAULT_BALL_SIZE)
//
//            throwBallCandy(1.5f, 0f, -2f, 5f, GenericCandyGlassBallActor.DEFAULT_BALL_SIZE)
//            throwBallCandy(2f, 0f, 1f, 5.5f, GenericCandyGlassBallActor.DEFAULT_BALL_SIZE)
        }
    }

    private fun throwSpinyGlass(posX: Float, posY: Float, velX: Float, velY: Float, size: Float) =
            SpinyBallActor.SPINY_BALL_ACTOR_POOL.obtain().poolInitGame(assets, mainStage0, soundPlayer, posX, posY, world, size)
                    .apply {
                        setVelocity(velX, velY)
                    }.also {
                        mainStage0.addActor(it)
                        Box2DActorDelayedRemover.add(it)
                    }


    private fun throwBallCandy(
            posX: Float, posY: Float, velX: Float, velY: Float, size: Float) {
        candyGlassBallFactory.get().getRandomGenericCandyGlassBallActor(posX, posY, size)
                .apply {
                    setVelocity(velX, velY)
                }.also {
                    mainStage0.addActor(it)
                    Box2DActorDelayedRemover.add(it)
                }
    }

    private fun throwBall(posX: Float, posY: Float, velX: Float, velY: Float, size: Float) {
        glassBallFactory.get().create(posX, posY, size)
                .apply {
                    setVelocity(velX, velY)
                }.also {
                    mainStage0.addActor(it)
                    Box2DActorDelayedRemover.add(it)
                }
    }

    private fun throwFireBall(posX: Float, posY: Float, velX: Float, velY: Float, size: Float) {
        fireGlassBallFactory.get().create(posX, posY, size)
                .apply {
                    setVelocity(velX, velY)
                }.also {
                    mainStage0.addActor(it)
                    Box2DActorDelayedRemover.add(it,Constants.WORLD_HEIGHT)
                }
    }

    fun initialize() {
        soundPlayer.loopBackGroundSound1()
        sceneCreator.addWalls()
        sceneCreator.addBallHolders()
    }

    override fun candyGained(candyData: GenericCandyGlassBallActorData) {
        candyCounter.incrementCandy()
        gameFinishedCandyGenerator.addGenericCandyGlassBallActorData(candyData)
        checkBestScoreReached()
    }

    private fun checkBestScoreReached() {
        val candyCount = candyCounter.candy
        if (candyCount > dataPersistenceManager.bestScore) {
            dataPersistenceManager.bestScore = candyCount
            bestScoreCounter.setBestScore(candyCount)
        }
    }

    fun candyModeWillBeQuit() {
        soundPlayer.stopBackGroundSound1()
        dataPersistenceManager.saveCandyMode()
    }

    override fun glassMissed(xWorld: Float) {
        if (DISABLE_GAME_OVER || gameFinished) {
            return
        }
        checkState(lifeCounter.lifeCount != 0, "should not happen")
        uiStage.addActor(GlassMissedActor(assets, xWorld, 0f))//TODO:POOL
        soundPlayer.playGlassMissedSound()
        if (lifeCounter.decrementLife() == 0) {
            gameFinished = true
            Gdx.app.postRunnable {
                candyModeScreenGameFinishedListener.get().gameFinished()
                sceneCreator.destroyScene()
                dataPersistenceManager.saveCandyMode()
            }
        }
    }

    override fun timesGlassBroken(size: Int) {
        candyCounter.giftCandiesGained(size)
        soundPlayer.playSound(Assets.BREAK_SOUND_8)
        checkBestScoreReached()
    }

    fun dispose() {
        disposeWorld()
    }

    companion object {
        private val DISABLE_GAME_OVER = Boolean.getBoolean("disableGameOver")
        private const val TAG = "CandyModeGameController"
    }
}