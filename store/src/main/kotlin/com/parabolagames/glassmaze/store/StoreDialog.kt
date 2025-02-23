package com.parabolagames.glassmaze.store

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions.*
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.Pools
import com.google.common.eventbus.EventBus
import com.parabolagames.glassmaze.framework.run2
import com.parabolagames.glassmaze.shared.Assets
import com.parabolagames.glassmaze.shared.Constants
import com.parabolagames.glassmaze.shared.Utils
import com.parabolagames.glassmaze.shared.ui.DialogCloserFromKeyEvents
import com.parabolagames.glassmaze.shared.ui.GM_Dialog
import com.parabolagames.glassmaze.store.classicmaze.AbstractStoreLine
import kotlin.math.roundToInt

internal class StoreDialog(assets: Assets,
                           private val lines: Array<AbstractStoreLine>,
                           private val eventBus: EventBus)
    : GM_Dialog("Store", assets, dialogHeight = 3.5f * Constants.DIALOG_SIZE_RATIO) {

    private val restoreButton: TextButton = TextButton("Restore", Utils.textButtonStyle(assets))

    init {
        isModal = false
        clip = false
        isTransform = true
        initialize(assets)
        addListener(DialogCloserFromKeyEvents { result(true) })
    }

    override fun result(obj: Any) = close()

    fun close() {
        hide(null)
        lines.forEach(AbstractStoreLine::free)
        lines.clear()
        Pools.free(lines)
    }

    override fun show(stage: Stage): Dialog {
        super.show(stage, null)
        setPosition(
                ((stage.width - width) / 2).roundToInt().toFloat(),
                ((stage.height - height) / 2).roundToInt().toFloat())
        restoreButton
                .apply {
                    label.setFontScale(2.75f)
                    setSize(0.9f * Constants.DIALOG_SIZE_RATIO, 0.3f * Constants.DIALOG_SIZE_RATIO)
                    setPosition(this@StoreDialog.width / 2 - width / 2, -1.5f * height)
                }
        return this
    }

    private fun initialize(assets: Assets) {

        Table().let {
            for (line in lines) {
                it.add(line)
                        .expandX()
                        .fillX()
                        .pad(0.02f * Constants.DIALOG_SIZE_RATIO)
                it.row()
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

        with(titleLabel) {
            setFontScale(4f)
            titleTable.getCell(this).padTop(-0.1f * Constants.DIALOG_SIZE_RATIO)
        }


        Button().apply {
            val textureAtlas = assets.getTextureAtlas(Assets.BUTTONS_ACCEPT_DENY)
            style = ButtonStyle(
                    TextureRegionDrawable(textureAtlas.findRegion("accept_released")),
                    TextureRegionDrawable(textureAtlas.findRegion("accept_pressed")),
                    null)
        }.also {
            buttonTable.add(it)
                    .padTop(0.04f * Constants.DIALOG_SIZE_RATIO)
                    .padBottom(0.01f * Constants.DIALOG_SIZE_RATIO)
            setObject(it,true)
        }

        addRestoreButton()
    }

    private fun addRestoreButton() {
        with(restoreButton) {
            this@StoreDialog.addActor(this)
            addListener(
                    object : ChangeListener() {
                        override fun changed(event: ChangeEvent, actor: Actor) {
                            eventBus.post(EventRestoreButtonClicked())
                        }
                    })
            addAction(sequence(fadeOut(0f),
                    run2 { isDisabled = true },
                    delay(0.5f),
                    parallel(fadeIn(1f),
                            run2 { isDisabled = false })))
        }
    }
}