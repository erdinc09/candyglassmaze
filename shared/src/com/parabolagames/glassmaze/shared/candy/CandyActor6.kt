package com.parabolagames.glassmaze.shared.candy

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.utils.Pool
import com.badlogic.gdx.utils.Pools

internal class CandyActor6 : CandyActorBox2D() {

    override fun setParent(parent: Group?) {
        super.setParent(parent)
        if (parent == null) {
            POOL.free(this)
            Gdx.app.debug("CandyActor6", "freed")
        }
    }

    companion object {
        val POOL: Pool<CandyActor6> = Pools.get(CandyActor6::class.java, CandyMap.MAX_POOLED_CANDY_COUNT)
    }
}