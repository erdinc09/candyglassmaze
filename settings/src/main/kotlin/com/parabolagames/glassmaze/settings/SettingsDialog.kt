package com.parabolagames.glassmaze.settings

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions.*
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle
import com.badlogic.gdx.scenes.scene2d.ui.Dialog
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.Pools
import com.google.common.eventbus.EventBus
import com.parabolagames.glassmaze.framework.run2
import com.parabolagames.glassmaze.shared.*
import com.parabolagames.glassmaze.shared.ui.DialogCloserFromKeyEvents
import com.parabolagames.glassmaze.shared.ui.DialogStyle
import com.parabolagames.glassmaze.shared.ui.GM_Slider
import kotlin.math.roundToInt

internal class SettingsDialog(
        private val assets: Assets,
        private val soundControl: ISoundControl,
        private val addsController: IAddsController,
        private val eventBus: EventBus) : Dialog("Settings", DialogStyle(assets)) {

    private val soundAtlas = assets.getTextureAtlas(Assets.BUTTONS_SOUND)
    private val soundButton = CheckBox("",
            CheckBoxStyle(
                    TextureRegionDrawable(soundAtlas.findRegion("sound_on")),
                    TextureRegionDrawable(soundAtlas.findRegion("sound_off")),
                    assets.getBitmapFont(Assets.FONT_COSMIC_SANS_ORANGE),
                    Color.WHITE))
    private val musicButton = CheckBox("", CheckBoxStyle(
            TextureRegionDrawable(soundAtlas.findRegion("music_on")),
            TextureRegionDrawable(soundAtlas.findRegion("music_off")),
            assets.getBitmapFont(Assets.FONT_COSMIC_SANS_ORANGE),
            Color.WHITE))
    private val soundSlider: GM_Slider = GM_Slider(0f, Constants.MAX_SOUND_VOLUME, Constants.MUSIC_SOUND_STEP, false, assets)
    private val musicSlider: GM_Slider = GM_Slider(0f, Constants.MAX_MUSIC_VOLUME, Constants.MUSIC_SOUND_STEP, false, assets)

    private val aboutButton: TextButton = TextButton("About", Utils.textButtonStyle(assets))

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
        addSliders()
        addSoundMusicButtons()
        addCloseButton()
        addAboutButton()
    }

    private fun addAboutButton() {
        with(aboutButton) {
            this@SettingsDialog.addActor(this)
            addListener(
                    object : ChangeListener() {
                        override fun changed(event: ChangeEvent, actor: Actor) {
                            result(true)
                            eventBus.post(EventOpenAboutDialog())
                        }
                    })
            addAction(sequence(fadeOut(0f),
                    run2 { isDisabled = true },
                    delay(0.5f),
                    parallel(fadeIn(1f),
                            run2 { isDisabled = false })))
        }
    }

    override fun result(obj: Any) {
        hide(null)
        soundControl.saveVolumes()
        addsController.showBanner(false)
    }

    override fun show(stage: Stage): Dialog {
        addsController.showBanner(true)
        super.show(stage, null)
        setPosition(((stage.width - width) / 2).roundToInt().toFloat(), ((stage.height - height) / 2).roundToInt().toFloat())
        musicButton
                .apply {
                    setPosition(-20 - width, this@SettingsDialog.height - height - PAD - height - 50)
                }
        soundButton
                .apply {
                    setPosition(-20 - width, this@SettingsDialog.height - height - PAD)
                }
        aboutButton
                .apply {
                    label.setFontScale(2.75f)
                    setSize(0.6f * Constants.DIALOG_SIZE_RATIO, 0.3f * Constants.DIALOG_SIZE_RATIO)
                    setPosition(this@SettingsDialog.width / 2 - width / 2, -2 * height)
                }
        return this
    }


    private fun addSliders() {
        with(soundSlider) {
            value = soundControl.soundVolume
            addListener(Runnable { soundControl.soundVolume = value })
            contentTable
                    .add(this)
                    .pad(0.1f * Constants.DIALOG_SIZE_RATIO)
                    .padTop(0.3f * Constants.DIALOG_SIZE_RATIO)
                    .width(1f * Constants.DIALOG_SIZE_RATIO)
        }

        contentTable.row()

        with(musicSlider) {
            value = soundControl.musicVolume
            addListener(Runnable { soundControl.musicVolume = value })
            contentTable.add(this).pad(0.1f * Constants.DIALOG_SIZE_RATIO)
                    .width(1f * Constants.DIALOG_SIZE_RATIO)
        }
    }

    private fun addSoundMusicButtons() {
        with(soundButton) {
            isChecked = soundControl.isSoundMuted
            this@SettingsDialog.addActor(this)
            addListener(
                    object : ChangeListener() {
                        override fun changed(event: ChangeEvent, actor: Actor) {
                            soundControl.isSoundMuted = soundButton.isChecked
                            with(soundSlider) {
                                isDisabled = soundButton.isChecked
                                color = if (soundButton.isChecked) Color.GRAY else Color.WHITE
                            }
                        }
                    })
            Pools.obtain(ChangeListener.ChangeEvent::class.java).let {
                fire(it)
                Pools.free(it)
            }
        }

        with(musicButton) {
            isChecked = soundControl.isMusicMuted
            this@SettingsDialog.addActor(this)
            addListener(
                    object : ChangeListener() {
                        override fun changed(event: ChangeEvent, actor: Actor) {
                            soundControl.isMusicMuted = musicButton.isChecked
                            with(musicSlider) {
                                isDisabled = musicButton.isChecked
                                color = if (musicButton.isChecked) Color.GRAY else Color.WHITE
                            }
                        }
                    })

            Pools.obtain(ChangeListener.ChangeEvent::class.java)
                    .let {
                        fire(it)
                        Pools.free(it)
                    }
        }
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

    companion object {
        const val PAD = 250f
        private const val TAG = "SettingsDialog"
    }
}