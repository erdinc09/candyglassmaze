package com.parabolagames.glassmaze.shared

import com.badlogic.gdx.Gdx
import java.lang.Boolean

object Constants {
    const val INFINITY = -9999999
    const val INFINITY_SYMBOL = "∞"
    @JvmField
    val DEBUG_STAGE_CHILDREN_SIZE = Boolean.getBoolean("debugStageChildrenSize")
    @JvmField
    val DEBUG_DRAW = Boolean.getBoolean("debugDraw")

    /** meters  */
    const val WORLD_HEIGHT = 5f

    /** meters  */
    const val MENU_BUTTON_WIDTH = 0.75f
    const val MENU_BUTTON_WIDTH_SMALL = MENU_BUTTON_WIDTH * 0.75f

    /** meters  */
    const val MENU_BUTTON_PAD = MENU_BUTTON_WIDTH / 3.5f

    /** meters  */
    @JvmField
    val WORLD_WIDTH = WORLD_HEIGHT * Gdx.graphics.width / Gdx.graphics.height
    const val DIALOG_SIZE_RATIO = 1000f

    const val MAX_MUSIC_VOLUME = 0.2f
    const val MAX_SOUND_VOLUME = 0.1f
    const val MUSIC_SOUND_STEP = 0.001f
}