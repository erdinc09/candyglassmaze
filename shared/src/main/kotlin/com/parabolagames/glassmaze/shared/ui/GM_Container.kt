package com.parabolagames.glassmaze.shared.ui

import com.badlogic.gdx.scenes.scene2d.Action
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Container

class GM_Container<T : Actor>(actor: T, action: Action? = null, sizeCoefficient: Float = 1f) : Container<T>(actor) {

    init {
        isTransform = true
        setSize(actor.width * sizeCoefficient, actor.height * sizeCoefficient)
        setOrigin(width / 2, height / 2)
        action?.let { addAction(it) }
    }
}