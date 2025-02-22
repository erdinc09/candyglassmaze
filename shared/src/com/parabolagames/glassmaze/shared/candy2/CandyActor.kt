package com.parabolagames.glassmaze.shared.candy2

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.utils.Pool.Poolable
import com.google.common.base.Supplier
import com.parabolagames.glassmaze.framework.TableActor
import com.parabolagames.glassmaze.shared.Assets

abstract class CandyActor internal constructor() : TableActor(), Poolable {
    init {
        touchable = Touchable.disabled
    }

    private fun reInit(
            assets: Assets,
            candyAtlasName: String,
            size: Float,
            loopAnimation: Boolean,
            frameDurationSupplier: Supplier<Float>) {
        if (animation == null) {
            loadAnimationFromTextureRegions(
                    assets.getTexturesFromTextureAtlas(candyAtlasName),
                    frameDurationSupplier.get()!!,
                    if (loopAnimation) PlayMode.LOOP else PlayMode.LOOP_PINGPONG)
        }
        animation.playMode = if (loopAnimation) PlayMode.LOOP else PlayMode.LOOP_PINGPONG
        setSize(size, size)
        setOrigin(height / 2, width / 2)
        clearActions()
        setScale(1f)
        poolReset()
    }

    override fun reset() {
        clearActions()
        resetAnimation()
    }

    companion object {
        fun getCandy(
                assets: Assets,
                candyAtlasName: String,
                size: Float,
                loopAnimation: Boolean,
                frameDurationSupplier: Supplier<Float>): CandyActor =
                CandyMap.CANDY_MAP.get(candyAtlasName).obtain()
                        .apply {
                            reInit(assets, candyAtlasName, size, loopAnimation, frameDurationSupplier)
                        }

        fun disposeObjectPools() = CandyMap.disposeObjectPools()
    }
}