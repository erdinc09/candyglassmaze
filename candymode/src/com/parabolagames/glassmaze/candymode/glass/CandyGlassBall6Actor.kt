package com.parabolagames.glassmaze.candymode.glass

import androidx.annotation.Keep
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.Pools
import com.parabolagames.glassmaze.candymode.ICandyGainer
import com.parabolagames.glassmaze.shared.Assets
import com.parabolagames.glassmaze.candymode.IGlassMissedListener
import com.parabolagames.glassmaze.candymode.IHandCounter
import com.parabolagames.glassmaze.shared.SoundPlayer

/** <img src="{@docRoot}/../../android/assets/candy6/candy.png"></img>  */
internal class CandyGlassBall6Actor  : GenericCandyGlassBallActor(true) {

    override fun setParent(parent: Group?) {
        super.setParent(parent)
        if (parent == null) {
            Pools.get(CandyGlassBall6Actor::class.java).free(this)
            Gdx.app.debug("CandyGlassBall6Actor", "freed")
        }
    }

    override fun getData(): GenericCandyGlassBallActorData = DATA

    companion object {
        val DATA =   GenericCandyGlassBallActorData(Assets.CANDY6_ATLAS, { MathUtils.random(0.01f, 0.09f) }, true)
    }
}