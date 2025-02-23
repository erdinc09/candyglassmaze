package com.parabolagames.glassmaze.classic

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.math.Vector2
import com.google.common.eventbus.EventBus
import com.google.common.eventbus.Subscribe
import com.parabolagames.glassmaze.framework.ForGame
import com.parabolagames.glassmaze.framework.proguard.Keep
import javax.inject.Inject

@ForGame
internal class ClassicModeScreenInputController @Inject constructor(
        private val classicModeScreenDialogController: ClassicModeScreenDialogController,
        private val mazeCubeControl: IMazeCubeControl,
        private val eventBus: EventBus,
        private val touchCoordinatesInGameAreaController: dagger.Lazy<ITouchCoordinatesInGameAreaController>) : InputAdapter() {

    private val touchPointVector = Vector2()
    private val touchVector = Vector2()

    var gameFinished = false
    var gameStarted = false

    private var touchPoint: Vector2? = null

    init {
        eventBus.register(this)
    }

    override fun keyDown(keycode: Int): Boolean {
        if (shouldInputNotToBeProcessed()) {
            return false
        }
        when (keycode) {
            Input.Keys.BACK, Input.Keys.DEL, Input.Keys.ESCAPE -> Gdx.app.postRunnable { classicModeScreenDialogController.showQuitModeDialog() }
            Input.Keys.UP -> mazeCubeControl.moveMazeCube(MazeCubeDirection.UP)
            Input.Keys.DOWN -> mazeCubeControl.moveMazeCube(MazeCubeDirection.DOWN)
            Input.Keys.LEFT -> mazeCubeControl.moveMazeCube(MazeCubeDirection.LEFT)
            Input.Keys.RIGHT -> mazeCubeControl.moveMazeCube(MazeCubeDirection.RIGHT)
        }
        return false
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        if (!touchCoordinatesInGameAreaController.get().isTouchCoordinatesInGameArea(screenX, screenY)) {
            return false
        }
        touchPointVector.set(screenX.toFloat(), screenY.toFloat())
        touchPoint = touchPointVector
        return false
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        return false
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        if (touchPoint == null) {
            return false
        }
        touchVector.set(screenX.toFloat(), screenY.toFloat())
        val angle: Float = touchVector.sub(touchPoint).angle()
        val direction: MazeCubeDirection = when {
            angle in 135.0..225.0 -> MazeCubeDirection.LEFT
            angle in 225.0..315.0 -> MazeCubeDirection.UP
            angle >= 315 || angle <= 45 -> MazeCubeDirection.RIGHT
            //angle in 45.0..135.0 -> MazeCubeDirection.DOWN
            else -> MazeCubeDirection.DOWN
        }
        val dragLenInPx = touchVector.len()
        when (direction) {
            MazeCubeDirection.LEFT, MazeCubeDirection.RIGHT -> {
                if (dragLenInPx / Gdx.graphics.ppcX < DRAG_LEN_LIMIT_CENTI_METERS) return false
            }
            MazeCubeDirection.DOWN, MazeCubeDirection.UP -> {
                if (dragLenInPx / Gdx.graphics.ppcY < DRAG_LEN_LIMIT_CENTI_METERS) return false
            }
        }
        mazeCubeControl.moveMazeCube(direction)

        touchPoint = null
        return false
    }

    private fun shouldInputNotToBeProcessed(): Boolean {
        return classicModeScreenDialogController.isDialogOpen || gameFinished || !gameStarted
    }

    @Subscribe
    @Keep
    private fun eedubl(event: EventEnableDisableUIButtons) {
        touchPoint = null
    }

    fun dispose() = eventBus.unregister(this)

    companion object {
        private const val TAG = "com.parabolagames.glassmaze.ClassicModeScreenInputController"
        private const val DRAG_LEN_LIMIT_CENTI_METERS = 0.2f
    }
}