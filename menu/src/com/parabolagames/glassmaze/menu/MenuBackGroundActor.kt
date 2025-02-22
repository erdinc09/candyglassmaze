package com.parabolagames.glassmaze.menu

import com.badlogic.gdx.Gdx
import com.parabolagames.glassmaze.framework.BaseActor
import com.parabolagames.glassmaze.framework.ForApp
import com.parabolagames.glassmaze.shared.Assets
import com.parabolagames.glassmaze.shared.Constants
import javax.inject.Inject

@ForApp
internal class MenuBackGroundActor @Inject constructor(private val assets: Assets) : BaseActor(0f, 0f), IBackgroundScaleProvider {
    override val scale: Float
        get() = height / assets.getTexture(Assets.CANDY_GLASS_HIGH).height

    init {
        loadTextureWithRespectToRatio(assets.getTexture(Assets.CANDY_GLASS_HIGH))
        setSize(Constants.WORLD_HEIGHT * Gdx.graphics.width / Gdx.graphics.height, Constants.WORLD_HEIGHT)
    }
}