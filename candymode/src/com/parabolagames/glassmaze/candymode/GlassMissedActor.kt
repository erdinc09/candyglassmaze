package com.parabolagames.glassmaze.candymode

import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.actions.Actions.*
import com.parabolagames.glassmaze.framework.ForGame
import com.parabolagames.glassmaze.framework.TableActor
import com.parabolagames.glassmaze.shared.Assets
import javax.inject.Inject

@ForGame
internal class GlassMissedActor @Inject constructor(assets: Assets, x: Float, y: Float) : TableActor() {
    companion object {
        private const val HEIGHT = 0.25f
    }

    init {
        loadTexture(assets.getTexture(Assets.LIFE_SINGLE_MISSED))
        setSize(HEIGHT, HEIGHT)
        setOrigin(width, height)
        setPosition(x - width / 2, y)
        addAction(sequence(delay(0.5f), fadeOut(0.5f), removeActor()))
        touchable = Touchable.disabled
    }
}