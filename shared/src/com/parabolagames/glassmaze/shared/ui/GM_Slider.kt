package com.parabolagames.glassmaze.shared.ui

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Slider
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.parabolagames.glassmaze.shared.Assets
import com.parabolagames.glassmaze.shared.Constants

class GM_Slider : Slider {
    constructor(
            min: Float, max: Float, stepSize: Float, vertical: Boolean, assets: Assets, listener: Runnable)
            : super(min, max, stepSize, vertical, getStyle(assets)) {
        setAnimateDuration(0f)
        with(style) {
            disabledKnob.minWidth = 0.1f * Constants.DIALOG_SIZE_RATIO
            disabledKnob.minHeight = 0.1f * Constants.DIALOG_SIZE_RATIO
            knob.minWidth = 0.1f * Constants.DIALOG_SIZE_RATIO
            knob.minHeight = 0.1f * Constants.DIALOG_SIZE_RATIO
        }
        addListener(
                object : ChangeListener() {
                    override fun changed(event: ChangeEvent, actor: Actor) {
                        listener.run()
                    }
                })
    }

    constructor(min: Float, max: Float, stepSize: Float, vertical: Boolean, assets: Assets)
            : super(min, max, stepSize, vertical, getStyle(assets)) {
        setAnimateDuration(0f)
        with(style) {
            disabledKnob.minWidth = 0.1f * Constants.DIALOG_SIZE_RATIO
            disabledKnob.minHeight = 0.1f * Constants.DIALOG_SIZE_RATIO
            knob.minWidth = 0.1f * Constants.DIALOG_SIZE_RATIO
            knob.minHeight = 0.1f * Constants.DIALOG_SIZE_RATIO
        }
    }

    fun addListener(changeListener: Runnable) {
        addListener(
                object : ChangeListener() {
                    override fun changed(event: ChangeEvent, actor: Actor) {
                        changeListener.run()
                    }
                })
    }

    companion object {
        private lateinit var style: SliderStyle
        private fun getStyle(assets: Assets): SliderStyle? {
            if (!Companion::style.isInitialized) {
                val sliderAtlas = assets.getTextureAtlas(Assets.SLIDER_UI)
                style = SliderStyle().apply {
                    background = TextureRegionDrawable(sliderAtlas.findRegion("background"))
                    disabledBackground = TextureRegionDrawable(sliderAtlas.findRegion("disabled_background"))
                    knob = TextureRegionDrawable(sliderAtlas.findRegion("knob"))
                    disabledKnob = TextureRegionDrawable(sliderAtlas.findRegion("knob_disabled"))
                    knobAfter = TextureRegionDrawable(sliderAtlas.findRegion("knob_after"))
                }
            }
            return style
        }
    }
}