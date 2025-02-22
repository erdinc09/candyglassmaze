package com.parabolagames.glassmaze.framework

import com.badlogic.gdx.InputProcessor

internal class InputProcessorProxy @JvmOverloads constructor(private val processor: InputProcessor, var open: Boolean = true) : InputProcessor {
    override fun keyDown(keycode: Int): Boolean = if (open) processor.keyDown(keycode) else false

    override fun keyUp(keycode: Int): Boolean = if (open) processor.keyUp(keycode) else false

    override fun keyTyped(character: Char): Boolean = if (open) processor.keyTyped(character) else false

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean = if (open) processor.touchDown(screenX, screenY, pointer, button) else false

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean = if (open) processor.touchUp(screenX, screenY, pointer, button) else false

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean = if (open) processor.touchDragged(screenX, screenY, pointer) else false

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean = if (open) processor.mouseMoved(screenX, screenY) else false

    override fun scrolled(amountX: Float, amountY: Float): Boolean = if (open) processor.scrolled(amountX,amountY)else false
}