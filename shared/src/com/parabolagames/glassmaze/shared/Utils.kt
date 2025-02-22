package com.parabolagames.glassmaze.shared

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable

object Utils {

    fun textButtonStyle(assets: Assets): TextButton.TextButtonStyle = TextButton.TextButtonStyle()
            .apply {
                val textureAtlas = assets.getTextureAtlas(Assets.BUTTONS_RECTANGLE)
                up = TextureRegionDrawable(textureAtlas.findRegion("released"))
                down = TextureRegionDrawable(textureAtlas.findRegion("pressed"))
                font = assets.getBitmapFont(Assets.FONT_COSMIC_SANS_ORANGE)
                downFontColor = Color.GRAY
                fontColor = Color.WHITE
            }

    fun textButtonStyleCurrency(assets: Assets): TextButton.TextButtonStyle = TextButton.TextButtonStyle()
            .apply {
                val textureAtlas = assets.getTextureAtlas(Assets.BUTTONS_RECTANGLE)
                up = TextureRegionDrawable(textureAtlas.findRegion("released"))
                down = TextureRegionDrawable(textureAtlas.findRegion("pressed"))
                font = assets.getBitmapFont(Assets.FONT_CURRENCY_MONTSERRAT)
                downFontColor = Color.GRAY
                fontColor = Color.WHITE
            }


    fun getColoredTexture(width: Int, height: Int, color: Color): Texture {
        val pixmap = Pixmap(width, height, Pixmap.Format.RGBA8888)
        pixmap.setColor(color)
        pixmap.fill()
        val drawable = Texture(pixmap)
        //pixmap.dispose()
        return drawable
    }
}