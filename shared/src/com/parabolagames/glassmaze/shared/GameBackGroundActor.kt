package com.parabolagames.glassmaze.shared

import com.badlogic.gdx.Gdx
import com.parabolagames.glassmaze.framework.BaseActor
import com.parabolagames.glassmaze.framework.ForGame
import javax.inject.Inject

@ForGame
class GameBackGroundActor @Inject internal constructor(assets: Assets) : BaseActor(0f, 0f) {
    init {
        loadTextureWithRespectToRatio(assets.getTexture(Assets.GAME_BACK_GROUND))
        setSize(Constants.WORLD_HEIGHT * Gdx.graphics.width / Gdx.graphics.height, Constants.WORLD_HEIGHT)
    }
}