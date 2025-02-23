package com.parabolagames.glassmaze.store.classicmaze

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable
import com.badlogic.gdx.utils.Pool
import com.parabolagames.glassmaze.shared.Assets
import com.parabolagames.glassmaze.shared.Constants

internal abstract class AbstractStoreLine : Table(), Pool.Poolable {
    private lateinit var spinyHandActor: SpinyHandActor
    private lateinit var candyGlassHandActor: CandyGlassHandActor


    protected open fun init(assets: Assets, spinyCount: Int, candyCount: Int) {
        if (background == null) {
            background = NinePatchDrawable(NinePatch(assets.getTexture(Assets.STORE_LINE_BACK_GROUND), 30, 30, 30, 30))
        }

        if (!::spinyHandActor.isInitialized) {
            spinyHandActor = SpinyHandActor(assets)
        }

        if (!::candyGlassHandActor.isInitialized) {
            candyGlassHandActor = CandyGlassHandActor(assets)
        }

        if (spinyCount > 0 || spinyCount == Constants.INFINITY) {
            add(spinyHandActor).left()
                    .pad(0.05f * Constants.DIALOG_SIZE_RATIO)
                    .padLeft(0.07f * Constants.DIALOG_SIZE_RATIO)
                    .padBottom(0.07f * Constants.DIALOG_SIZE_RATIO)
                    .size(WIDTH, WIDTH / spinyHandActor.widthHeightRatio)
        }

        if (candyCount > 0 || candyCount == Constants.INFINITY) {
            add(candyGlassHandActor).left()
                    .pad(0.05f * Constants.DIALOG_SIZE_RATIO)
                    .padLeft(0.07f * Constants.DIALOG_SIZE_RATIO)
                    .padBottom(0.07f * Constants.DIALOG_SIZE_RATIO)
                    .size(WIDTH, WIDTH / candyGlassHandActor.widthHeightRatio)
        }

        spinyHandActor.setCount(spinyCount)
        candyGlassHandActor.setCount(candyCount)
    }

    override fun reset() {
        super.reset()//clears children and all insets...
        spinyHandActor.reset()
        candyGlassHandActor.reset()
        Gdx.app.debug(TAG, "reset()")
    }

    abstract fun free()

    companion object {
        private const val WIDTH = 0.4f * Constants.DIALOG_SIZE_RATIO
        private const val TAG = "AbstractStoreLine"
    }
}