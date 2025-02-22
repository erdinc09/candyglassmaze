package com.parabolagames.glassmaze.shared.candy2

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.utils.Pool
import com.badlogic.gdx.utils.Pools

internal class CandyActor17 : CandyActor() {

    override fun setParent(parent: Group?) {
        super.setParent(parent)
        if (parent == null) {
            POOL.free(this)
            Gdx.app.debug("CandyActor17", "freed")
        }
    }

    companion object {
        val POOL: Pool<CandyActor17> = Pools.get(CandyActor17::class.java, CandyMap.MAX_POOLED_CANDY_COUNT)
    }
}