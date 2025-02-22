package com.parabolagames.glassmaze.shared

import com.parabolagames.glassmaze.framework.TableActor

class SingleTextureActor(assets: Assets, texturePath: String, height: Float) : TableActor() {
    init {
        loadTexture(assets.getTexture(texturePath))
        setSize(height * (width / getHeight()), height)
    }
}