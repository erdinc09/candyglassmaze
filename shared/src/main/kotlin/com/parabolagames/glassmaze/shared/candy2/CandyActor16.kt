package com.parabolagames.glassmaze.shared.candy2

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.utils.Pool
import com.badlogic.gdx.utils.Pools

internal class CandyActor16 : CandyActor() {

    override fun setParent(parent: Group?) {
        super.setParent(parent)
        if (parent == null) {
            POOL.free(this)
            Gdx.app.debug("CandyActor16", "freed")
        }
    }

    companion object {
        val POOL: Pool<CandyActor16> = Pools.get(CandyActor16::class.java, CandyMap.MAX_POOLED_CANDY_COUNT)
    }
}