package com.parabolagames.glassmaze.candymode

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.actions.Actions.delay
import com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence
import com.parabolagames.glassmaze.framework.IScreenDrawer
import com.parabolagames.glassmaze.framework.TableActor
import com.parabolagames.glassmaze.framework.run2
import com.parabolagames.glassmaze.shared.Assets

internal abstract class BallHolderActor(
        x: Float,
        y: Float,
        private val assets: Assets,
        private val world: World,
        screenDrawer: IScreenDrawer,
        texture: String) : TableActor() {


    val dragVectorDrawer: DragVectorDrawer
    private val LINE_WIDTH = 0.02f
    private val drawPad = 0.1f
    private val RADIUS = ThrowableBallActor.BALL_RADIUS + drawPad


    protected val WIDTH_HEIGHT = RADIUS * 2
    private var isMoved = false
    private var currentDragVector: Vector2? = null
    private var forceVector: Vector2? = null

    init {
        setPosition(x, y)
        loadTexture(assets.getTexture(texture))
        setSize(WIDTH_HEIGHT, WIDTH_HEIGHT)
        dragVectorDrawer = DragVectorDrawer(screenDrawer)
        addListener(dragVectorDrawer)
        setDrawPad(drawPad)
    }

    override fun act(dt: Float) {
        if (!isMoved && forceVector != null) {
            isMoved = true
            createActor(x, y, assets, world)
                    .apply {
                        setVelocity(forceVector!!.x, forceVector!!.y)
                    }.also {
                        stage.addActor(it)
                    }
            forceVector = null
            isAnimationVisible = false
            addAction(sequence(
                    delay(0.1f),
                    run2 {
                        isMoved = false
                        isAnimationVisible = true
                    }))
        }
        super.act(dt)
    }

    protected abstract fun createActor(x: Float, y: Float, assets: Assets, world: World): ThrowableBallActor

    inner class DragVectorDrawer(private val screenDrawer: IScreenDrawer) : InputListener() {
        override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
            return true
        }

        override fun touchUp(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) {
            screenDrawer.drawLine(null)
            if (currentDragVector != null && currentDragVector!!.len() > RADIUS) {
                forceVector = currentDragVector!!.scl(10f)
            }
            currentDragVector = null
        }

        override fun touchDragged(event: InputEvent, x: Float, y: Float, pointer: Int) {
            if (isMoved) {
                return
            }
            currentDragVector = currentDragVectorFinal.set(x, y).sub(RADIUS, RADIUS)
            if (currentDragVectorFinal.len() > RADIUS) {
                currentDragVectorFinal.add(RADIUS, RADIUS)
                currentDragVectorFinal.limit(BALL_MAX_DRAG_LENGTH)


                screenDrawer.drawLine(IScreenDrawer.LineData(
                        localToParentCoordinates(lineStart.set(RADIUS, RADIUS)),
                        localToParentCoordinates(lineEnd.set(currentDragVectorFinal.x, currentDragVectorFinal.y)),
                        LINE_WIDTH,
                        lineColor.set(Interpolation.linear.apply(0.392f, 1f, currentDragVectorFinal.len() / BALL_MAX_DRAG_LENGTH),
                                Interpolation.linear.apply(1f, 0f, currentDragVectorFinal.len() / BALL_MAX_DRAG_LENGTH),
                                Interpolation.linear.apply(0.184f, 0f, currentDragVectorFinal.len() / BALL_MAX_DRAG_LENGTH),
                                1f),
                        lineColorEnd.set(lineColor.r, lineColor.g, lineColor.b, 0.1f)))
            } else {
                currentDragVector = null
                screenDrawer.drawLine(null)
            }
        }
    }

    private val lineColor = Color()
    private val lineColorEnd = Color()
    private val lineStart = Vector2()
    private val lineEnd = Vector2()
    private val currentDragVectorFinal = Vector2()

    companion object {
        private const val BALL_MAX_DRAG_LENGTH = 2f
    }
}