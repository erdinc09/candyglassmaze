package com.parabolagames.glassmaze.core.persistence

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.parabolagames.glassmaze.candymode.ICandyCountPersistenceManager
import com.parabolagames.glassmaze.framework.ForApp
import javax.inject.Inject


@ForApp
internal class CandyMode @Inject constructor() : ICandyCountPersistenceManager {
    override var bestScore: Int
        set(value) = set(value, BEST_SCORE, candyModePreferences)
        get() = get(0, BEST_SCORE, candyModePreferences, String::toInt)

    override var totalScore: Int
        set(value) = set(value, TOTAL_SCORE, candyModePreferences)
        get() = get(0, TOTAL_SCORE, candyModePreferences, String::toInt)

    override var ironNumberCount: Int
        set(value) = set(value, IRON_NUMBER_COUNT, candyModePreferences)
        get() = get(DEFAULT_IRON_NUMBER, IRON_NUMBER_COUNT, candyModePreferences, String::toInt)

    override var handNumberCount: Int
        set(value) = set(value, HAND_NUMBER_COUNT, candyModePreferences)
        get() = get(DEFAULT_HAND_NUMBER, HAND_NUMBER_COUNT, candyModePreferences, String::toInt)

    private val candyModePreferences: Preferences = Gdx.app.getPreferences(PREFERENCE_FILE_PREFIX + "cc")

    override fun saveCandyMode() = candyModePreferences.flush()

    companion object {
        private const val DEFAULT_HAND_NUMBER = 20
        private const val BEST_SCORE = "9zL0ag4zYn"
        private const val TOTAL_SCORE = "WyoKIGMhgJ"
        private const val IRON_NUMBER_COUNT = "LbGKVTZg5k"
        private const val HAND_NUMBER_COUNT = "b0qLtSOZPD"
        private const val DEFAULT_IRON_NUMBER = 10
    }
}