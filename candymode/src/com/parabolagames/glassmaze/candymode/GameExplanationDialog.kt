package com.parabolagames.glassmaze.candymode

import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Action
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.actions.Actions.*
import com.badlogic.gdx.scenes.scene2d.ui.Dialog
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Pools
import com.google.common.base.Supplier
import com.parabolagames.glassmaze.candymode.glass.*
import com.parabolagames.glassmaze.framework.IDialogDoesNotPauseResume
import com.parabolagames.glassmaze.framework.TableActor
import com.parabolagames.glassmaze.framework.labelStyle
import com.parabolagames.glassmaze.framework.run2
import com.parabolagames.glassmaze.shared.Assets
import com.parabolagames.glassmaze.shared.Constants
import com.parabolagames.glassmaze.shared.GenericGlassBallCrackedPieceActorData
import com.parabolagames.glassmaze.shared.SoundPlayer
import com.parabolagames.glassmaze.shared.ui.DialogStyle
import com.parabolagames.glassmaze.shared.ui.GM_Container
import javax.inject.Inject

internal class GameExplanationDialog
private constructor(private val assets: Assets,
                    private val soundPlayer:
                    SoundPlayer, private val
                    closeAction: Runnable)
    : Dialog("How To Play", DialogStyle(assets)), IDialogDoesNotPauseResume {

    private var willClose = false
    private var rulePageIndex = 0
    private lateinit var lblContainer: GM_Container<Label>
    private lateinit var continueLabelAction: Action
    private lateinit var candyGlass: GenericCandyGlassBallActor
    private lateinit var handActor: HandActor
    private val dummyWorld = World(Vector2(0f, -2.8f), true)

    init {
        initialize()
        addListener(
                object : InputListener() {
                    override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                        if (willClose) {
                            closeAction.run()
                            contentTable.clear()
                            hide()
                            removeListener(this)
                        } else {
                            insertNextRule()
                        }
                        return super.touchDown(event, x, y, pointer, button)
                    }

                    override fun keyDown(event: InputEvent, keycode: Int): Boolean {
                        if (keycode == Input.Keys.BACK || keycode == Input.Keys.DEL || keycode == Input.Keys.SPACE) {
                            closeAction.run()
                            contentTable.clear()
                            hide()
                            removeListener(this)
                        }
                        return super.keyDown(event, keycode)
                    }
                })
    }

    private fun initialize() {
        handActor = HandActor(assets)
        titleLabel.setFontScale(3.2f)
        titleTable.getCell(titleLabel).padTop(-0.1f * Constants.DIALOG_SIZE_RATIO)
        addContinueLabel()
        insertNextRule()
    }

    private fun insertNextRule() {
        contentTable.clear()
        with(handActor) {
            clearActions()
            setPosition(0f, 0f)
            setColor(1f, 1f, 1f, 0f)
        }

        continueLabelAction.restart()
        when (rulePageIndex) {
            0 -> addRule0()
            1 -> addRule1()
            2 -> addRule2()
            3 -> addRule3()
            else -> throw IllegalStateException("Undefined Page Number = $rulePageIndex")
        }
        rulePageIndex++
        if (rulePageIndex == 4) {
            willClose = true
        }
    }

    override fun hide(action: Action?) {
        super.hide(action)
        dummyWorld.dispose()
    }

    private fun addRule0() {

        val throwableBall = createGenericThrowableBallActor1(dummyWorld, 0.2f, 0.6f, 1f)
        contentTable.addActor(throwableBall)
        throwableBall.addAction(sequence(alpha(0f), fadeIn(0.4f, Interpolation.fade)))
        val candyGlassSize = throwableBall.width * 2
        candyGlass = Pools.get(CandyGlassBall2Actor::class.java).obtain().initPoolForExplanation(assets,
                null,
                null,
                soundPlayer,
                1.6f * Constants.DIALOG_SIZE_RATIO,
                1.7f * Constants.DIALOG_SIZE_RATIO,
                dummyWorld,
                DummyCandyGlassInterfaceImplementor.INSTANCE,
                DummyCandyGlassInterfaceImplementor.INSTANCE,
                DummyCandyGlassInterfaceImplementor.INSTANCE,
                candyGlassSize)
                .apply {
                    addAction(sequence(alpha(0f), fadeIn(0.4f, Interpolation.fade)))
                    getChild(0).addAction(sequence(alpha(0f), fadeIn(0.4f, Interpolation.fade)))
                }

        contentTable.addActor(candyGlass)
        contentTable.addActor(handActor)

        contentTable
                .add(Label("Glass Balls Break\nGlass Balls", labelStyle(assets.getBitmapFont(Assets.FONT_COSMIC_SANS_ORANGE)))
                        .apply {
                            setFontScale(3f)
                            setAlignment(Align.center)
                        })
                .pad(LABEL_PAD)
                .padBottom(0.1f * Constants.DIALOG_SIZE_RATIO)
                .padTop(2f * Constants.DIALOG_SIZE_RATIO)

        throwableBall.addAction(
                createThrowableAnimationAction(
                        { createGenericThrowableBallActor1(dummyWorld, 0.55f, 0.9f, 0.0f) },
                        { createGenericThrowableBallActor1(dummyWorld, 0.9f, 1.2f, 0f) }
                ) { createGenericThrowableBallActor1(dummyWorld, 1.25f, 1.5f, 0.0f) })
    }

    private fun addRule1() {
        val ironBall = createIronBallActor(dummyWorld, 0.2f, 0.6f, 1f)
        contentTable.addActor(ironBall)
        val candyGlassSize = ironBall.width * 2
        val glassBall = GenericGlassBallActor.GENERIC_GLASS_BALL_ACTOR_POOL.obtain().poolInitExplanation(
                assets,
                null,
                soundPlayer,
                1.4f * Constants.DIALOG_SIZE_RATIO, 1.85f * Constants.DIALOG_SIZE_RATIO,
                dummyWorld,
                candyGlassSize * 0.8f,
                GenericGlassBallCrackedPieceActorData.GLASS_4,
                DummyCandyGlassInterfaceImplementor.INSTANCE,
                DummyCandyGlassInterfaceImplementor.INSTANCE)
        contentTable.addActor(glassBall)

        candyGlass = Pools.get(CandyGlassBall2Actor::class.java).obtain().initPoolForExplanation(assets,
                null,
                null,
                soundPlayer,
                1.6f * Constants.DIALOG_SIZE_RATIO,
                1.7f * Constants.DIALOG_SIZE_RATIO,
                dummyWorld,
                DummyCandyGlassInterfaceImplementor.INSTANCE,
                DummyCandyGlassInterfaceImplementor.INSTANCE,
                DummyCandyGlassInterfaceImplementor.INSTANCE,
                candyGlassSize)
                .apply {
                    addAction(sequence(alpha(0f), fadeIn(0.4f, Interpolation.fade)))
                    getChild(0).addAction(sequence(alpha(0f), fadeIn(0.4f, Interpolation.fade)))
                }
        contentTable.addActor(candyGlass)

        val spinyBall = SpinyBallActor.SPINY_BALL_ACTOR_POOL.obtain().poolInitExplanation(assets,
                null,
                soundPlayer,
                1.65f * Constants.DIALOG_SIZE_RATIO,
                1.25f * Constants.DIALOG_SIZE_RATIO,
                dummyWorld,
                candyGlassSize)
        contentTable.addActor(spinyBall)
        contentTable.addActor(handActor)

        contentTable
                .add(Label("Iron Balls \nAre Powerful !", labelStyle(assets.getBitmapFont(Assets.FONT_COSMIC_SANS_ORANGE)))
                        .apply {
                            setFontScale(3f)
                            setAlignment(Align.center)
                        })
                .pad(LABEL_PAD)
                .padBottom(0.1f * Constants.DIALOG_SIZE_RATIO)
                .padTop(2f * Constants.DIALOG_SIZE_RATIO)
        ironBall.addAction(
                createThrowableAnimationAction(
                        { createIronBallActor(dummyWorld, 0.55f, 0.9f, 0.0f) },
                        { createIronBallActor(dummyWorld, 0.9f, 1.2f, 0f) }
                ) { createIronBallActor(dummyWorld, 1.25f, 1.5f, 0.0f) })
    }

    private fun addRule2() {
        val glassBall = GenericGlassBallActor.GENERIC_GLASS_BALL_ACTOR_POOL.obtain().poolInitExplanation(
                assets,
                null,
                soundPlayer,
                0.85f * Constants.DIALOG_SIZE_RATIO, 1f * Constants.DIALOG_SIZE_RATIO,
                dummyWorld,
                0.8f * Constants.DIALOG_SIZE_RATIO,
                GenericGlassBallCrackedPieceActorData.GLASS_2,
                DummyCandyGlassInterfaceImplementor.INSTANCE,
                DummyCandyGlassInterfaceImplementor.INSTANCE)
        contentTable.addActor(glassBall)
        handActor.apply {
            rotation = 0f
            setScale(1.6f)
            setPosition(1f * Constants.DIALOG_SIZE_RATIO, 0.5f * Constants.DIALOG_SIZE_RATIO)
            setColor(1f, 1f, 1f, 0f)
            addAction(alpha(HandActor.ALPHA, 2f))
        }.also { contentTable.addActor(it) }

        contentTable
                .add(Label("Touch the Glass Balls\n To Break", labelStyle(assets.getBitmapFont(Assets.FONT_COSMIC_SANS_ORANGE)))
                        .apply {
                            setFontScale(3f)
                            setAlignment(Align.center)
                        })
                .pad(LABEL_PAD)
                .padBottom(0.1f * Constants.DIALOG_SIZE_RATIO)
                .padTop(2f * Constants.DIALOG_SIZE_RATIO)
    }

    private fun addRule3() {
        contentTable
                .add(Label("Never Break\nFire Balls !", labelStyle(assets.getBitmapFont(Assets.FONT_COSMIC_SANS_ORANGE)))
                        .apply {
                            setFontScale(3f)
                            setAlignment(Align.center)
                        })
                .pad(LABEL_PAD)
                .padBottom(0.1f * Constants.DIALOG_SIZE_RATIO)
                .padTop(2f * Constants.DIALOG_SIZE_RATIO)
    }

    private fun addContinueLabel() {
        lblContainer = GM_Container(Label("Tap To Continue", labelStyle(assets.getBitmapFont(Assets.FONT_COMIC_SANS_MS)))
                .apply {
                    setFontScale(2f)
                    setAlignment(Align.center)
                }, sizeCoefficient = Constants.DIALOG_SIZE_RATIO)
                .apply {
                    addAction(
                            forever(sequence(delay(2f),
                                    scaleBy(0.1f, 0.1f, 0.2f),
                                    scaleBy(-0.1f, -0.1f, 0.2f),
                                    scaleBy(0.1f, 0.1f, 0.2f),
                                    scaleBy(-0.1f, -0.1f, 0.2f))
                                    .also {
                                        continueLabelAction = it
                                    }))
                }.also { buttonTable.add(it) }
    }

    private fun createGenericThrowableBallActor1(
            world: World, x: Float, y: Float, alpha: Float): ThrowableGlassBallActor = ThrowableGlassBallActor.createForUI(
            x * Constants.DIALOG_SIZE_RATIO,
            y * Constants.DIALOG_SIZE_RATIO,
            assets,
            soundPlayer,
            GenericGlassBallCrackedPieceActorData.GLASS_3, world, ThrowableBallActor.BALL_RADIUS * Constants.DIALOG_SIZE_RATIO)
            .apply {
                color = Color(1f, 1f, 1f, alpha)
            }

    private fun createThrowableAnimationAction(
            ball1Supplier: Supplier<ThrowableBallActor>,
            ball2Supplier: Supplier<ThrowableBallActor>,
            ball3Supplier: Supplier<ThrowableBallActor>): Action {
        val handActorX = handActor.x
        val handActorY = handActor.y
        return forever(
                sequence(
                        delay(1f),
                        parallel(
                                alpha(0.1f, 1f),
                                sequence(
                                        run2 {
                                            handActor.addAction(alpha(HandActor.ALPHA))
                                            handActor.addAction(
                                                    moveTo(
                                                            0.5f * Constants.DIALOG_SIZE_RATIO,
                                                            0.5f * Constants.DIALOG_SIZE_RATIO,
                                                            1.5f))
                                            handActor.addAction(fadeOut(2f))
                                            val ball1 = ball1Supplier.get()
                                            ball1.addAction(
                                                    sequence(
                                                            alpha(0.3f, 1f),
                                                            delay(1f),
                                                            fadeOut(0.5f),
                                                            removeActor(),
                                                            run2 { ball1.destroyBody() }))
                                            contentTable.addActorAt(handActor.zIndex - 1, ball1)
                                        },
                                        delay(0.2f),
                                        run2 {
                                            val ball1 = ball2Supplier.get()
                                            ball1.addAction(
                                                    sequence(
                                                            alpha(0.55f, 1f),
                                                            delay(1f),
                                                            fadeOut(0.5f),
                                                            removeActor(),
                                                            Actions.run { ball1.destroyBody() }))
                                            contentTable.addActorAt(handActor.zIndex - 1, ball1)
                                        },
                                        delay(0.2f),
                                        run2 {
                                            val ball1 = ball3Supplier.get()
                                            ball1.addAction(
                                                    sequence(
                                                            alpha(0.8f, 1f),
                                                            delay(1f),
                                                            fadeOut(0.5f),
                                                            removeActor(),
                                                            Actions.run { ball1.destroyBody() }))
                                            contentTable.addActorAt(handActor.zIndex - 1, ball1)
                                        })),
                        delay(2f),
                        run2 {
                            with(handActor) {
                                clearActions()
                                setColor(1f, 1f, 1f, 0f)
                                setPosition(handActorX, handActorY)
                            }
                        },
                        fadeIn(1f)))
    }

    private fun createIronBallActor(world: World, x: Float, y: Float, alpha: Float): ThrowableIronBallActor =
            ThrowableIronBallActor.createForUI(x * Constants.DIALOG_SIZE_RATIO,
                    y * Constants.DIALOG_SIZE_RATIO,
                    assets,
                    world,
                    ThrowableBallActor.BALL_RADIUS * Constants.DIALOG_SIZE_RATIO)
                    .apply {
                        setColor(1f, 1f, 1f, alpha)
                    }

    internal class GameExplanationDialogFactory @Inject constructor(private val assets: Assets, private val soundPlayer: SoundPlayer) {
        fun create(closeAction: Runnable) = GameExplanationDialog(assets, soundPlayer, closeAction)
    }

    private class HandActor(assets: Assets) : TableActor() {
        init {
            loadTexture(assets.getTexture(Assets.HAND_EXPLANATION))
            setOrigin(width / 2, height / 2)
            rotation = 45f
            setColor(1f, 1f, 1f, 0f)
        }

        companion object {
            const val ALPHA = 0.4f
        }
    }

    private class DummyCandyGlassInterfaceImplementor : ICandyGainer, IHandCounter, IGlassMissedListener {
        override fun glassMissed(xWorld: Float) {
            // NO  OP
        }

        override fun decrementHandCount(): Long {
            return 0
        }

        override var handCount: Long
            get() = 0
            set(handCount) {}

        override fun pressedWhenHandCountIsZero() {
            // NO  OP
        }

        override fun candyGained(candyData: GenericCandyGlassBallActorData) {
            // NO  OP
        }

        companion object {
            val INSTANCE = DummyCandyGlassInterfaceImplementor()
        }
    }

    companion object {
        private const val LABEL_PAD = 0.30f * Constants.DIALOG_SIZE_RATIO
    }
}