package com.parabolagames.glassmaze.framework

import com.badlogic.gdx.*

/** Created when program is launched; manages the screens that appear during the game.  */
abstract class BaseGame : Game() {
    /** Called when game is initialized, after Gdx.input and other objects have been initialized.  */
    override fun create() {
        // prepare for multiple classes/stages/actors to receive discrete input
        val im = InputMultiplexer()
        Gdx.input.setCatchKey(Input.Keys.BACK, true)
        Gdx.input.inputProcessor = im
    }

    override fun setScreen(screen: Screen) {
        val previousScreen = getScreen()
        super.setScreen(screen)
        previousScreen?.dispose()
    }
}