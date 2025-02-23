package com.parabolagames.glassmaze.shared.ui

import com.badlogic.gdx.scenes.scene2d.ui.Dialog
import com.parabolagames.glassmaze.shared.Assets

open class GM_Dialog(title: String,
                     assets: Assets,
                     private val dialogWidth: Float? = null,
                     private val dialogHeight: Float? = null)
    : Dialog(title, DialogStyle(assets)) {

    override fun getWidth(): Float = dialogWidth ?: super.getWidth()

    override fun getHeight(): Float = dialogHeight ?: super.getHeight()

    override fun getPrefHeight(): Float = dialogHeight ?: super.getPrefHeight()

    override fun getPrefWidth(): Float = dialogWidth ?: super.getPrefWidth()
}