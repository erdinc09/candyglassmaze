package com.parabolagames.glassmaze.shared

import com.badlogic.gdx.physics.box2d.World

abstract class AbstractGameController(private val world: World) {
    protected fun updateWorld(delta: Float) = world.step(delta, 1, 1)

    protected fun disposeWorld() = world.dispose()
}