package com.parabolagames.glassmaze.classic

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions.*
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import com.parabolagames.glassmaze.framework.TableActor
import com.parabolagames.glassmaze.framework.labelStyle
import com.parabolagames.glassmaze.framework.run2
import com.parabolagames.glassmaze.shared.Assets
import com.parabolagames.glassmaze.shared.Constants
import com.parabolagames.glassmaze.shared.IClassicMazeCandyHandPersistenceManager
import com.parabolagames.glassmaze.shared.ui.GM_Container
import dagger.Lazy
import javax.inject.Named

internal class HandActorCandyGlass
private constructor(
        private val assets: Assets,
        listener: Lazy<IHandActorListenerClassicMode>,
        glassSize: Float,
        private val noHandCountListener: Lazy<INoHandCountListener>,
        private val dataPersistenceManager: IClassicMazeCandyHandPersistenceManager?,
        @param:Named(ClassicModeModule2.CLASSIC_MAIN_STAGE_0) private val mainStage0: Stage?)
    : Group(),
        ICandyGlassHandActor,
        IHandActorControlForExplanationDialog,
        ICandyHandActorBonusActorInterface {
    private val handActorImpl: HandActorImpl
    private val candyGlassActor: CandyGlassActor
    private val clickListener: ClickListener
    private var infiniteMode = false

    private lateinit var count: GM_Container<Label>

    override var handCount: Int
        set(value) {
            if (!infiniteMode) {
                dataPersistenceManager!!.classicCandyGlassHandNumberCount = value
            }
        }
        get() = if (infiniteMode) INVALID_HAND_COUNT else dataPersistenceManager!!.classicCandyGlassHandNumberCount

    private val listener: Lazy<IHandActorListenerClassicMode>
    override val isBreakingEnabled: Boolean
        get() = infiniteMode || handCount > 0

    private var actionFinished = true

    override fun giftGained() {
        if (dataPersistenceManager!!.classicCandyGlassHandNumberCount != Constants.INFINITY) {
            dataPersistenceManager!!.classicCandyGlassHandNumberCount++

            count.clearActions()
            count.actor.setText("$handCount")
            count.addAction(
                    sequence(
                            run2 { actionFinished = false },
                            scaleBy(0.2f, 0.2f, 0.1f),
                            scaleBy(-0.2f, -0.2f, 0.1f),
                            scaleBy(0.4f, 0.2f, 0.1f),
                            scaleBy(-0.4f, -0.2f, 0.1f),
                            run2() { actionFinished = true }))
        }
    }

    override val posX: Float
        get() = mainStage0Coordinates.x
    override val posY: Float
        get() = mainStage0Coordinates.y

    private val mainStage0Coordinates = Vector2()

    init {
        candyGlassActor = CandyGlassActor(assets, glassSize).apply { candyActor.isAnimationPaused = true }
        addActor(candyGlassActor)
        handActorImpl = HandActorImpl(assets, glassSize * 1.25f)
        addActor(handActorImpl)
        handActorImpl.setPosition(candyGlassActor.width - handActorImpl.width / 2, -handActorImpl.height)
        clickListener = object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                if (handCount == 0) {
                    noHandCountListener.get().handCountIsZeroAndUserPressedToHandSpinyOrCandy()
                } else {
                    if (!candyGlassActor.candyActor.isAnimationPaused) {
                        stopAnimation()
                    } else {
                        startAnimation()
                    }
                }
            }
        }
        addListener(clickListener)
        this.listener = listener
    }

    val bottomPosition: Float
        get() = handActorImpl.y

    override fun positionChanged() {
        mainStage0?.let {
            mainStage0Coordinates.set(0f + candyGlassActor.width / 2, 0f)
            localToScreenCoordinates(mainStage0Coordinates)
            it.screenToStageCoordinates(mainStage0Coordinates)
        }
    }

    override fun startAnimation() {
        candyGlassActor.candyActor.isAnimationPaused = false
        listener.get().handCandyGlassIsActive()
        candyGlassActor.addAction(
                forever(sequence(scaleBy(0.2f, 0.2f, 0.4f),
                        scaleBy(-0.2f, -0.2f, 0.4f))))
    }

    override fun stopAnimation() {
        candyGlassActor.candyActor.isAnimationPaused = true
        listener.get().handCandyGlassIsPassive()
        candyGlassActor.clearActions()
        candyGlassActor.setScale(1f)
    }

    private fun initializeForClassicMode(): HandActorCandyGlass {
        handActorImpl.initializeForClassicMode(assets)
        handActorImpl.addActor(count)
        checkHandCountFor0()
        return this
    }

    override fun updateFromDataPersistenceManager() {
        count.actor.setText(if (handCount == Constants.INFINITY) Constants.INFINITY_SYMBOL else "$handCount")
        infiniteMode = handCount == Constants.INFINITY
        checkHandCountFor0()
    }

    override fun decrementHandCount(): Int {
        if (infiniteMode) {
            return INVALID_HAND_COUNT
        }
        if (handCount > 0) {
            handCount--
        }
        count.actor.setText(handCount.toString())
        checkHandCountFor0()
        return handCount
    }

    private fun checkHandCountFor0() {
        if (handCount == 0) {
            handActorImpl.addAction(alpha(0.4f))
        } else {
            handActorImpl.addAction(alpha(1f))
        }
    }

    override fun makePassive() {
        if (candyGlassActor.candyActor.isAnimationPaused) {
            return
        }
        stopAnimation()
    }

    private class CandyGlassActor(assets: Assets, glassSize: Float) : TableActor() {
        var candyActor: TableActor

        init {
            loadTexture(assets.getTexture(Assets.GLASS2))
            setSize(glassSize, glassSize)
            setOrigin(width / 2, height / 2)
            candyActor = TableActor().apply {
                loadAnimationFromTextureRegions(
                        assets.getTexturesFromTextureAtlas(Assets.CANDY17_ATLAS),
                        MathUtils.random(0.015f, 0.035f),
                        true,
                        MathUtils.random(0, assets.getTexturesFromTextureAtlas(Assets.CANDY2_ATLAS).size))
                addAction(forever(sequence(
                        scaleBy(-0.1f, -0.1f, 1f),
                        scaleBy(0.1f, 0.1f, 1f))))
                setColor(0.6f, 0.6f, 0.6f, 1f)
                setSize(glassSize * 0.75f, glassSize * 0.75f)
                setOrigin(width / 2, height / 2)
                setPosition(this@CandyGlassActor.width / 2 - width / 2, this@CandyGlassActor.height / 2 - height / 2)
            }
            addActor(candyActor)
        }
    }

    private class DummyHandActorCandyGlassImplementor : IHandActorListenerClassicMode, Lazy<IHandActorListenerClassicMode>, INoHandCountListener, IClassicMazeCandyHandPersistenceManager {
        override fun handSpinyIsActive() {}
        override fun handCandyGlassIsActive() {}
        override fun handCandyGlassIsPassive() {}
        override fun handSpinyIsPassive() {}
        override fun get(): IHandActorListenerClassicMode = INSTANCE

        companion object {
            var INSTANCE = DummyHandActorCandyGlassImplementor()
        }

        override fun handCountIsZeroAndUserPressedToHandSpinyOrCandy() {}

        override var classicCandyGlassHandNumberCount: Int
            get() = 5
            set(value) {}
    }

    private class DummyHandActorCandyGlassImplementor2 : Lazy<INoHandCountListener> {
        override fun get(): INoHandCountListener = DummyHandActorCandyGlassImplementor.INSTANCE

        companion object {
            var INSTANCE = DummyHandActorCandyGlassImplementor2()
        }
    }

    internal inner class HandActorImpl constructor(assets: Assets, height: Float) : TableActor() {
        init {
            loadTexture(assets.getTexture(Assets.HAND_CANDY_GLASS))
            setWidthHeightFromHeight(height)
        }

        fun initializeForClassicMode(assets: Assets) {
            count = GM_Container(object : Label(if (handCount == Constants.INFINITY) Constants.INFINITY_SYMBOL else "$handCount",
                    labelStyle(assets.getBitmapFont(Assets.FONT_CURRENCY_MONTSERRAT))) {
                override fun sizeChanged() {
                    parent?.setPosition(handActorImpl.width, -height)
                }
            }.apply {
                setAlignment(Align.left)
                wrap = false
                setFontScale(this@HandActorImpl.height * 0.0045f)
            })
            infiniteMode = handCount == Constants.INFINITY
        }
    }

    companion object {
        private const val INVALID_HAND_COUNT: Int = -100
        private const val GLASS_SIZE = 0.20f
        fun createHandActorForClassicMode(
                assets: Assets,
                dataPersistenceManager: IClassicMazePersistenceManager,
                listener: Lazy<IHandActorListenerClassicMode>,
                noHandCountListener: Lazy<INoHandCountListener>,
                mainStage0: Stage): HandActorCandyGlass =
                HandActorCandyGlass(assets,
                        listener,
                        GLASS_SIZE,
                        noHandCountListener,
                        dataPersistenceManager,
                        mainStage0)
                        .initializeForClassicMode()

        @JvmStatic
        fun createHandActorForHowToPlay(assets: Assets,
                                        cellSize: Float): HandActorCandyGlass =
                HandActorCandyGlass(assets,
                        DummyHandActorCandyGlassImplementor.INSTANCE,
                        cellSize,
                        DummyHandActorCandyGlassImplementor2.INSTANCE,
                        DummyHandActorCandyGlassImplementor.INSTANCE,
                        null)
                        .initializeForClassicMode()
    }
}