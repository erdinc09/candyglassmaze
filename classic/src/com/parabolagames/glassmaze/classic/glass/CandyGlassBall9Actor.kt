package com.parabolagames.glassmaze.classic.glass

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.utils.Pools
import com.parabolagames.glassmaze.framework.proguard.Keep
import com.parabolagames.glassmaze.shared.Assets

/** <img src="{@docRoot}/../../android/assets/candy9/candy.png"></img>  */
internal class CandyGlassBall9Actor : GenericCandyGlassBallActor(false) {

    override val candyEffect = CandyParticleEffect()

    override fun setParent(parent: Group?) {
        super.setParent(parent)
        if (parent == null) {
            Pools.get(CandyGlassBall9Actor::class.java).free(this)
            Gdx.app.debug(TAG, "freed")
        }
    }

    override fun initCandyEffect(assets: Assets) = candyEffect.load(Gdx.files.internal("effects/candy1.p"), assets.getTextureAtlas(Assets.FIRE_ATLAS))

    override fun getData(): GenericCandyGlassBallActorData = DATA

    companion object {
        @Keep
        @JvmStatic
        fun disposeObjectPools() {
            Pools.get(CandyGlassBall9Actor::class.java).clear()
            Gdx.app.debug(TAG, "pool disposed")
        }

        const val TAG = "com.parabolagames.glassmaze.CandyGlassBall9Actor"

        private val DATA = GenericCandyGlassBallActorData(
                Assets.CANDY9_ATLAS,
                0.7f,
                1.2f,
                { MathUtils.random(0.04f, 0.06f) },
                false)
    }
}