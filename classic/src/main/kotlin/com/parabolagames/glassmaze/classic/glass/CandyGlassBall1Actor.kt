package com.parabolagames.glassmaze.classic.glass


import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.utils.Pools
import com.parabolagames.glassmaze.framework.proguard.Keep
import com.parabolagames.glassmaze.shared.Assets

/**
 *
 * <img src="{@docRoot}/../../android/assets/candy1/candy1.png" />
 *
 * */

internal class CandyGlassBall1Actor : GenericCandyGlassBallActor(true) {

    override val candyEffect = CandyParticleEffect()

    override fun setParent(parent: Group?) {
        super.setParent(parent)
        if (parent == null) {
            Pools.get(CandyGlassBall1Actor::class.java).free(this)
            Gdx.app.debug(TAG, "freed")
        }
    }

    override fun getData(): GenericCandyGlassBallActorData = DATA


    override fun initCandyEffect(assets: Assets) = candyEffect.load(Gdx.files.internal("effects/candy1.p"), assets.getTextureAtlas(Assets.FIRE_ATLAS))

    companion object {
        @Keep
        @JvmStatic
        fun disposeObjectPools() {
            Pools.get(CandyGlassBall1Actor::class.java).clear()
            Gdx.app.debug(TAG, "pool disposed")
        }

        const val TAG = "com.parabolagames.glassmaze.CandyGlassBall1Actor"
        val DATA = GenericCandyGlassBallActorData(Assets.CANDY1_ATLAS, { MathUtils.random(0.004f, 0.007f) }, true)
    }
}