package com.parabolagames.glassmaze.classic

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.parabolagames.glassmaze.framework.ForGame
import com.parabolagames.glassmaze.shared.Assets
import javax.inject.Inject

@ForGame
internal class LevelNumberActor
@Inject constructor(assets: Assets)
    : Label(getFormattedLevelString(0), ScoreActorStyle(assets)) {

    init {
        wrap = false
        setFontScale(0.002f)
    }

    fun setLevelNumber(levelNumber: Int) = setText(getFormattedLevelString(levelNumber.toLong()))

    internal class ScoreActorStyle(assets: Assets) : LabelStyle() {
        init {
            font = assets.getBitmapFont(Assets.FONT_COSMIC_SANS_ORANGE_BOLD)
        }
    }

    companion object {
        private fun getFormattedLevelString(level: Long): String {
            return String.format("LEVEL-%d", level)
        }
    }
}