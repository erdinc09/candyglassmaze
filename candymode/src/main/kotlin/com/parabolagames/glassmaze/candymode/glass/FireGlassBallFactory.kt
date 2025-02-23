package com.parabolagames.glassmaze.candymode.glass

import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Stage
import com.parabolagames.glassmaze.candymode.CandyModeModule
import com.parabolagames.glassmaze.framework.ForGame
import com.parabolagames.glassmaze.shared.Assets
import com.parabolagames.glassmaze.shared.SoundPlayer
import javax.inject.Inject
import javax.inject.Named

@ForGame
internal class FireGlassBallFactory @Inject constructor(
        private val assets: Assets,
        @param:Named(CandyModeModule.CANDY_MAIN_STAGE_0) private val mainStage0: Stage,
        private val soundPlayer: SoundPlayer,
        @param:Named(CandyModeModule.CANDY_WORLD) private val world: World) {

    fun create(posX: Float, posY: Float, widthAndHeight: Float) = FireGlassBall.FIRE_GLASS_BALL_ACTOR_POOL.obtain().poolInitGame(
            assets,
            mainStage0,
            soundPlayer,
            posX,
            posY,
            world,
            widthAndHeight
    )
}
