package com.parabolagames.glassmaze.store.classicmaze

import com.parabolagames.glassmaze.framework.TableActor
import com.parabolagames.glassmaze.shared.Assets
import com.parabolagames.glassmaze.shared.ui.GM_Container

internal class SpinyHandActor(assets: Assets) : TableActor() {

    private val countActor: GM_Container<CountActor> = GM_Container(CountActor(assets))

    init {
        loadTexture(assets.getTexture(Assets.STORE_SPINY_HAND))
        countActor.also {
            addActor(it)
        }
    }

    fun setCount(count: Int) {
        countActor.actor.setCount(count)
        countActor.y = -countActor.prefHeight * 0.5f
    }

    fun reset() {
        countActor.setScale(1f)
    }

    override fun sizeChanged() {
        countActor.x = width * 1.025f
    }
}