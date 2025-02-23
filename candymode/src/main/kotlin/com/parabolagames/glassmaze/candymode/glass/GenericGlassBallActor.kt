package com.parabolagames.glassmaze.candymode.glass

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.ObjectMap
import com.badlogic.gdx.utils.Pool
import com.badlogic.gdx.utils.Pools
import com.google.common.base.Preconditions
import com.parabolagames.glassmaze.candymode.IGlassMissedListener
import com.parabolagames.glassmaze.candymode.IHandCounter
import com.parabolagames.glassmaze.framework.Box2DActor
import com.parabolagames.glassmaze.shared.*
import com.parabolagames.glassmaze.shared.crack.GenericGlassCrackedPieceActor

internal class GenericGlassBallActor : Box2DActor(), IBreakable, Pool.Poolable {
    private val typeData = ActorTypeData(ActorType.GLASS, this)
    private val clickListener: ClickListener = ClickListener()
    private lateinit var soundPlayer: SoundPlayer
    private var stageForPieces: Stage? = null
    private lateinit var assets: Assets
    private var handCounter: IHandCounter? = null
    private var glassMissedListener: IGlassMissedListener? = null
    private var data: GenericGlassBallCrackedPieceActorData? = null

    private var isCracked = false
    private var isMarkedForBreak = false
    private var soundWillBePlayed = false
    private var becameVisible = false
    private var missedReported = false

    private var mode: ExplanationOrGame? = null
    private var isReset = true
    private var loadedAnimationAssetName: String? = null


    init {
        addListener(clickListener)
    }

    fun poolInitGame(assets: Assets,
                     stageForPieces: Stage,
                     soundPlayer: SoundPlayer,
                     posX: Float,
                     posY: Float,
                     world: World,
                     widthAndHeight: Float,
                     data: GenericGlassBallCrackedPieceActorData,
                     handCounter: IHandCounter?,
                     glassMissedListener: IGlassMissedListener?): GenericGlassBallActor {
        this.mode = ExplanationOrGame.GAME
        this.isReset = false
        this.assets = assets
        this.stageForPieces = stageForPieces
        this.soundPlayer = soundPlayer
        this.handCounter = handCounter
        this.glassMissedListener = glassMissedListener
        this.data = data
        init(posX, posY, world, widthAndHeight)
        return this
    }

    fun poolInitExplanation(assets: Assets,
                            stageForPieces: Stage?,
                            soundPlayer: SoundPlayer,
                            posX: Float,
                            posY: Float,
                            world: World,
                            widthAndHeight: Float,
                            data: GenericGlassBallCrackedPieceActorData,
                            handCounter: IHandCounter?,
                            glassMissedListener: IGlassMissedListener?): GenericGlassBallActor {
        this.mode = ExplanationOrGame.EXPLANATION
        this.isReset = false
        this.assets = assets
        this.stageForPieces = stageForPieces
        this.soundPlayer = soundPlayer
        this.handCounter = handCounter
        this.glassMissedListener = glassMissedListener
        this.data = data
        init(posX, posY, world, widthAndHeight)
        return this
    }

    private fun init(
            posX: Float,
            posY: Float,
            world: World,
            widthAndHeight: Float) {

        val atlasName = data!!.glassAtlas
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
        if (!isCracked && (clickListener.isPressed && (handCounter == null || handCounter!!.handCount > 0) || isMarkedForBreak)) {
            isCracked = true
            soundWillBePlayed = true
            if (clickListener.isPressed) {
                handCounter?.decrementHandCount()
            }
            breakTheGlass()
        }
        if (handCounter != null && !isCracked && clickListener.isPressed && handCounter!!.handCount == 0L) {
            soundPlayer.playNoHandSound()
            handCounter!!.pressedWhenHandCountIsZero()
        }
        if (!becameVisible && y + height > 0) {
            becameVisible = true
            soundPlayer.playGlassAppearSound()
        }
        if (glassMissedListener != null) {
            if (!isCracked && !missedReported && becameVisible && y + height < 0) {
                missedReported = true
                glassMissedListener!!.glassMissed(x + width / 2)
            }
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
                GenericCandyGlassBallActor.DEFAULT_FADE_OUT_TIME,
                GenericCandyGlassBallActor.DEFAULT_DELAY_TIME,
                stageForPieces!!,
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
        stageForPieces = null
        soundWillBePlayed = false
        becameVisible = false
        mode = null
        data = null
        missedReported = false
        setColor(color.r,color.g,color.b,1f)
    }

    override fun setParent(parent: Group?) {
        super.setParent(parent)
        if (parent == null) {
            Preconditions.checkState(!isReset)
            GENERIC_GLASS_BALL_ACTOR_POOL.free(this)
            Gdx.app.debug(TAG, "freed")
        }
    }

    companion object {
        private const val TAG = "GenericGlassBallActor"
        val GENERIC_GLASS_BALL_ACTOR_POOL: Pool<GenericGlassBallActor> = Pools.get(GenericGlassBallActor::class.java)
        private val animationTable = ObjectMap<String, Animation<TextureRegion>>()
    }
}