package com.parabolagames.glassmaze.candymode

import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.google.common.base.Preconditions.checkState
import com.parabolagames.glassmaze.framework.ForGame
import com.parabolagames.glassmaze.framework.IScreenDrawer
import com.parabolagames.glassmaze.shared.Assets
import com.parabolagames.glassmaze.shared.Constants
import com.parabolagames.glassmaze.shared.IPlatformUiFix
import com.parabolagames.glassmaze.shared.SoundPlayer
import dagger.Lazy
import javax.inject.Inject
import javax.inject.Named

@ForGame
internal class SceneCreator @Inject constructor(
        @param:Named(CandyModeModule.CANDY_WORLD) private val world: World,
        @param:Named(CandyModeModule.CANDY_UI_STAGE) private val uiStage: Stage,
        private val soundPlayer: SoundPlayer,
        private val assets: Assets,
        private val dataPersistenceManager: ICandyCountPersistenceManager,
        private val screenDrawer: Lazy<IScreenDrawer>,
        private val candyModeGameController: Lazy<CandyModeGameController>,
        private val platformUiFix: IPlatformUiFix) {

    private lateinit var ball1Holder: Ball1HolderActor
    private lateinit var ball2Holder: Ball2HolderActor

    private lateinit var leftWall: WallActor
    private lateinit var rightWall: WallActor
    private lateinit var roof: WallActor

    fun destroyScene() {
        ball1Holder.remove()
        ball2Holder.remove()

        rightWall.remove()
        leftWall.remove()
        roof.remove()
    }

    fun addBallHolders() {
        checkState(!::ball1Holder.isInitialized)
        checkState(!::ball2Holder.isInitialized)
        val iosPad = if (platformUiFix.isThrowingBallPadInCandyModeNecessary) 0.4f else 0.25f
        ball1Holder = Ball1HolderActor(
                -0.1f,
                0.04f + iosPad,
                assets,
                world,
                screenDrawer.get(),
                soundPlayer)
        uiStage.addActor(ball1Holder)
        ball2Holder = Ball2HolderActor(
                -0.1f,
                ball1Holder.y + ball1Holder.height - 0.1f,
                assets,
                world,
                screenDrawer.get(),
                candyModeGameController.get(),
                dataPersistenceManager.ironNumberCount
        )
        uiStage.addActor(ball2Holder)
        val leftPad = 0.7f
        val duration = 0.5f
        ball1Holder.moveBy(-leftPad, 0f)
        ball1Holder.addAction(Actions.moveBy(leftPad, 0f, duration))
        ball2Holder.moveBy(-leftPad, 0f)
        ball2Holder.addAction(Actions.moveBy(leftPad, 0f, duration))
    }

    fun addWalls() {
        checkState(!::leftWall.isInitialized)
        checkState(!::rightWall.isInitialized)
        checkState(!::roof.isInitialized)

        leftWall = WallActor.create(0.01f, Constants.WORLD_HEIGHT, 0f, 0f, world)
        rightWall = WallActor.create(0.01f, Constants.WORLD_HEIGHT, Constants.WORLD_WIDTH - 0.01f, 0f, world)
        roof = WallActor.create(Constants.WORLD_WIDTH, 0.01f, 0f, Constants.WORLD_HEIGHT - 0.01f, world)
    }
}