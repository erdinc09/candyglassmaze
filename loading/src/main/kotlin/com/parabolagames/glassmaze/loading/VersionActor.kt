package com.parabolagames.glassmaze.loading

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.parabolagames.glassmaze.shared.Version
import com.parabolagames.glassmaze.shared.Assets
import com.parabolagames.glassmaze.shared.Constants
import javax.inject.Inject
import javax.inject.Named

internal class VersionActor
@Inject constructor(@Named(Version.VERSION_DAGGER_NAME_FOR_INJECTION) version: String?)
    : Label(version, LabelStyle(BitmapFont(Gdx.files.internal(Assets.FONT_COMIC_SANS_MS)), null)) {

    init {
        wrap = false
        this.setFontScale(2f)
        setPosition(
                Constants.WORLD_WIDTH * Constants.DIALOG_SIZE_RATIO / 2 - width - PADDING,
                Constants.WORLD_HEIGHT * Constants.DIALOG_SIZE_RATIO / 2 - height / 2)
        setPosition(50f, 50f)
    }

    fun dispose() = style.font.dispose()

    companion object {
        private const val PADDING = 0.05f
    }
}