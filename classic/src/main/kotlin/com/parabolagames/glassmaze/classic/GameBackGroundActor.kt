package com.parabolagames.glassmaze.classic

import com.badlogic.gdx.Gdx
import com.parabolagames.glassmaze.framework.BaseActor
import com.parabolagames.glassmaze.framework.ForGame
import com.parabolagames.glassmaze.shared.Assets
import com.parabolagames.glassmaze.shared.Constants
import javax.inject.Inject

@ForGame
internal class GameBackGroundActor
@Inject constructor(assets: Assets) : BaseActor(0f, 0f) {
    init {
        loadTextureWithRespectToRatio(assets.getTexture(Assets.GAME_BACK_GROUND2))
        setSize(
                Constants.WORLD_HEIGHT * Gdx.graphics.width / Gdx.graphics.height,
                Constants.WORLD_HEIGHT)
    }
}