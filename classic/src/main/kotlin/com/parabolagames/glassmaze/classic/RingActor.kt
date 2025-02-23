package com.parabolagames.glassmaze.classic

import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.parabolagames.glassmaze.framework.TableActor
import com.parabolagames.glassmaze.shared.Assets

internal class RingActor(assets: Assets, x: Float, y: Float) : TableActor() {

    init {
        loadTexture(assets.getTexture(Assets.RING))
        setSize(SIZE, SIZE)
        setPosition(x - width / 2, y - height / 2)
        setOrigin(width / 2, height / 2)
        setColor(1f, 1f, 1f, 0.8f)
        addAction(
                Actions.forever(
                        Actions.sequence(
                                Actions.scaleBy(0.2f, 0.2f, 0.5f),
                                Actions.scaleBy(-0.2f, -0.2f, 0.5f),
                                Actions.scaleBy(0.2f, 0.2f, 0.4f),
                                Actions.scaleBy(-0.2f, -0.2f, 0.4f))))
        touchable = Touchable.disabled
    }

    fun poolInit(assets: Assets, x: Float, y: Float, size: Float) {
        if (animation == null) {
            loadTexture(assets.getTexture(Assets.RING))
            setSize(SIZE, SIZE)
        }
        setSize(size, size)
        setPosition(x - width / 2, y - height / 2)
        setOrigin(width / 2, height / 2)
    }

    fun reset() = poolReset()

    companion object {
        const val SIZE = 0.37f
    }
}