package com.parabolagames.glassmaze.shared.ui

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.ui.Window
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.parabolagames.glassmaze.shared.Assets


class DialogStyle @JvmOverloads constructor(assets: Assets, lightStageBackGround: Boolean = false)
    : Window.WindowStyle(
        assets.getBitmapFont(Assets.FONT_COSMIC_SANS_ORANGE),
        Color.WHITE,
        NinePatchDrawable(DialogNinePatch(assets))) {
    init {
        stageBackground = TextureRegionDrawable(
                assets.getTexture(if (lightStageBackGround) Assets.DIALOG_STAGE_BACK_GROUND_LIGHT else Assets.DIALOG_STAGE_BACK_GROUND))
    }
}