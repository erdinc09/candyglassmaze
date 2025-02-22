package com.parabolagames.glassmaze.candymode.glass

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.utils.Pools
import com.parabolagames.glassmaze.shared.Assets

/** <img src="{@docRoot}/../../android/assets/candy12/candy.png"></img>  */
internal class CandyGlassBall12Actor : GenericCandyGlassBallActor(true) {

    override fun setParent(parent: Group?) {
        super.setParent(parent)
        if (parent == null) {
            Pools.get(CandyGlassBall12Actor::class.java).free(this)
            Gdx.app.debug("CandyGlassBall12Actor", "freed")
        }
    }

    override fun getData(): GenericCandyGlassBallActorData = DATA

    companion object {
        val DATA = GenericCandyGlassBallActorData(
                Assets.CANDY12_ATLAS,
                0.4f,
                1.5f,
                GenericCandyGlassBallActorData.RandomForceInterval(-4f, 4f, 25f, 30f),
                { MathUtils.random(0.015f, 0.03f) },
                true)
    }
}