package com.parabolagames.glassmaze.shared.candy

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.utils.Pool
import com.badlogic.gdx.utils.Pools

internal class CandyActor14 : CandyActorBox2D() {

    override fun setParent(parent: Group?) {
        super.setParent(parent)
        if (parent == null) {
            POOL.free(this)
            Gdx.app.debug("CandyActor14", "freed")
        }
    }

    companion object {
        val POOL: Pool<CandyActor14> = Pools.get(CandyActor14::class.java, CandyMap.MAX_POOLED_CANDY_COUNT)
    }
}