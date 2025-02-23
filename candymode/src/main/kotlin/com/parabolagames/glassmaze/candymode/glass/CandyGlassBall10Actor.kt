package com.parabolagames.glassmaze.candymode.glass

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.utils.Pools
import com.parabolagames.glassmaze.shared.Assets

/** <img src="{@docRoot}/../../android/assets/candy10/candy.png"></img>  */
internal class CandyGlassBall10Actor : GenericCandyGlassBallActor(true) {

    override fun setParent(parent: Group?) {
        super.setParent(parent)
        if (parent == null) {
            Pools.get(CandyGlassBall10Actor::class.java).free(this)
            Gdx.app.debug("CandyGlassBall10Actor", "freed")
        }
    }

    override fun getData(): GenericCandyGlassBallActorData = DATA

    companion object {
        val DATA = GenericCandyGlassBallActorData(
                Assets.CANDY10_ATLAS,
                0.75f,
                1.2f,
                GenericCandyGlassBallActorData.RandomForceInterval(-15f, 15f, 35f, 42f),
                { MathUtils.random(0.04f, 0.06f) },
                false)
    }
}