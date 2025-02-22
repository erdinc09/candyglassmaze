package com.parabolagames.glassmaze.core.persistence

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.parabolagames.glassmaze.classic.IClassicMazePersistenceManager
import com.parabolagames.glassmaze.framework.ForApp
import com.parabolagames.glassmaze.store.classicmaze.IStorePersistenceManagerClassicMaze
import javax.inject.Inject


@ForApp
internal class ClassicMaze @Inject constructor() : IClassicMazePersistenceManager, IStorePersistenceManagerClassicMaze {

    override var totalScore: Int
        set(value) = set(value, TOTAL_SCORE, classicMazePreferences)
        get() = get(0, TOTAL_SCORE, classicMazePreferences, String::toInt)

    override var levelNumber
        set(value) = set(value, LEVEL_NUMBER, classicMazePreferences)
        get() = get(1, LEVEL_NUMBER, classicMazePreferences, String::toInt)

    override var classicSpinyGlassHandNumberCount: Int
        set(value) = set(value, CLASSIC_SPINY_GLASS_HAND_COUNT, classicMazePreferences)
        get() = get(15, CLASSIC_SPINY_GLASS_HAND_COUNT, classicMazePreferences, String::toInt)

    override var classicCandyGlassHandNumberCount: Int
        set(value) = set(value, CLASSIC_CANDY_GLASS_HAND_COUNT, classicMazePreferences)
        get() = get(15, CLASSIC_CANDY_GLASS_HAND_COUNT, classicMazePreferences, String::toInt)

    private val classicMazePreferences: Preferences = Gdx.app.getPreferences(PREFERENCE_FILE_PREFIX + "aa")

    override fun saveClassicMaze() = classicMazePreferences.flush()


    companion object {
        private const val TOTAL_SCORE = "WyoKIGMhgJ"
        private const val LEVEL_NUMBER = "CHPBbDfxfo"
        private const val CLASSIC_CANDY_GLASS_HAND_COUNT = "CQosIKiAAn"
        private const val CLASSIC_SPINY_GLASS_HAND_COUNT = "DWOBEuaxNZ"
    }
}