package com.parabolagames.glassmaze.classic

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.*
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.actions.Actions.*
import com.badlogic.gdx.scenes.scene2d.ui.Dialog
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.Pools
import com.badlogic.gdx.utils.Timer
import com.google.common.base.Supplier
import com.parabolagames.glassmaze.classic.MazeBallActor.Companion.createForExplanation
import com.parabolagames.glassmaze.classic.glass.*
import com.parabolagames.glassmaze.framework.Box2DActor
import com.parabolagames.glassmaze.framework.IDialogDoesNotPauseResume
import com.parabolagames.glassmaze.framework.TableActor
import com.parabolagames.glassmaze.framework.run2
import com.parabolagames.glassmaze.shared.Assets
import com.parabolagames.glassmaze.shared.Constants
import com.parabolagames.glassmaze.shared.GenericGlassBallCrackedPieceActorData
import com.parabolagames.glassmaze.shared.SoundPlayer
import com.parabolagames.glassmaze.shared.ui.DialogStyle
import com.parabolagames.glassmaze.shared.ui.GM_Container
import javax.inject.Inject
import javax.inject.Named

internal class GameExplanationDialog private constructor(
        private val assets: Assets,
        private val soundPlayer: SoundPlayer,
        private val closeAction: Runnable,
        private val levelNumber: Int,
        private val explanationCandyStage: Stage) : Dialog("How To Play", DialogStyle(assets)), IDialogDoesNotPauseResume {
    private val worldForCandy = World(Vector2(0f, -2.8f), true)
    private var willClose = false
    private var rulePageIndex = 0
    private var continueLabelAction: Action? = null
    private lateinit var handActor: HandActor
    private lateinit var gridActor: GridActor
    private val box2dActorArray = Array<Box2DActor>()
    private val actorArray = Array<Actor>()

    init {
        addListener(
                object : InputListener() {
                    override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                        if (willClose) {
                            doBeforeCloseJobs()
                        } else {
                            insertNextRule()
                        }
                        return super.touchDown(event, x, y, pointer, button)
                    }

                    override fun keyDown(event: InputEvent, keycode: Int): Boolean {
                        if (keycode == Input.Keys.BACK || keycode == Input.Keys.DEL || keycode == Input.Keys.SPACE || keycode == Input.Keys.ESCAPE) {
                            doBeforeCloseJobs()
                        }
                        return super.keyDown(event, keycode)
                    }

                    private fun doBeforeCloseJobs() {
                        contentTable.clear()
                        clearActorArrays()
                        gridActor.resetLinesToPool()
                        closeAction.run()
                        hide()
                        removeListener(this)
                        Timer.schedule(
                                object : Timer.Task() {
                                    override fun run() {
                                        worldForCandy.dispose()
                                        Gdx.app.debug(TAG, "worlds disposed")
                                    }
                                },
                                GenericCandyGlassBallActor.EXPLANATION_CANDY_REMOVAL_DELAY + 1)
                    }
                })
    }

    override fun act(delta: Float) {
        worldForCandy.step(1 / 60f, 1, 1)
        super.act(delta)
    }

    override fun show(uiStage: Stage): Dialog {
        return try {
            super.show(uiStage)
        } finally {
            initialize()
        }
    }

    private fun initialize() {
        handActor = HandActor(assets)
        addMazeWalls()
        titleLabel.setFontScale(3.2f)
        titleTable.getCell(titleLabel).padTop(-0.1f * Constants.DIALOG_SIZE_RATIO)
        insertNextRule()
        addContinueLabel()
    }

    override fun getWidth(): Float = 2 * Constants.DIALOG_SIZE_RATIO

    override fun getHeight(): Float = 3 * Constants.DIALOG_SIZE_RATIO

    private fun insertNextRule() {
        contentTable.clear()
        clearActorArrays()
        when (levelNumber) {
            1 -> {
                when (rulePageIndex) {
                    0 -> addRule0()
                    1 -> {
                        continueLabelAction!!.restart()
                        addRule3()
                    }
                    else -> throw IllegalStateException("Undefined Page Number = $rulePageIndex")
                }
                rulePageIndex++
                if (rulePageIndex == 2) {
                    willClose = true
                }
            }
            2 -> {
                addRule2()
                willClose = true
            }
            3 -> {
                addRule1()
                willClose = true
            }
            else -> throw IllegalStateException()
        }
    }

    private fun clearActorArrays() {
        handActor.clearActions()
        handActor.remove()
        for (actor in box2dActorArray) {
            if (actor.parent != null) { // maybe it is broken
                actor.destroyBody()
                actor.remove()
            }
        }
        box2dActorArray.clear()
        for (actor in actorArray) {
            actor.remove()
        }
        box2dActorArray.clear()
    }

    private fun addMazeWalls() {
        gridActor = GridActor(GRID_ROW, GRID_COL, CELL_SIZE, 0.008f * Constants.DIALOG_SIZE_RATIO, assets)
        gridActor.setPosition(width / 2 - gridActor.width / 2, 1.2f * Constants.DIALOG_SIZE_RATIO)
        addActor(gridActor)
    }

    private fun addRule0() {
        handActor.apply {
            rotation = 0f
            setScale(1.0f)
            setPosition(gridActor.x, gridActor.y - gridActor.height / 2)
            setColor(1f, 1f, 1f, 0f)
            rotation = 45f
            addAction(alpha(0f, 2f))
        }.also {
            addActor(it)
        }

        addRule0Impl(1.25f)
        createForExplanation(
                assets,
                soundPlayer,
                0,
                2,
                gridActor.x,
                gridActor.y,
                CELL_SIZE,
                1f)
                .apply {
                    addAction(
                            createThrowableAnimationAction(
                                    {
                                        createForExplanation(
                                                assets,
                                                soundPlayer,
                                                2,
                                                2,
                                                gridActor.x,
                                                gridActor.y,
                                                CELL_SIZE,
                                                0f)
                                    },
                                    {
                                        createForExplanation(
                                                assets,
                                                soundPlayer,
                                                4,
                                                2,
                                                gridActor.x,
                                                gridActor.y,
                                                CELL_SIZE,
                                                0f)
                                    }
                            ) { addRule0Impl(2f) })
                }.also {
                    addActor(it)
                    actorArray.add(it)
                }

        contentTable
                .add(Label(
                        "Move The Ball\nUp, Down, Right, Left\n",
                        LabelStyle(assets.getBitmapFont(Assets.FONT_COSMIC_SANS_ORANGE), null))
                        .apply {
                            setFontScale(FONT_SCALE)
                            setAlignment(Align.center)
                        })
                .pad(LABEL_PAD)
                .padBottom(0.1f * Constants.DIALOG_SIZE_RATIO)
                .padTop(2f * Constants.DIALOG_SIZE_RATIO)
    }

    private fun addRule0Impl(firstDelay: Float) {
        val localCoordOfDialog = Vector2(0f, 0f)
        val stageCoordOfDialog = localToStageCoordinates(localCoordOfDialog)
        Pools.get(CandyGlassBall2Actor::class.java).obtain().apply {
            initPoolForExplanation(
                    assets,
                    this@GameExplanationDialog.stage,
                    explanationCandyStage,
                    soundPlayer,
                    worldForCandy,
                    DummyCandyGlassInterfaceImplementor.INSTANCE,
                    DummyCandyGlassInterfaceImplementor.INSTANCE,
                    3,
                    2,
                    gridActor.x,
                    gridActor.y,
                    CELL_SIZE,
                    stageCoordOfDialog.x,
                    stageCoordOfDialog.y)
            addAction(
                    sequence(
                            delay(firstDelay + 0.25f), Actions.run { markForBreak(true) }))
        }.also {
            addActor(it)
            actorArray.add(it)
        }

        Pools.get(GenericGlassBallActor::class.java).obtain().apply {
            poolInitExplanation(
                    assets,
                    soundPlayer,
                    GenericGlassBallCrackedPieceActorData.GLASS_2,
                    1,
                    2,
                    gridActor.x,
                    gridActor.y,
                    CELL_SIZE,
                    stageCoordOfDialog.x,
                    stageCoordOfDialog.y)
            addAction(
                    sequence(delay(firstDelay), Actions.run { markForBreak(true) }))
        }.also {
            addActor(it)
            actorArray.add(it)
        }
    }

    private fun addRule1() {
        val localCoordOfDialog = Vector2(0f, 0f)
        val stageCoordOfDialog = localToStageCoordinates(localCoordOfDialog)
        val candyGlassPos = Vector2()
        val handActorForHowToPlay = addHandActorAndHandActorCandyRule1()
        addHandActorAnimationForBreak(
                stageCoordOfDialog,
                candyGlassPos,
                handActorForHowToPlay)
        { addCandyGlassInRule1(stageCoordOfDialog, candyGlassPos) }

        contentTable
                .add(Label(
                        "Touch The Candy Crusher\nThen Touch \nThe Candy Glass",
                        LabelStyle(assets.getBitmapFont(Assets.FONT_COSMIC_SANS_ORANGE), null))
                        .apply {
                            setFontScale(FONT_SCALE)
                            setAlignment(Align.center)
                        })
                .pad(LABEL_PAD)
                .padBottom(0.1f * Constants.DIALOG_SIZE_RATIO)
                .padTop(2f * Constants.DIALOG_SIZE_RATIO)
    }

    private fun addRule2() {
        val localCoordOfDialog = Vector2(0f, 0f)
        val stageCoordOfDialog = localToStageCoordinates(localCoordOfDialog)
        val candyGlassPos = Vector2()
        val handActorForHowToPlay = addHandActorAndHandActorCandyRule2()
        addHandActorAnimationForBreak(
                stageCoordOfDialog,
                candyGlassPos,
                handActorForHowToPlay
        ) { addSpinyGlassInRule2(stageCoordOfDialog, candyGlassPos) }

        contentTable
                .add(Label(
                        "Touch The Spiny Crusher\nThen Touch \nThe Spiny Glass",
                        LabelStyle(assets.getBitmapFont(Assets.FONT_COSMIC_SANS_ORANGE), null)).apply {
                    setFontScale(FONT_SCALE)
                    setAlignment(Align.center)
                })
                .pad(LABEL_PAD)
                .padBottom(0.1f * Constants.DIALOG_SIZE_RATIO)
                .padTop(2f * Constants.DIALOG_SIZE_RATIO)
    }

    private fun addRule3() {
        handActor.apply {
            rotation = 0f
            setScale(1.0f)
            setPosition(gridActor.x, gridActor.y - gridActor.height / 2)
            setColor(1f, 1f, 1f, 0f)
            rotation = 45f
            addAction(alpha(0f, 2f))
        }.also { addActor(it) }


        addRule3Impl(1.25f)
        createForExplanation(
                assets,
                soundPlayer,
                0,
                2,
                gridActor.x,
                gridActor.y,
                CELL_SIZE,
                1f)
                .apply {
                    addAction(
                            createThrowableAnimationAction(
                                    {
                                        createForExplanation(
                                                assets,
                                                soundPlayer,
                                                2,
                                                2,
                                                gridActor.x,
                                                gridActor.y,
                                                CELL_SIZE,
                                                0f)
                                    },
                                    {
                                        createForExplanation(
                                                assets,
                                                soundPlayer,
                                                4,
                                                2,
                                                gridActor.x,
                                                gridActor.y,
                                                CELL_SIZE,
                                                0f)
                                    }
                            ) { addRule3Impl(2f) })
                }.also {
                    addActor(it)
                    actorArray.add(it)
                }


        contentTable
                .add(Label(
                        "Crush All Candy Glass\nTo Play Next Level!",
                        LabelStyle(assets.getBitmapFont(Assets.FONT_COSMIC_SANS_ORANGE), null))
                        .apply {
                            setFontScale(FONT_SCALE)
                            setAlignment(Align.center)
                        })
                .pad(LABEL_PAD)
                .padBottom(0.1f * Constants.DIALOG_SIZE_RATIO)
                .padTop(2f * Constants.DIALOG_SIZE_RATIO)
    }

    private fun addRule3Impl(firstDelay_: Float) {
        var firstDelay = firstDelay_
        val localCoordOfDialog = Vector2(0f, 0f)
        val stageCoordOfDialog = localToStageCoordinates(localCoordOfDialog)
        addCandyGlass(1, 2, CandyGlassBall2Actor::class.java, firstDelay, stageCoordOfDialog)
        firstDelay += 0.25f
        addCandyGlass(2, 2, CandyGlassBall3Actor::class.java, firstDelay, stageCoordOfDialog)
        firstDelay += 0.25f
        addCandyGlass(3, 2, CandyGlassBall13Actor::class.java, firstDelay, stageCoordOfDialog)
    }

    private fun addSpinyGlassInRule2(stageCoordOfDialog: Vector2, candyGlassPos: Vector2) {
        Pools.get(SpinyBallActor::class.java).obtain()
                .apply {
                    poolInitExplanation(
                            assets,
                            soundPlayer,
                            2,
                            2,
                            gridActor.x,
                            gridActor.y,
                            CELL_SIZE,
                            stageCoordOfDialog.x,
                            stageCoordOfDialog.y)
                    addAction(
                            forever(
                                    sequence(
                                            delay(0.5f),
                                            run2 { markAndEnableForBreakable() },
                                            delay(1f),
                                            run2 { markForBreak(true) })))
                }.also {
                    addActor(it)
                    actorArray.add(it)
                    candyGlassPos.set(it.x, it.y)
                }
    }

    private fun addHandActorAndHandActorCandyRule1(): HandActorCandyGlass {
        val handActorForHowToPlay = HandActorCandyGlass.createHandActorForHowToPlay(assets, CELL_SIZE)
                .apply {
                    setPosition(
                            gridActor.x + gridActor.width - 0.5f * CELL_SIZE,
                            gridActor.y + gridActor.height + CELL_SIZE * 1.1f)
                }.also {
                    actorArray.add(it)
                    it.setScale(0.7f)
                    addActor(it)
                }

        handActor
                .apply {
                    rotation = 0f
                    setScale(1.0f)
                    setPosition(
                            handActorForHowToPlay.x, handActorForHowToPlay.y - height * 0.55f)
                    setColor(1f, 1f, 1f, 0.0f)
                    rotation = 45f
                }.also { addActor(it) }


        return handActorForHowToPlay
    }

    private fun addHandActorAndHandActorCandyRule2(): HandActorSpinyGlass {
        val handActorForHowToPlay = HandActorSpinyGlass.createHandActorForHowToPlay(assets, CELL_SIZE)
                .apply {
                    setPosition(
                            gridActor.x + gridActor.width - 0.5f * CELL_SIZE,
                            gridActor.y + gridActor.height + CELL_SIZE * 1.1f)
                    setScale(0.7f)
                }.also {
                    actorArray.add(it)
                    addActor(it)
                }

        handActor.apply {
            rotation = 0f
            setScale(1.0f)
            setPosition(
                    handActorForHowToPlay.x, handActorForHowToPlay.y - height * 0.55f)
            setColor(1f, 1f, 1f, 0.0f)
            rotation = 45f
        }.also { addActor(it) }

        return handActorForHowToPlay
    }

    private fun addCandyGlassInRule1(stageCoordOfDialog: Vector2, candyGlassPos: Vector2) {
        Pools.get(CandyGlassBall2Actor::class.java).obtain().apply {
            initPoolForExplanation(
                    assets,
                    this@GameExplanationDialog.stage,
                    explanationCandyStage,
                    soundPlayer,
                    worldForCandy,
                    DummyCandyGlassInterfaceImplementor.INSTANCE,
                    DummyCandyGlassInterfaceImplementor.INSTANCE,
                    2,
                    2,
                    gridActor.x,
                    gridActor.y,
                    CELL_SIZE,
                    stageCoordOfDialog.x,
                    stageCoordOfDialog.y)
            addAction(
                    forever(
                            sequence(
                                    delay(0.5f),
                                    run2 { markAndEnableForBreakable() },
                                    delay(1f),
                                    run2 { markForBreak(true) })))
        }.also {
            addActor(it)
            actorArray.add(it)
            candyGlassPos.set(it.x, it.y)
        }
    }

    private fun <T : GenericCandyGlassBallActor> addCandyGlass(
            row: Int, col: Int, clazz: Class<T>, firstDelay: Float, stageCoordOfDialog: Vector2): T {
        return Pools.get(clazz).obtain().apply {
            initPoolForExplanation(
                    assets,
                    this@GameExplanationDialog.stage,
                    explanationCandyStage,
                    soundPlayer,
                    worldForCandy,
                    DummyCandyGlassInterfaceImplementor.INSTANCE,
                    DummyCandyGlassInterfaceImplementor.INSTANCE,
                    row,
                    col,
                    gridActor.x,
                    gridActor.y,
                    CELL_SIZE,
                    stageCoordOfDialog.x,
                    stageCoordOfDialog.y)
            addAction(
                    sequence(
                            delay(firstDelay), Actions.run { markForBreak(true) }))
        }.also {
            addActor(it)
            actorArray.add(it)
        }
    }

    private fun <T> addHandActorAnimationForBreak(
            stageCoordOfDialog: Vector2,
            candyGlassPos: Vector2,
            handActorCandy: T,
            glassAddRunnable: Runnable) where T : Actor?, T : IHandActorControlForExplanationDialog? {
        val handActorX = handActor.x
        val handActorY = handActor.y
        handActor.addAction(
                forever(
                        sequence(
                                run2(glassAddRunnable),
                                delay(0.5f),
                                run2 {
                                    handActor.setColor(1f, 1f, 1f, 0.5f)
                                    handActorCandy!!.startAnimation()
                                },
                                delay(1f),
                                run2 {
                                    handActor.setPosition(
                                            candyGlassPos.x, candyGlassPos.y - handActor.height * 0.55f)
                                },
                                delay(0.5f),
                                run2 { handActor.setColor(1f, 1f, 1f, 0.0f) },
                                delay(0.5f),
                                run2 { handActorCandy!!.stopAnimation() },
                                delay(1f),
                                run2 {
                                    handActor.setColor(1f, 1f, 1f, 0.5f)
                                    handActor.setPosition(handActorX, handActorY)
                                    handActorCandy!!.startAnimation()
                                    handActorCandy.startAnimation()
                                })))
    }

    private fun addContinueLabel() {
        GM_Container(
                Label("Tap To Continue",
                        LabelStyle(assets.getBitmapFont(Assets.FONT_COMIC_SANS_MS), null))
                        .apply {
                            setFontScale(2f)
                            setAlignment(Align.center)
                        })
                .apply {
                    setOrigin(
                            width * Constants.DIALOG_SIZE_RATIO / 2,
                            height * Constants.DIALOG_SIZE_RATIO / 2)
                    addAction(
                            forever(
                                    sequence(
                                            delay(2f),
                                            Actions.scaleBy(0.1f, 0.1f, 0.2f),
                                            Actions.scaleBy(-0.1f, -0.1f, 0.2f),
                                            Actions.scaleBy(0.1f, 0.1f, 0.2f),
                                            Actions.scaleBy(-0.1f, -0.1f, 0.2f))
                                            .also {
                                                continueLabelAction = it
                                            }))
                }.also {
                    buttonTable.add(it)
                }
    }

    private fun createThrowableAnimationAction(
            ball1Supplier: Supplier<MazeBallActor>,
            ball2Supplier: Supplier<MazeBallActor>,
            actionEnd: Runnable): Action {
        val handActorX = handActor.x
        val handActorY = handActor.y
        return forever(
                sequence(
                        delay(1f),
                        parallel(
                                alpha(0.1f, 1f),
                                sequence(
                                        run2 {
                                            with(handActor) {
                                                addAction(alpha(HandActor.ALPHA))
                                                addAction(
                                                        moveTo(
                                                                gridActor.x,
                                                                gridActor.y + gridActor.height / 2,
                                                                1.5f))
                                                addAction(fadeOut(2f))
                                            }
                                            ball1Supplier.get().apply {
                                                addAction(
                                                        sequence(
                                                                alpha(0.3f, 1f),
                                                                delay(1f),
                                                                fadeOut(0.5f),
                                                                run2 {
                                                                    //destroyBody()
                                                                    remove()
                                                                    Gdx.app.debug(TAG, "MazeBallActor removed")
                                                                }))
                                            }.also { addActor(it) }
                                        },
                                        delay(0.2f),
                                        run2 {
                                            ball2Supplier.get().apply {
                                                addAction(
                                                        sequence(
                                                                alpha(0.55f, 1f),
                                                                delay(1f),
                                                                fadeOut(0.5f),
                                                                run2 {
                                                                    //destroyBody()
                                                                    remove()
                                                                    Gdx.app.debug(TAG, "MazeBallActor removed")
                                                                }))
                                            }.also { addActor(it) }
                                        },
                                        delay(0.2f))),
                        delay(1f),
                        run2 {
                            with(handActor) {
                                clearActions()
                                setColor(1f, 1f, 1f, 0f)
                                setPosition(handActorX, handActorY)
                            }
                            actionEnd.run()
                        },
                        Actions.fadeIn(1f)))
    }

    internal class GameExplanationDialogFactory @Inject constructor(
            private val assets: Assets,
            private val soundPlayer: SoundPlayer,
            @param:Named(ClassicModeModule2.CLASSIC_EXPLANATION_CANDY_STAGE_1) private val explanationCandyStage: Stage) {
        fun create(closeAction: Runnable, levelNumber: Int): GameExplanationDialog {
            return GameExplanationDialog(
                    assets, soundPlayer, closeAction, levelNumber, explanationCandyStage)
        }
    }

    private class HandActor(assets: Assets) : TableActor() {
        init {
            loadTexture(assets.getTexture(Assets.HAND_EXPLANATION))
            setOrigin(width / 2, height / 2)
            rotation = 45f
        }

        companion object {
            const val ALPHA = 0.4f
        }
    }

    private class DummyCandyGlassInterfaceImplementor : ICandyGainer, ICandyGlassHandActor {
        override fun decrementHandCount(): Int = 0

        override val handCount: Int = 0

        override fun makePassive() {}
        override fun updateFromDataPersistenceManager() {}

        override val isBreakingEnabled: Boolean = false

        override fun candyGained(candyData: GenericCandyGlassBallActorData, row: Int, col: Int) {}

        override fun candyGlassCrackedButNotGained(row: Int, col: Int) {}

        companion object {
            val INSTANCE = DummyCandyGlassInterfaceImplementor()
        }
    }

    companion object {
        private const val TAG = "Gcom.parabolagames.glassmaze.ameExplanationDialog"
        private const val FONT_SCALE = 2.5f
        private const val CELL_SIZE = 0.25f * Constants.DIALOG_SIZE_RATIO
        private const val GRID_COL = 5
        private const val GRID_ROW = 5
        private const val LABEL_PAD = 0.30f * Constants.DIALOG_SIZE_RATIO
    }
}