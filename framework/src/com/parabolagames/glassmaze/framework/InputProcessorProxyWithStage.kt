package com.parabolagames.glassmaze.framework

import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.scenes.scene2d.Stage

internal class InputProcessorProxyWithStage constructor(private val processorToOpenToClose: InputProcessor, private val stageToTest: Stage) : InputProcessor {

    val open: Boolean
        get() = stageToTest.actors.size == 0

    override fun keyDown(keycode: Int): Boolean = if (open) processorToOpenToClose.keyDown(keycode) else false

    override fun keyUp(keycode: Int): Boolean = if (open) processorToOpenToClose.keyUp(keycode) else false

    override fun keyTyped(character: Char): Boolean = if (open) processorToOpenToClose.keyTyped(character) else false

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean = if (open) processorToOpenToClose.touchDown(screenX, screenY, pointer, button) else false

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean = if (open) processorToOpenToClose.touchUp(screenX, screenY, pointer, button) else false

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean = if (open) processorToOpenToClose.touchDragged(screenX, screenY, pointer) else false

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean = if (open) processorToOpenToClose.mouseMoved(screenX, screenY) else false

    override fun scrolled(amountX: Float, amountY: Float): Boolean = if (open) processorToOpenToClose.scrolled(amountX, amountY) else false

}