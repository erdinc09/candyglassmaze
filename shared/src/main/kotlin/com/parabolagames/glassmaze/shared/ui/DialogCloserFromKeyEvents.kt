package com.parabolagames.glassmaze.shared.ui

import com.badlogic.gdx.Input
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener

class DialogCloserFromKeyEvents(private val closeAction: Runnable) : InputListener() {
    override fun keyDown(event: InputEvent, keycode: Int): Boolean {
        if (keycode == Input.Keys.BACK || keycode == Input.Keys.DEL || keycode == Input.Keys.SPACE || keycode == Input.Keys.ESCAPE) {
            closeAction.run()
        }
        return true
    }
}