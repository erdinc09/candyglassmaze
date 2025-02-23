package com.parabolagames.glassmaze.classic.glass

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.utils.Pools
import com.parabolagames.glassmaze.framework.proguard.Keep
import com.parabolagames.glassmaze.shared.Assets

/** <img src="{@docRoot}/../../android/assets/candy7/candy.png"></img>  */
internal class CandyGlassBall7Actor : GenericCandyGlassBallActor(true) {

    override val candyEffect = CandyParticleEffect()

    override fun setParent(parent: Group?) {
        super.setParent(parent)
        if (parent == null) {
            Pools.get(CandyGlassBall7Actor::class.java).free(this)
            Gdx.app.debug(TAG, "freed")
        }
    }

    override fun initCandyEffect(assets: Assets) = candyEffect.load(Gdx.files.internal("effects/candy7.p"), assets.getTextureAtlas(Assets.FIRE_ATLAS))

    override fun getData(): GenericCandyGlassBallActorData = DATA

    companion object {
        @Keep
        @JvmStatic
        fun disposeObjectPools() {
            Pools.get(CandyGlassBall7Actor::class.java).clear()
            Gdx.app.debug(TAG, "pool disposed")
        }

        const val TAG = "com.parabolagames.glassmaze.CandyGlassBall7Actor"

        private val DATA = GenericCandyGlassBallActorData(Assets.CANDY7_ATLAS, { MathUtils.random(0.01f, 0.09f) }, true)
    }
}