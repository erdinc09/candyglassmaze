package com.parabolagames.glassmaze.settings

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle
import com.badlogic.gdx.scenes.scene2d.ui.Dialog
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.Align
import com.parabolagames.glassmaze.shared.Assets
import com.parabolagames.glassmaze.shared.Constants
import com.parabolagames.glassmaze.shared.SingleTextureActor
import com.parabolagames.glassmaze.shared.ui.DialogCloserFromKeyEvents
import com.parabolagames.glassmaze.shared.ui.DialogStyle
import com.parabolagames.glassmaze.shared.ui.GM_HyperTextLabel
import com.parabolagames.glassmaze.shared.ui.GM_Label
import kotlin.math.roundToInt

internal class AboutDialog(
        private val assets: Assets) : Dialog("About", DialogStyle(assets)) {


    init {
        isModal = false
        clip = false
        isTransform = true
        initialize()
        addListener(DialogCloserFromKeyEvents { result(true) })
    }

    private fun initialize() {
        titleLabel.setFontScale(4f)
        titleTable.getCell(titleLabel).padTop(-0.1f * Constants.DIALOG_SIZE_RATIO)
        addContent()
        addCloseButton()
    }

    private fun addContent() {
        Table().let {
            SingleTextureActor(assets, Assets.CANDY_GLASS_HIGH_TEXT, 0.5f * Constants.DIALOG_SIZE_RATIO)
                    .apply {
                        it.add(this)
                                .pad(0.25f * Constants.DIALOG_SIZE_RATIO)
                                .padBottom(0.15f * Constants.DIALOG_SIZE_RATIO)
                    }
            it.row()

            GM_HyperTextLabel("by ParabolaGames",
                    assets,
                    Assets.FONT_COMIC_SANS_MS,
                    2f, Color.BROWN,
                    "http://www.parabolagames.com")
                    .apply {
                        it.add(this).pad(0.15f * Constants.DIALOG_SIZE_RATIO)
                    }
            it.row()
            GM_Label("Credits", assets, Assets.FONT_COMIC_SANS_MS, 3f).apply {
                it.add(this).pad(0.01f * Constants.DIALOG_SIZE_RATIO)
            }
            it.row()
            GM_Label(
                    """SOUNDS:
                |
                |(https://freesound.org/people/)
                |-Edo333/sounds/396061/
                |-qubodup/sounds/219940/
                |-Natty23/sounds/349240/
                |-qubodup/sounds/219940/
                |https://creativecommons.org/licenses/by/3.0/
                |
                |-suntemple/sounds/249300/
                |-SypherZent/sounds/420668/
                |-SypherZent/sounds/420670/
                |-tec_studio/sounds/443346/
                |-SypherZent/sounds/420668/
                |-SypherZent/sounds/420670/
                |https://creativecommons.org/publicdomain/zero/1.0/
                |
                |MUSIC:
                |
                |NoWay3 by PeriTune 
                |http://peritune.com
                |Music promoted by 
                |https://www.free-stock-music.com
                |Attribution 4.0 International (CC BY 4.0)
                |https://creativecommons.org/licenses/by/4.0/
            """.trimMargin(), assets, Assets.FONT_COMIC_SANS_MS, 1.1f)
                    .apply {
                        setAlignment(Align.left)
                        it.add(this).pad(0.15f * Constants.DIALOG_SIZE_RATIO).fillX().expandX()
                    }

            ScrollPane(it)
                    .run {
                        setScrollingDisabled(true, false)
                        fadeScrollBars = false
                        contentTable.add(this).fill().expand()
                                .pad(0.01f * Constants.DIALOG_SIZE_RATIO)
                                .padTop(0.15f * Constants.DIALOG_SIZE_RATIO)
                        style = ScrollPane.ScrollPaneStyle()
                                .apply {
                                    val sliderAtlas = assets.getTextureAtlas(Assets.SCROLL_PANE_UI)
                                    vScrollKnob = TextureRegionDrawable(sliderAtlas.findRegion("vertical_knob"))
                                    vScroll = TextureRegionDrawable(sliderAtlas.findRegion("vScroll"))
                                    background = TextureRegionDrawable(sliderAtlas.findRegion("background2"))
                                }
                    }
        }
    }

    override fun result(obj: Any) {
        if (obj as Boolean) {
            hide(null)
        }
    }

    override fun show(stage: Stage): Dialog {
        super.show(stage, null)
        setPosition(
                ((stage.width - width) / 2).roundToInt().toFloat(),
                ((stage.height - height) / 2).roundToInt().toFloat())
        return this
    }


    private fun addCloseButton() {
        Button().apply {
            val textureAtlas = assets.getTextureAtlas(Assets.BUTTONS_ACCEPT_DENY)
            style = ButtonStyle(
                    TextureRegionDrawable(textureAtlas.findRegion("accept_released")),
                    TextureRegionDrawable(textureAtlas.findRegion("accept_pressed")),
                    null)
        }.also {
            buttonTable.add(it)
            setObject(it, true)
        }
    }

    override fun getWidth(): Float {
        return 2 * Constants.DIALOG_SIZE_RATIO
    }

    override fun getHeight(): Float {
        return 3.5f * Constants.DIALOG_SIZE_RATIO
    }

    override fun getPrefHeight(): Float {
        return height
    }

    override fun getPrefWidth(): Float {
        return width
    }
}