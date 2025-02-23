package com.parabolagames.glassmaze.candymode.glass

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.Pool
import com.badlogic.gdx.utils.Pools
import com.google.common.base.Preconditions.checkState
import com.parabolagames.glassmaze.framework.Box2DActor
import com.parabolagames.glassmaze.shared.*
import com.parabolagames.glassmaze.shared.crack.GenericGlassCrackedPieceActor

internal class SpinyBallActor() : Box2DActor(), IBreakable, Pool.Poolable {
    private val typeData: ActorTypeData = ActorTypeData(ActorType.SPINY_GLASS, this)
    private lateinit var assets: Assets
    private var stageForCracks: Stage? = null
    private lateinit var soundPlayer: SoundPlayer
    private var isCracked = false
    private var isMarkedForBreak = false
    private var soundWillBePlayed = false
    private var becameVisible = false
    private var isReset = true
    private var mode: ExplanationOrGame? = null

    fun poolInitGame(
            assets: Assets,
            stageForCracks: Stage,
            soundPlayer: SoundPlayer,
            posX: Float,
            posY: Float,
            world: World,
            widthAndHeight: Float): SpinyBallActor {
        this.mode = ExplanationOrGame.GAME
        this.isReset = false
        initialize(assets, stageForCracks, soundPlayer, posX, posY, world, widthAndHeight)
        return this
    }

    fun poolInitExplanation(
            assets: Assets,
            stageForCracks: Stage?,
            soundPlayer: SoundPlayer,
            posX: Float,
            posY: Float,
            world: World,
            widthAndHeight: Float): SpinyBallActor {
        this.mode = ExplanationOrGame.EXPLANATION
        this.isReset = false
        initialize(assets, stageForCracks, soundPlayer, posX, posY, world, widthAndHeight)
        return this
    }

    private fun initialize(
            assets: Assets,
            stageForCracks: Stage?,
            soundPlayer: SoundPlayer,
            posX: Float,
            posY: Float,
            world: World,
            widthAndHeight: Float) {
        this.assets = assets
        this.soundPlayer = soundPlayer
        this.stageForCracks = stageForCracks
        if (animation == null) {
            loadAnimationFromTextureRegions(assets.getTexturesFromTextureAtlas(Assets.SPINY_GLASS_ATLAS), 0.025f, true)
        }
        setSize(widthAndHeight, widthAndHeight)
        setPosition(posX, posY)

        if (fixtureDef.shape == null) {
            setDynamic()
            setShapeCircle()
            setPhysicsProperties(1f, 0.5f, 0.1f)
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
            soundWillBePlayed = true
            breakTheGlass()
        }
        if (!becameVisible && y + height > 0) {
            becameVisible = true
            soundPlayer.playSpinyBallAppearSound()
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
                GenericGlassBallCrackedPieceActorData.SPINY_GLASS,
                GenericCandyGlassBallActor.DEFAULT_FADE_OUT_TIME,
                GenericCandyGlassBallActor.DEFAULT_DELAY_TIME,
                stageForCracks!!,
                1f,
                1f)

        destroyBody()
        remove()
    }

    override fun initializePhysics(w: World) {
        super.initializePhysics(w)
        bodyUserData = typeData
    }

    override fun markForBreak(soundWillBePlayed: Boolean) {
        this.soundWillBePlayed = soundWillBePlayed
        isMarkedForBreak = true
    }

    override fun reset() {
        poolReset()
        clearActions()
        isCracked = false
        isReset = true
        isMarkedForBreak = false
        stageForCracks = null
        soundWillBePlayed = false
        becameVisible = false
        mode = null
    }

    override fun setParent(parent: Group?) {
        super.setParent(parent)
        if (parent == null) {
            checkState(!isReset)
            SPINY_BALL_ACTOR_POOL.free(this)
            Gdx.app.debug("SpinyBallActor", "freed")
        }
    }

    companion object {
        const val DEFAULT_BALL_SIZE = 0.65f
        val SPINY_BALL_ACTOR_POOL: Pool<SpinyBallActor> = Pools.get(SpinyBallActor::class.java)
    }
}