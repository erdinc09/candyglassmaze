package com.parabolagames.glassmaze.classic.glass

import com.badlogic.gdx.graphics.g2d.ParticleEffect
import com.badlogic.gdx.graphics.g2d.ParticleEmitter
import com.parabolagames.glassmaze.classic.IPositionProvider
import java.io.BufferedReader

internal class CandyParticleEffect : ParticleEffect() {

    fun setTarget(candyCounterPositionProvider: IPositionProvider) {
        emitters.forEach {
            it as CandyParticleEmitter
            it.setTarget(candyCounterPositionProvider)
        }
    }

    override fun newEmitter(emitter: ParticleEmitter) = CandyParticleEmitter(emitter)
    override fun newEmitter(reader: BufferedReader) = CandyParticleEmitter(reader)
}