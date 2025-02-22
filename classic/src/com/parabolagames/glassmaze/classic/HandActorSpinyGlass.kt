package com.parabolagames.glassmaze.classic

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
import com.parabolagames.glassmaze.shared.IClassicMazeSpinyHandPersistenceManager
import com.parabolagames.glassmaze.shared.ui.GM_Container
import dagger.Lazy
import javax.inject.Named

internal class HandActorSpinyGlass
private constructor(
        private val assets: Assets,
        listener: Lazy<IHandActorListenerClassicMode>,
        glassSize: Float,
        private val levelNumberProvider: ILevelNumberProvider?,
        private val noHandCountListener: Lazy<INoHandCountListener>,
        private val dataPersistenceManager: IClassicMazeSpinyHandPersistenceManager?,
        @param:Named(ClassicModeModule2.CLASSIC_MAIN_STAGE_0) private val mainStage0: Stage?)
    : Group(),
        ISpinyGlassHandActor,
        IHandActorControlForExplanationDialog,
        ISpinyHandActorBonusActorInterface {
    private val handActorImpl: HandActorImpl
    private val spinyBall: SpinyBallActor
    private val clickListener: ClickListener
    private val listener: Lazy<IHandActorListenerClassicMode>
    private var infiniteMode = false
    private lateinit var count: GM_Container<Label>
    override var handCount
        set(value) {
            if (!infiniteMode) {
                dataPersistenceManager!!.classicSpinyGlassHandNumberCount = value
            }
        }
        get() = if (infiniteMode) INVALID_HAND_COUNT else dataPersistenceManager!!.classicSpinyGlassHandNumberCount


    override val isBreakingEnabled: Boolean
        get() = infiniteMode || handCount > 0

    private var actionFinished = true

    override fun giftGained() {

        if (dataPersistenceManager!!.classicSpinyGlassHandNumberCount != Constants.INFINITY) {
            dataPersistenceManager!!.classicSpinyGlassHandNumberCount++

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
        spinyBall = SpinyBallActor(assets, glassSize * 1.25f).apply { isAnimationPaused = true }
        addActor(spinyBall)
        handActorImpl = HandActorImpl(assets, glassSize * 1.25f)
        addActor(handActorImpl)
        handActorImpl.setPosition(spinyBall.width - handActorImpl.width / 2, -handActorImpl.height)
        clickListener = object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                if (handCount == 0) {
                    noHandCountListener.get().handCountIsZeroAndUserPressedToHandSpinyOrCandy()
                } else {
                    if (!spinyBall.isAnimationPaused) {
                        stopAnimation()
                    } else {
                        startAnimation()
                    }
                }
            }
        }
        addListener(clickListener)
        this.listener = listener
        isVisible = levelNumberProvider?.spinyGlassCount != 0
    }

    override fun positionChanged() {
        mainStage0?.let {
            mainStage0Coordinates.set(0f + spinyBall.width / 2, spinyBall.height / 2)
            localToScreenCoordinates(mainStage0Coordinates)
            it.screenToStageCoordinates(mainStage0Coordinates)
        }
    }


    override fun startAnimation() {
        spinyBall.isAnimationPaused = false
        listener.get().handSpinyIsActive()
        spinyBall.addAction(
                forever(sequence(scaleBy(0.2f, 0.2f, 0.4f),
                        scaleBy(-0.2f, -0.2f, 0.4f))))
    }

    override fun stopAnimation() {
        spinyBall.isAnimationPaused = true
        listener.get().handSpinyIsPassive()
        spinyBall.clearActions()
        spinyBall.setScale(1f)
    }

    private fun initializeForClassicMode(): HandActorSpinyGlass {
        handActorImpl.initializeForClassicMode(assets)
        handActorImpl.addActor(count)
        checkHandCountFor0()
        return this
    }

    override fun makePassive() {
        if (spinyBall.isAnimationPaused) {
            return
        }
        stopAnimation()
    }

    override fun updateFromDataPersistenceManager() {
        count.actor.setText(if (handCount == Constants.INFINITY) Constants.INFINITY_SYMBOL else "$handCount")
        infiniteMode = handCount == Constants.INFINITY
        checkHandCountFor0()
    }

    override fun decrementHandCount(): Int {
        checkForZeroSpinyGlass()
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


    private class SpinyBallActor(assets: Assets, glassSize: Float) : TableActor() {
        init {
            loadAnimationFromTextureRegions(
                    assets.getTexturesFromTextureAtlas(Assets.SPINY_GLASS_ATLAS), 0.025f, true)
            setSize(glassSize, glassSize)
            setOrigin(width / 2, height / 2)
        }
    }

    private class DummyHandActorCandyGlassImplementor : IHandActorListenerClassicMode, Lazy<IHandActorListenerClassicMode>, INoHandCountListener, IClassicMazeSpinyHandPersistenceManager {
        override fun get(): IHandActorListenerClassicMode = INSTANCE

        override fun handSpinyIsActive() {}
        override fun handCandyGlassIsActive() {}
        override fun handCandyGlassIsPassive() {}
        override fun handSpinyIsPassive() {}

        companion object {
            var INSTANCE = DummyHandActorCandyGlassImplementor()
        }

        override fun handCountIsZeroAndUserPressedToHandSpinyOrCandy() {}
        override var classicSpinyGlassHandNumberCount: Int
            get() = 5
            set(value) {}
    }

    private class DummyHandActorCandyGlassImplementor2 : Lazy<INoHandCountListener> {
        override fun get(): INoHandCountListener = DummyHandActorCandyGlassImplementor.INSTANCE

        companion object {
            var INSTANCE = DummyHandActorCandyGlassImplementor2()
        }
    }

    internal inner class HandActorImpl(assets: Assets, height: Float) : TableActor() {
        init {
            loadTexture(assets.getTexture(Assets.HAND_SPINY_GLASS))
            setSize(height * (this@HandActorImpl.width / getHeight()), height)
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
            checkForZeroSpinyGlass()
        }
    }

    private fun checkForZeroSpinyGlass() {
        if (levelNumberProvider?.spinyGlassCountCurrent == 0) {
            addAction(sequence(fadeOut(1f)))
            removeListener(clickListener)
        }
    }

    companion object {
        private const val INVALID_HAND_COUNT: Int = -100
        private const val GLASS_SIZE = 0.20f
        fun createHandActorForClassicMode(
                assets: Assets,
                dataPersistenceManager: IClassicMazePersistenceManager,
                listener: Lazy<IHandActorListenerClassicMode>,
                levelNumberProvider: ILevelNumberProvider?,
                noHandCountListener: Lazy<INoHandCountListener>,
                mainStage0: Stage): HandActorSpinyGlass = HandActorSpinyGlass(
                assets,
                listener, GLASS_SIZE,
                levelNumberProvider,
                noHandCountListener,
                dataPersistenceManager,
                mainStage0)
                .initializeForClassicMode()

        @JvmStatic
        fun createHandActorForHowToPlay(assets: Assets, cellSize: Float): HandActorSpinyGlass = HandActorSpinyGlass(
                assets,
                DummyHandActorCandyGlassImplementor.INSTANCE,
                cellSize,
                null,
                DummyHandActorCandyGlassImplementor2.INSTANCE,
                DummyHandActorCandyGlassImplementor.INSTANCE,
                null)
                .initializeForClassicMode()
    }
}