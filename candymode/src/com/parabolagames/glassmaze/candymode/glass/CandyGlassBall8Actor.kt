package com.parabolagames.glassmaze.candymode.glass

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.utils.Pools
import com.parabolagames.glassmaze.shared.Assets

/**
 * <img src="{@docRoot}/../../android/assets/candy8/candy.png"></img>
 */
internal class CandyGlassBall8Actor : GenericCandyGlassBallActor(true) {

    override fun setParent(parent: Group?) {
        super.setParent(parent)
        if (parent == null) {
            Pools.get(CandyGlassBall8Actor::class.java).free(this)
            Gdx.app.debug("CandyGlassBall8Actor", "freed")
        }
    }

    override fun getData(): GenericCandyGlassBallActorData = DATA

    companion object {
        val DATA = GenericCandyGlassBallActorData(
                Assets.CANDY8_ATLAS,
                0.55f,
                1.2f,
                GenericCandyGlassBallActorData.RandomForceInterval(-15f, 15f, 35f, 42f),
                { 0.02f }, false)
    }
}