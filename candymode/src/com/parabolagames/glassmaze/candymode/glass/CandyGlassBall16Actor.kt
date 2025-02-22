package com.parabolagames.glassmaze.candymode.glass

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.utils.Pools
import com.parabolagames.glassmaze.shared.Assets

/** <img src="{@docRoot}/../../android/assets/candy16/candy.png"></img>  */
internal class CandyGlassBall16Actor : GenericCandyGlassBallActor(true) {

    override fun setParent(parent: Group?) {
        super.setParent(parent)
        if (parent == null) {
            Pools.get(CandyGlassBall16Actor::class.java).free(this)
            Gdx.app.debug("CandyGlassBall16Actor", "freed")
        }
    }

    override fun getData(): GenericCandyGlassBallActorData = DATA

    companion object {
        val DATA = GenericCandyGlassBallActorData(
                Assets.CANDY16_ATLAS,
                0.4f,
                1.5f,
                GenericCandyGlassBallActorData.RandomForceInterval(-4f, 4f, 20f, 25f),
                { MathUtils.random(0.015f, 0.03f) },
                true)
    }
}