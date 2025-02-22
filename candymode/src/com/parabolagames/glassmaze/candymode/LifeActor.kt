package com.parabolagames.glassmaze.candymode

import com.badlogic.gdx.scenes.scene2d.actions.Actions.scaleBy
import com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence
import com.parabolagames.glassmaze.framework.ForGame
import com.parabolagames.glassmaze.framework.TableActor
import com.parabolagames.glassmaze.framework.run2
import com.parabolagames.glassmaze.shared.Assets
import javax.inject.Inject

@ForGame
internal class LifeActor @Inject constructor(private val assets: Assets) : TableActor(), ILifeCounter {

    override var lifeCount = 3
    private var actionFinished = true

    init {
        updateLife(lifeCount)
        setSize(HEIGHT * (width / height), HEIGHT)
        setOrigin(width, height)
    }

    private fun updateLife(lifeCount: Int) {
        when (lifeCount) {
            3 -> loadTexture(assets.getTexture(Assets.LIFE_3))
            2 -> loadTexture(assets.getTexture(Assets.LIFE_2))
            1 -> loadTexture(assets.getTexture(Assets.LIFE_1))
            0 -> loadTexture(assets.getTexture(Assets.LIFE_0))
            else -> throw IllegalArgumentException("Undefined lifeCount = $lifeCount")
        }
        setSize(HEIGHT * (width / height), HEIGHT)
        setOrigin(width, height)
    }

    override fun decrementLife(): Int {
        updateLife(--lifeCount)
        if (actionFinished) {
            clearActions()
            addAction(
                    sequence(
                            run2 { actionFinished = false },
                            scaleBy(0.2f, 0.2f, 0.1f),
                            scaleBy(-0.2f, -0.2f, 0.1f),
                            scaleBy(0.1f, 0.1f, 0.05f),
                            scaleBy(-0.1f, -0.1f, 0.05f),
                            run2 { actionFinished = true }))
        }
        return lifeCount
    }

    companion object {
        private const val HEIGHT = 0.35f
    }
}