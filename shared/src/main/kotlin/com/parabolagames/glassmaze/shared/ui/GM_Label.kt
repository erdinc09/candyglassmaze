package com.parabolagames.glassmaze.shared.ui

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.parabolagames.glassmaze.shared.Assets

class GM_Label(text: CharSequence, assets: Assets, fontPath: String, fontScale: Float)
    : Label(text, LabelStyle(assets.getBitmapFont(fontPath), null)) {
    init {
        setFontScale(fontScale)
    }
}