package com.parabolagames.glassmaze.candymode.glass

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions.*
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Pool
import com.parabolagames.glassmaze.candymode.ICandyGainer
import com.parabolagames.glassmaze.candymode.IGlassMissedListener
import com.parabolagames.glassmaze.candymode.IHandCounter
import com.parabolagames.glassmaze.framework.Box2DActor
import com.parabolagames.glassmaze.framework.TableActor
import com.parabolagames.glassmaze.shared.*
import com.parabolagames.glassmaze.shared.crack.GenericGlassCrackedPieceActor

internal abstract class GenericCandyGlassBallActor(private val loopAnimation: Boolean)

    : Box2DActor(), IBreakable, Pool.Poolable {

    private val clickListener: ClickListener = ClickListener()
    private var isCracked = false
    private var isMarkedForBreak = false
    private var soundWillBePlayed = false
    private var becameVisible = false
    private var missedReported = false
    private val typeData = ActorTypeData(ActorType.GLASS, this)
    private lateinit var data: GenericCandyGlassBallActorData
    private lateinit var candyThrown: GenericCandyActor
    private lateinit var assets: Assets
    private lateinit var candyInGlass: TableActor

    private var mainStage0: Stage? = null
    private var mainStage1: Stage? = null
    private var soundPlayer: SoundPlayer? = null
    private var candyGainer: ICandyGainer? = null
    private var handCounter: IHandCounter? = null
    private var glassMissedListener: IGlassMissedListener? = null
    private var mode: ExplanationOrGame? = null

    init {
        addListener(clickListener)
    }

    fun initPoolForGame(assets: Assets,
                        mainStage0: Stage,
                        mainStage1: Stage,
                        soundPlayer: SoundPlayer,
                        posX: Float,
                        posY: Float,
                        world: World,
                        candyGainer: ICandyGainer,
                        handCounter: IHandCounter,
                        glassMissedListener: IGlassMissedListener,
                        widthAndHeight: Float): GenericCandyGlassBallActor {
        this.assets = assets
        this.mainStage0 = mainStage0
        this.mainStage1 = mainStage1
        this.soundPlayer = soundPlayer
        this.candyGainer = candyGainer
        this.handCounter = handCounter
        this.glassMissedListener = glassMissedListener
        this.mode = ExplanationOrGame.GAME
        init(world, posX, posY, widthAndHeight)
        return this
    }

    fun initPoolForExplanation(assets: Assets,
                               mainStage0: Stage?,
                               mainStage1: Stage?,
                               soundPlayer: SoundPlayer,
                               posX: Float,
                               posY: Float,
                               world: World,
                               candyGainer: ICandyGainer,
                               handCounter: IHandCounter,
                               glassMissedListener: IGlassMissedListener,
                               widthAndHeight: Float): GenericCandyGlassBallActor {
        this.assets = assets
        this.mainStage0 = mainStage0
        this.mainStage1 = mainStage1
        this.soundPlayer = soundPlayer
        this.candyGainer = candyGainer
        this.handCounter = handCounter
        this.glassMissedListener = glassMissedListener
        this.mode = ExplanationOrGame.EXPLANATION
        init(world, posX, posY, widthAndHeight)
        return this
    }

    private fun init(
            world: World,
            posX: Float,
            posY: Float,
            widthAndHeight: Float) {

        if (!::data.isInitialized) {
            data = getData()
        }

        if (!::candyThrown.isInitialized) {
            candyThrown = GenericCandyActor(
                    assets, data.candyAtlas, data.size, loopAnimation, data.frameDurationSupplier)
        }
        if (animation == null) {
            loadTexture(assets.getTexture(Assets.GLASS2))
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

        if (!::candyInGlass.isInitialized) {
            candyInGlass = TableActor().apply {
                loadAnimationFromTextureRegions(
                        assets.getTexturesFromTextureAtlas(data.candyAtlas),
                        MathUtils.random(0.015f, 0.035f),
                        true,
                        MathUtils.random(0, assets.getTexturesFromTextureAtlas(data.candyAtlas).size))
                isAnimationPaused = !data.isAnimatedInGlass
                if (!data.isAnimatedInGlass) {
                    addAction(forever(sequence(
                            scaleBy(-0.1f, -0.1f, 1f),
                            scaleBy(0.1f, 0.1f, 1f))))
                }
                setColor(0.6f, 0.6f, 0.6f, 1f)
                setSize(widthAndHeight / data.candyInGlassSizeRatio, widthAndHeight / data.candyInGlassSizeRatio)
                setOrigin(width / 2, height / 2)
                setPosition(this@GenericCandyGlassBallActor.width / 2 - width / 2, this@GenericCandyGlassBallActor.height / 2 - height / 2)
            }.also {
                addActor(it)
            }
        }

        initializePhysics(world)
    }

    protected abstract fun getData(): GenericCandyGlassBallActorData

    override fun act(dt: Float) {
        super.act(dt)
        if (!isCracked && (clickListener.isPressed && handCounter!!.handCount > 0 || isMarkedForBreak)) {
            isCracked = true
            soundWillBePlayed = true
            if (clickListener.isPressed) {
                handCounter!!.decrementHandCount()
            }
            breakTheGlass()
        }
        if (!isCracked && clickListener.isPressed && handCounter!!.handCount == 0L) {
            soundPlayer!!.playNoHandSound()
            handCounter!!.pressedWhenHandCountIsZero()
        }
        if (!becameVisible && y + height > 0) {
            becameVisible = true
            soundPlayer!!.playGlassAppearSound()
        }
        if (!isCracked && !missedReported && becameVisible && y + height < 0) {
            missedReported = true
            glassMissedListener!!.glassMissed(x + width / 2)
        }
    }

    override fun breakTheGlass() {
        if (soundWillBePlayed) {
            soundPlayer!!.playRandomBreak()
        }

        GenericGlassCrackedPieceActor.createAllPiecesWithSplit(
                x,
                y,
                assets,
                width,
                GenericGlassBallCrackedPieceActorData.GLASS_2,
                DEFAULT_FADE_OUT_TIME,
                DEFAULT_DELAY_TIME,
                mainStage0!!,
                1f,
                1f)

        candyThrown.setAnimationAlignedFromActor(candyInGlass)
        candyThrown.reInit(x + width / 2 - data.size / 2, y + height / 2 - data.size / 2, baseWorld!!)

        candyThrown.setVelocity(MathUtils.random(-1f, 1f), MathUtils.random(1.8f, 2.3f))
        mainStage1!!.addActor(candyThrown)

        Box2DActorDelayedRemover.add(candyThrown)
        candyThrown.addAction(scaleBy(0.25f, 0.25f, 3f))
        candyGainer!!.candyGained(data)

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
        isMarkedForBreak = false
        mainStage0 = null
        mainStage1 = null
        candyGainer = null
        handCounter = null
        soundWillBePlayed = false
        becameVisible = false
        mode = null
        glassMissedListener = null
        missedReported=false
        setColor(color.r,color.g,color.b,1f)
    }


    companion object {
        const val DEFAULT_FADE_OUT_TIME = 0.5f
        const val DEFAULT_DELAY_TIME = 0.25f
        const val DEFAULT_BALL_SIZE = 0.55f
    }
}