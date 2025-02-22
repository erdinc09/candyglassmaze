package com.parabolagames.glassmaze.shared.candy2

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.utils.Pool
import com.badlogic.gdx.utils.Pools

internal class CandyActor4 : CandyActor() {

    override fun setParent(parent: Group?) {
        super.setParent(parent)
        if (parent == null) {
            POOL.free(this)
            Gdx.app.debug("CandyActor4", "freed")
        }
    }

    companion object {
        val POOL: Pool<CandyActor4> = Pools.get(CandyActor4::class.java, CandyMap.MAX_POOLED_CANDY_COUNT)
    }
}