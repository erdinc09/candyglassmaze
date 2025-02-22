package com.parabolagames.glassmaze.classic.glass

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.ParticleEmitter
import com.badlogic.gdx.graphics.g2d.Sprite
import com.parabolagames.glassmaze.classic.IPositionProvider
import java.io.BufferedReader

internal class CandyParticleEmitter : ParticleEmitter {

    private lateinit var positionProvider: IPositionProvider

    constructor(reader: BufferedReader) : super(reader)

    constructor(emitter: ParticleEmitter) : super(emitter)

    fun setTarget(positionProvider: IPositionProvider) {
        this.positionProvider = positionProvider
    }

    override fun newParticle(sprite: Sprite): Particle {
        return CandyParticle(sprite)
    }

    inner class CandyParticle(sprite: Sprite) : Particle(sprite) {
        override fun draw(spriteBatch: Batch) {
            val v = vertices
            if (v[1] + (height * scaleY) / 2 < positionProvider.posY) {
                super.draw(spriteBatch)
            }
        }
    }
}