package com.parabolagames.glassmaze.candymode

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.ObjectMap
import com.badlogic.gdx.utils.Pool
import com.badlogic.gdx.utils.Pools
import com.google.common.base.Preconditions
import com.parabolagames.glassmaze.shared.*
import com.parabolagames.glassmaze.shared.crack.GenericGlassCrackedPieceActor

internal class ThrowableGlassBallActor : ThrowableBallActor(), IBreakable, Pool.Poolable {

    private var isMarkedForBreak = false
    private var isCracked = false
    private var soundWillBePlayed = false
    private val actorType = ActorTypeData(ActorType.GLASS_BALL, this)
    private var isReset = true

    private var loadedAnimationAssetName: String? = null

    private lateinit var assets: Assets
    private lateinit var soundPlayer: SoundPlayer
    private var stageForPieces: Stage? = null
    private var data: GenericGlassBallCrackedPieceActorData? = null

    private fun init(soundPlayer: SoundPlayer,
                     assets: Assets,
                     posX: Float,
                     posY: Float,
                     ballRadius: Float,
                     world: World,
                     stageForPieces: Stage?,
                     data: GenericGlassBallCrackedPieceActorData) {
        isReset=false
        this.assets = assets
        this.soundPlayer = soundPlayer
        this.data = data
        this.stageForPieces = stageForPieces

        val atlasName = data.glassAtlas
        if (animation == null || atlasName != loadedAnimationAssetName) {
            loadedAnimationAssetName = atlasName
            var animationFromTable = animationTable.get(loadedAnimationAssetName)
            if (animationFromTable == null) {
                val animation = loadTexture(assets.getTexture(data!!.glassAtlas))
                animationTable.put(loadedAnimationAssetName, animation)
                animationFromTable = animation
                Gdx.app.debug(TAG, "new texture created")
            } else {
                Gdx.app.debug(TAG, "cached texture used")
            }
            setAnimation(animationFromTable)
        } else {
            Gdx.app.debug(TAG, "current texture used")
        }

        setPosition(posX, posY)
        setSize(ballRadius * 2, ballRadius * 2)
        if (fixtureDef.shape == null) {
            setDynamic()
            setShapeCircle()
            setPhysicsProperties(10f, 1f, 1f)
            setFixedRotation()
        } else {
            resetShapeCirclePosition()
        }

        initializePhysics(world)
    }

    override fun act(dt: Float) {
        super.act(dt)
        if (!isCracked && isMarkedForBreak) {
            isCracked = true
            breakTheGlass()
        }
    }

    override fun breakTheGlass() {
        if (soundWillBePlayed) {
            soundPlayer.playRandomBreak()
        }

        GenericGlassCrackedPieceActor.createAllPiecesWithSplit(
                x,
                y,
                assets,
                width,
                data!!,
                0.3f,
                0.1f,
                stageForPieces!!,
                1f,
                1f)

        destroyBody()
        remove()
    }

    override fun markForBreak(soundWillBePlayed: Boolean) {
        this.soundWillBePlayed = soundWillBePlayed
        isMarkedForBreak = true
    }

    override fun initializePhysics(w: World) {
        super.initializePhysics(w)
        bodyUserData = actorType
    }

    override fun reset() {
        poolReset()
        clearActions()
        isCracked = false
        isMarkedForBreak = false
        soundWillBePlayed = false
        isReset = true
        setColor(color.r, color.g, color.b, 1f)
    }

    override fun setParent(parent: Group?) {
        super.setParent(parent)
        if (parent == null) {
            Preconditions.checkState(!isReset)
            THROWABLE_BALL_ACTOR_POOL.free(this)
            Gdx.app.debug(TAG, "freed")
        }
    }

    companion object {
        private val THROWABLE_BALL_ACTOR_POOL: Pool<ThrowableGlassBallActor> = Pools.get(ThrowableGlassBallActor::class.java)
        private const val TAG = "ThrowableGlassBallActor"
        private val animationTable = ObjectMap<String, Animation<TextureRegion>>()
        fun createForGame(
                x: Float,
                y: Float,
                assets: Assets,
                stage: Stage,
                soundPlayer: SoundPlayer,
                data: GenericGlassBallCrackedPieceActorData,
                world: World): ThrowableGlassBallActor =
                THROWABLE_BALL_ACTOR_POOL.obtain()
                        .apply {
                            init(soundPlayer, assets, x, y, BALL_RADIUS, world, stage, data)
                            Box2DActorDelayedRemover.add(this)
                        }


        fun createForUI(
                x: Float,
                y: Float,
                assets: Assets,
                soundPlayer: SoundPlayer,
                data: GenericGlassBallCrackedPieceActorData, world: World, ballRadius: Float):
                ThrowableGlassBallActor = THROWABLE_BALL_ACTOR_POOL.obtain()
                .apply {
                    init(soundPlayer, assets, x, y, ballRadius, world, null, data)
                }
    }
}