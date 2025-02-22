package com.parabolagames.glassmaze.classic.glass

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions.*
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Pool.Poolable
import com.google.common.base.Preconditions.checkState
import com.parabolagames.glassmaze.classic.*
import com.parabolagames.glassmaze.framework.TableActor
import com.parabolagames.glassmaze.framework.run2
import com.parabolagames.glassmaze.shared.*
import com.parabolagames.glassmaze.shared.crack.GenericGlassCrackedPieceActor
import kotlin.properties.Delegates

internal abstract class GenericCandyGlassBallActor(private val loopAnimation: Boolean)
    : TableActor(), IClassicBreakable, IBreakableInGrid, Poolable, IGenericCandyActorEffectManager {

    private val clickListener: ClickListener = ClickListener()

    private lateinit var soundPlayer: SoundPlayer
    private var worldForCandy: World? = null
    private var mainStage0: Stage? = null
    private var mainStage1: Stage? = null
    private lateinit var assets: Assets
    private var isCracked = false
    private lateinit var data: GenericCandyGlassBallActorData
    private var isMarkedForBreak = false
    private var soundWillBePlayed = false
    private lateinit var candyInGlass: TableActor
    private var candyGainer: ICandyGainer? = null
    private var handCounter: ICandyGlassHandActor? = null
    private lateinit var ringActor: RingActor
    private var clickable = false
    private lateinit var candyThrown: GenericCandyActor
    private var crackPadX = 0f
    private var crackPadY = 0f
    private var mode: ExplanationOrGame? = null
    private var row by Delegates.notNull<Int>()
    private var col by Delegates.notNull<Int>()
    private var candyCounterPositionProvider: ICandyCounterPositionProvider? = null
    protected abstract fun getData(): GenericCandyGlassBallActorData
    override var candyEffectShouldDraw = false
    private val velocityAngle = Vector2()

    init {
        addListener(clickListener)
    }

    fun initPoolForGame(
            assets: Assets,
            mainStage0: Stage,
            mainStage1: Stage,
            soundPlayer: SoundPlayer,
            world: World,
            candyGainer: ICandyGainer,
            handCounter: ICandyGlassHandActor,
            row: Int,
            col: Int,
            xGridActor: Float,
            yGridActor: Float,
            candyCounterPositionProvider: ICandyCounterPositionProvider) {
        this.candyCounterPositionProvider = candyCounterPositionProvider
        this.soundPlayer = soundPlayer
        this.worldForCandy = world
        this.mainStage0 = mainStage0
        this.mainStage1 = mainStage1
        this.assets = assets
        this.candyGainer = candyGainer
        this.handCounter = handCounter
        this.mode = ExplanationOrGame.GAME
        this.row = row
        this.col = col

        initialize(assets, row, col, xGridActor, yGridActor, GridActor.CELL_SIZE, offset)

        (candyEffect as CandyParticleEffect).setTarget(candyCounterPositionProvider)
    }

    fun initPoolForExplanation(
            assets: Assets,
            mainStage0: Stage,
            mainStage1: Stage,
            soundPlayer: SoundPlayer,
            worldForCandy: World,
            candyGainer: ICandyGainer,
            handCounter: ICandyGlassHandActor,
            row: Int,
            col: Int,
            xGridActor: Float,
            yGridActor: Float,
            cellSize: Float,
            crackPadX: Float,
            crackPadY: Float) {
        this.crackPadX = crackPadX
        this.crackPadY = crackPadY
        this.mode = ExplanationOrGame.EXPLANATION
        this.soundPlayer = soundPlayer
        this.worldForCandy = worldForCandy
        this.mainStage0 = mainStage0
        this.mainStage1 = mainStage1
        this.assets = assets
        this.candyGainer = candyGainer
        this.handCounter = handCounter
        this.row = row
        this.col = col
        initialize(
                assets,
                row,
                col,
                xGridActor,
                yGridActor,
                cellSize,
                GenericGlassBallActor.INSETS_FOR_EXP_DIALOG)
    }

    private fun initialize(
            assets: Assets,
            row: Int,
            col: Int,
            xGridActor: Float,
            yGridActor: Float,
            cellSize: Float,
            offset: Float) {
        if (!::ringActor.isInitialized) {
            ringActor = RingActor(assets, x + width / 2, y + height / 2)
        }

        if (!::data.isInitialized) {
            data = getData()
        }

        if (!::candyThrown.isInitialized) {
            candyThrown = GenericCandyActor(assets, data.candyAtlas, data.size, loopAnimation, data.frameDurationSupplier, this)
        }
        if (animation == null) {
            loadTexture(assets.getTexture(Assets.GLASS2))
            initCandyEffect(assets)
        }
        setSize(cellSize - offset, cellSize - offset)
        setPosition(xGridActor + cellSize * col + offset / 2, yGridActor + cellSize * row + offset / 2)

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
            }.also {
                addActor(it)
            }
        }

        with(candyInGlass) {
            setSize(cellSize / data.candyInGlassSizeRatio, cellSize / data.candyInGlassSizeRatio)
            setOrigin(width / 2, height / 2)
            setPosition(this@GenericCandyGlassBallActor.width / 2 - width / 2, this@GenericCandyGlassBallActor.height / 2 - height / 2)
        }
    }

    protected abstract fun initCandyEffect(assets: Assets)

    override fun reset() {
        poolReset()
        clearActions()
        unMarkAndDisableForBreakable()
        crackPadX = 0f
        crackPadY = 0f
        isCracked = false
        isMarkedForBreak = false
        mainStage0 = null
        mainStage1 = null
        worldForCandy = null
        handCounter = null
        candyGainer = null

        candyEffectShouldDraw = false
        setColor(color.r, color.g, color.b, 1f)
        isVisible = true
    }

    override val centerX: Float
        get() = x + width / 2
    override val centerY: Float
        get() = y + height / 2
    override val radius: Float
        get() = width / 2

    override fun act(dt: Float) {
        super.act(dt)
        if (!isCracked && ((clickListener.isPressed && clickable && handCounter!!.isBreakingEnabled) || isMarkedForBreak)) {
            isCracked = true
            soundWillBePlayed = true
            if (clickListener.isPressed) {
                handCounter!!.decrementHandCount()
            }
            breakTheGlass()
        }
    }

    override fun breakTheGlass() {
        if (soundWillBePlayed) {
            soundPlayer.playRandomBreak()
        }

        GenericGlassCrackedPieceActor.createAllPiecesWithSplit(
                x + crackPadX,
                y + crackPadY,
                assets,
                width,
                GenericGlassBallCrackedPieceActorData.GLASS_2,
                GenericGlassBallActor.DEFAULT_FADE_OUT_TIME,
                GenericGlassBallActor.DEFAULT_DELAY_TIME,
                mainStage0!!,
                if (mode == ExplanationOrGame.EXPLANATION) 3f else 1.toFloat(),
                if (mode == ExplanationOrGame.EXPLANATION) 400f else 1.toFloat())

        if (mode == ExplanationOrGame.GAME) {
            candyThrown.setAnimationAlignedFromActor(candyInGlass)
            candyThrown.reInit(
                    x + width / 2 - data.size / 2,
                    y + height / 2 - data.size / 2,
                    worldForCandy!!)
        } else {
            checkState(mode == ExplanationOrGame.EXPLANATION)
            candyThrown.setAnimationAlignedFromActor(candyInGlass)
            val coordinateForAll = Vector2(x, y)
            parent.localToScreenCoordinates(coordinateForAll)
            mainStage1!!.screenToStageCoordinates(coordinateForAll)
            candyThrown.reInit(coordinateForAll.x, coordinateForAll.y, worldForCandy!!)
        }

        candyThrown.setVelocity(MathUtils.random(-1f, 1f), MathUtils.random(1.8f, 2.3f))

        mainStage1!!.addActor(candyThrown)
        if (mode == ExplanationOrGame.GAME) {
            candyGainer!!.candyGlassCrackedButNotGained(row, col)
            candyThrown.addAction(forever(run2 {
                if (candyEffectShouldDraw) {
                    candyEffect.emitters.forEach {
                        it.transparency.setHigh(candyThrown.color.a / 4)
                        it.transparency.setLow(it.transparency.highMin)
                        it.setPosition(candyThrown.x + candyThrown.width / 2, candyThrown.y + candyThrown.height / 2)
                        it.scaleSize(candyThrown.color.a)
                        it.xScale.setHigh(0.22f * candyThrown.color.a * candyThrown.color.a)
                        it.xScale.setLow(it.xScale.highMin)
                        it.yScale.setHigh(it.xScale.highMin)
                        it.yScale.setLow(it.xScale.highMin)
                        it.update(Gdx.app.graphics.deltaTime)
                    }
                }
            }))

            val candyGainerForCandy = candyGainer
            candyThrown.addAction(sequence(
                    scaleBy(0.5f, 0.5f, 1f),
                    run2 { candyThrown.separateFromBox2d() },
                    parallel(
                            moveTo(candyCounterPositionProvider!!.posX - candyThrown.width / 2, candyCounterPositionProvider!!.posY, 1f),
                            sequence(delay(0.5f), fadeOut(0.5f)),
                            scaleBy(-0.5f, -0.5f, 0.5f),
                            run2 {
                                velocityAngle.set(candyCounterPositionProvider!!.posX - (candyThrown.x + candyThrown.width / 2),
                                        candyCounterPositionProvider!!.posY - (candyThrown.y + candyThrown.height / 2))
                                candyEffect.emitters.forEach {
                                    it.velocity.setHigh(8f)
                                    it.velocity.setLow(it.velocity.highMin)
                                    it.angle.setHigh(velocityAngle.angleDeg())
                                    it.angle.setLow(it.angle.highMin)
                                }

                                candyEffect.start()
                                candyEffectShouldDraw = true
                            }
                    ),
                    run2 {
                        candyGainerForCandy!!.candyGained(data, row, col)
                    },
                    run2 {
                        candyThrown.remove()
                        candyEffectShouldDraw = false
                        candyEffect.reset(true)
                        remove()
                    }
            ))

            ringActor.remove()
            handCounter!!.makePassive()
            isVisible = false

        } else {
            checkState(mode == ExplanationOrGame.EXPLANATION)
            candyThrown.addAction(sequence(delay(EXPLANATION_CANDY_REMOVAL_DELAY),
                    run2 {
                        candyThrown.remove()
                        candyThrown.destroyBody()
                        candyThrown.clearActions()
                        Gdx.app.debug(TAG, "candyThrown in explanation removed")
                    }))

            ringActor.remove()
            handCounter!!.makePassive()
            remove()
        }
    }

    override fun markForBreak(soundWillBePlayed: Boolean) {
        isMarkedForBreak = true
    }

    override fun markAndEnableForBreakable() {
        if (parent == null || !isVisible) {
            return
        }
        clickable = true

        if (mode == ExplanationOrGame.GAME) {
            ringActor.reset()
            ringActor.poolInit(
                    assets, x + width / 2, y + height / 2, RingActor.SIZE)
            mainStage1!!.addActor(ringActor)
        } else {
            checkState(mode == ExplanationOrGame.EXPLANATION)
            ringActor.reset()
            ringActor.poolInit(
                    assets,
                    0 + width / 2,
                    0 + height / 2,
                    RingActor.SIZE * Constants.DIALOG_SIZE_RATIO)
            addActor(ringActor)
        }
    }

    override fun unMarkAndDisableForBreakable() {
        clickable = false
        with(ringActor) {
            remove()
            resetAnimation()
        }
    }

    companion object {
        const val EXPLANATION_CANDY_REMOVAL_DELAY = 1f
        const val offset = 0.01f
        private const val TAG = "com.parabolagames.glassmaze.GenericCandyGlassBallActor"
    }
}