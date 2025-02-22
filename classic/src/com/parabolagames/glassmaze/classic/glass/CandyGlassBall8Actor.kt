package com.parabolagames.glassmaze.classic.glass

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.utils.Pools
import com.parabolagames.glassmaze.framework.proguard.Keep
import com.parabolagames.glassmaze.shared.Assets

/** <img src="{@docRoot}/../../android/assets/candy8/candy.png"></img>  */
internal class CandyGlassBall8Actor : GenericCandyGlassBallActor(false) {

    override val candyEffect = CandyParticleEffect()

    override fun setParent(parent: Group?) {
        super.setParent(parent)
        if (parent == null) {
            Pools.get(CandyGlassBall8Actor::class.java).free(this)
            Gdx.app.debug(TAG, "freed")
        }
    }

    override fun initCandyEffect(assets: Assets) = candyEffect.load(Gdx.files.internal("effects/candy1.p"), assets.getTextureAtlas(Assets.FIRE_ATLAS))

    override fun getData(): GenericCandyGlassBallActorData = DATA

    companion object {
        @Keep
        @JvmStatic
        fun disposeObjectPools() {
            Pools.get(CandyGlassBall8Actor::class.java).clear()
            Gdx.app.debug(TAG, "pool disposed")
        }

        const val TAG = "com.parabolagames.glassmaze.CandyGlassBall8Actor"

        private val DATA = GenericCandyGlassBallActorData(
                Assets.CANDY8_ATLAS,
                0.4f,
                1.2f,
                { 0.02f },
                false)
    }
}