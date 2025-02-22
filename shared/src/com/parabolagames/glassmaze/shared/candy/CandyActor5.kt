package com.parabolagames.glassmaze.shared.candy

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.utils.Pool
import com.badlogic.gdx.utils.Pools

internal class CandyActor5 : CandyActorBox2D() {

    override fun setParent(parent: Group?) {
        super.setParent(parent)
        if (parent == null) {
            POOL.free(this)
            Gdx.app.debug("CandyActor5", "freed")
        }
    }

    companion object {
        val POOL: Pool<CandyActor5> = Pools.get(CandyActor5::class.java, CandyMap.MAX_POOLED_CANDY_COUNT)
    }
}