package com.parabolagames.glassmaze.candymode.glass

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.utils.Pools
import com.parabolagames.glassmaze.shared.Assets

/**
 * <img src="{@docRoot}/../../android/assets/candy1/candy1.png"></img>
 */
internal class CandyGlassBall1Actor : GenericCandyGlassBallActor(true) {

    override fun setParent(parent: Group?) {
        super.setParent(parent)
        if (parent == null) {
            Pools.get(CandyGlassBall1Actor::class.java).free(this)
            Gdx.app.debug("CandyGlassBall1Actor", "freed")
        }
    }

    override fun getData(): GenericCandyGlassBallActorData = DATA

    companion object {
        val DATA = GenericCandyGlassBallActorData(Assets.CANDY1_ATLAS, { MathUtils.random(0.004f, 0.007f) }, true)
    }
}