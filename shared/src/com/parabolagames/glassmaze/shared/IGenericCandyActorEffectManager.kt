package com.parabolagames.glassmaze.shared

import com.badlogic.gdx.graphics.g2d.ParticleEffect

interface IGenericCandyActorEffectManager {
    val candyEffect:ParticleEffect
    val candyEffectShouldDraw:Boolean
}