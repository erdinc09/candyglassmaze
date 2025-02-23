package com.parabolagames.glassmaze.shared

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.utils.Timer
import com.parabolagames.glassmaze.framework.ForApp
import javax.inject.Inject

@ForApp
class SoundPlayer
@Inject internal constructor(private val assets: Assets, private val soundPersistenceManager: ISoundPersistenceManager) : ISoundControl {

    override var isSoundMuted: Boolean
        get() = soundPersistenceManager.isSoundMuted
        set(value) {
            soundPersistenceManager.isSoundMuted = value
        }

    override var isMusicMuted: Boolean
        get() = soundPersistenceManager.isMusicMuted
        set(value) {
            soundPersistenceManager.isMusicMuted = value

            //we are in game
            if (backGroundMusicStarted) {
                if (value) {
                    assets.getSound(Assets.GAME_BACK_GROUUND_SOUND_1).stop()
                } else {
                    backGroundSoundId = assets.getSound(Assets.GAME_BACK_GROUUND_SOUND_1).loop(musicVolume)
                }
            }

            //we are in menu
            if (menuMusicStarted) {
                if (value) {
                    stopMenuMusicImpl()
                } else {
                    playMenuMusicImpl()
                }
            }
        }

    override var musicVolume: Float
        get() = soundPersistenceManager.musicVolume
        set(value) {
            soundPersistenceManager.musicVolume = value
            assets.getMusic(Assets.MENU_MUSIC).volume = value
            assets.getSound(Assets.GAME_BACK_GROUUND_SOUND_1).setVolume(backGroundSoundId, musicVolume)
        }

    override var soundVolume: Float
        get() = soundPersistenceManager.soundVolume
        set(value) {
            soundPersistenceManager.soundVolume = value
        }

    private var backGroundSoundId: Long = 0
    private val breakSoundNames: List<String> = assets.breakSoundNames
    private var backGroundMusicStarted = false
    private var menuMusicStarted = false
    private var menuMusicRandomGlassBreakSoundTask: Timer.Task? = null

    fun playRandomBreak() = assets.getSound(breakSoundNames[MathUtils.random(breakSoundNames.size - 1)]).play(if (isSoundMuted) 0f else soundVolume)

    private fun playRandomBreakInMusic() = assets.getSound(breakSoundNames[MathUtils.random(breakSoundNames.size - 1)]).play(if (isMusicMuted) 0f else musicVolume / 4)

    fun playSound(soundName: String) = assets.getSound(soundName).play(if (isSoundMuted) 0f else soundVolume)

    fun playSpinyBallAppearSound() = assets.getSound(Assets.SPINY_BALL_APPEAR_SOUND).play(if (isSoundMuted) 0f else 1f)

    fun playGlassAppearSound() = assets.getSound(Assets.GLASS_APPEAR_SOUND).play(if (isSoundMuted) 0f else soundVolume * 0.6f)

    fun playNoHandSound() = assets.getSound(Assets.NO_HAND_SOUND).play(if (isSoundMuted) 0f else soundVolume * 0.6f)

    fun playGlassMissedSound() = assets.getSound(Assets.GLASS_MISSED_SOUND).play(if (isSoundMuted) 0f else soundVolume * 0.5f)

    fun loopBackGroundSound1() {
        assets.getSound(Assets.GAME_BACK_GROUUND_SOUND_1).stop()
        backGroundSoundId = assets.getSound(Assets.GAME_BACK_GROUUND_SOUND_1).loop(if (isMusicMuted) 0f else musicVolume)
        backGroundMusicStarted = true
    }

    fun stopBackGroundSound1() {
        assets.getSound(Assets.GAME_BACK_GROUUND_SOUND_1).stop()
        backGroundMusicStarted = false
    }

    fun playMenuMusic() {
        if (!isMusicMuted) {
            playMenuMusicImpl()
        }
        menuMusicStarted = true
    }

    private fun playMenuMusicImpl() {
        if (menuMusicRandomGlassBreakSoundTask != null) {
            return
        }
        assets.getMusic(Assets.MENU_MUSIC)
                .apply {
                    isLooping = true
                    volume = if (isMusicMuted) 0f else musicVolume
                    play()
                }
        menuMusicRandomGlassBreakSoundTask = Timer.schedule(
                object : Timer.Task() {
                    override fun run() {
                        playRandomBreakInMusic()
                    }
                }, 10f, 5f,  // 5
                10000)
    }

    fun stopMenuMusic() {
        stopMenuMusicImpl()
        menuMusicStarted = false
    }

    private fun stopMenuMusicImpl() {
        if (menuMusicRandomGlassBreakSoundTask == null) {
            return
        }
        assets.getMusic(Assets.MENU_MUSIC).stop()
        menuMusicRandomGlassBreakSoundTask!!.cancel()
        menuMusicRandomGlassBreakSoundTask = null
    }

    override fun saveVolumes() = soundPersistenceManager.flushGeneralPreferences()
}