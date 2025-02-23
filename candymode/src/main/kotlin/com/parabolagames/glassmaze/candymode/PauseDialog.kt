package com.parabolagames.glassmaze.candymode

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Action
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox
import com.badlogic.gdx.scenes.scene2d.ui.Dialog
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.Pools
import com.parabolagames.glassmaze.shared.Assets
import com.parabolagames.glassmaze.shared.Constants
import com.parabolagames.glassmaze.shared.IAddsController
import com.parabolagames.glassmaze.shared.ISoundControl
import com.parabolagames.glassmaze.shared.ui.DialogCloserFromKeyEvents
import com.parabolagames.glassmaze.shared.ui.DialogStyle
import com.parabolagames.glassmaze.shared.ui.GM_Slider
import javax.inject.Inject
import kotlin.math.roundToInt

internal class PauseDialog private constructor(private val assets: Assets,
                                               private val playAction: Runnable,
                                               private val soundControl: ISoundControl,
                                               private val addsController: IAddsController)
    : Dialog("Paused", DialogStyle(assets)) {

    private val soundAtlas = assets.getTextureAtlas(Assets.BUTTONS_SOUND)
    private val soundButton = CheckBox("", CheckBox.CheckBoxStyle(
            TextureRegionDrawable(soundAtlas.findRegion("sound_on")),
            TextureRegionDrawable(soundAtlas.findRegion("sound_off")),
            assets.getBitmapFont(Assets.FONT_COSMIC_SANS_ORANGE),
            Color.WHITE))
    private val musicButton = CheckBox("", CheckBox.CheckBoxStyle(
            TextureRegionDrawable(soundAtlas.findRegion("music_on")),
            TextureRegionDrawable(soundAtlas.findRegion("music_off")),
            assets.getBitmapFont(Assets.FONT_COSMIC_SANS_ORANGE),
            Color.WHITE))

    private val soundSlider: GM_Slider = GM_Slider(0f, Constants.MAX_SOUND_VOLUME, Constants.MUSIC_SOUND_STEP, false, assets)
    private val musicSlider: GM_Slider = GM_Slider(0f, Constants.MAX_MUSIC_VOLUME, Constants.MUSIC_SOUND_STEP, false, assets)

    init {
        isModal = false
        clip = false
        isTransform = true
        initialize()
        addListener(DialogCloserFromKeyEvents { result(true) })
    }

    override fun result(`object`: Any) {
        if (`object` as Boolean) {
            hide(null)
            playAction.run()
        }
        addsController.showBanner(false)
    }

    override fun hide(action: Action?) {
        super.hide(action)
        soundButton.remove()
    }

    private fun initialize() {
        titleLabel.setFontScale(4f)
        titleTable.getCell(titleLabel).padTop(-0.1f * Constants.DIALOG_SIZE_RATIO)
        addSliders()
        addSoundMusicButtons()
        addContinueButton()
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
            this@PauseDialog.addActor(this)
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
            this@PauseDialog.addActor(this)
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

            Pools.obtain(ChangeListener.ChangeEvent::class.java).let {
                fire(it)
                Pools.free(it)
            }
        }
    }


    override fun show(stage: Stage): Dialog {
        addsController.showBanner(true)
        super.show(stage, null)
        setPosition(((stage.width - width) / 2).roundToInt().toFloat(), ((stage.height - height) / 2).roundToInt().toFloat())
        musicButton.apply {
            setPosition(-20 - width, this@PauseDialog.height - height - PAD - height - 50)
        }
        soundButton.apply {
            setPosition(-20 - width, this@PauseDialog.height - height - PAD)
        }
        return this
    }


    private fun addContinueButton() {
        Button().apply {
            val textureAtlas = assets.getTextureAtlas(Assets.BUTTONS_PLAY_PAUSE)
            style = ButtonStyle(TextureRegionDrawable(textureAtlas.findRegion("play_released")),
                    TextureRegionDrawable(textureAtlas.findRegion("play_pressed")), null)
        }.also {
            setObject(it, true)
            buttonTable.add(it)
        }
    }

    class PauseDialogFactory @Inject constructor(
            private val assets: Assets,
            private val soundControl: ISoundControl,
            private val addsController: IAddsController,
    ) {
        fun create(playAction: Runnable): PauseDialog = PauseDialog(assets, playAction, soundControl, addsController)
    }

    companion object {
        const val PAD = 250f
    }
}