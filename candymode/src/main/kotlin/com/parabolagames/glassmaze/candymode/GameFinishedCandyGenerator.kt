package com.parabolagames.glassmaze.candymode

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Stage
import com.parabolagames.glassmaze.candymode.glass.GenericCandyGlassBallActorData
import com.parabolagames.glassmaze.framework.ForGame
import com.parabolagames.glassmaze.shared.Assets
import com.parabolagames.glassmaze.shared.Box2DActorDelayedRemover
import com.parabolagames.glassmaze.shared.Constants
import com.parabolagames.glassmaze.shared.candy.CandyActorBox2D
import java.util.ArrayList
import javax.inject.Inject
import javax.inject.Named

@ForGame
internal class GameFinishedCandyGenerator @Inject constructor(
        @param:Named(CandyModeModule.CANDY_WORLD) private val world: World,
        @param:Named(CandyModeModule.CANDY_MAIN_STAGE_1) private val mainStage1: Stage,
        private val assets: Assets) {
    private val gainedCandyDataList: MutableList<GenericCandyGlassBallActorData> = ArrayList()
    private var candyGenerationClock = 0f
    private var candyGenerationIndex = 0
    private var candyGenerationStartTimeCounter = 0f

    fun addGenericCandyGlassBallActorData(data: GenericCandyGlassBallActorData) {
        gainedCandyDataList.add(data)
    }

    fun generateCandies(dt: Float) {
        if (candyGenerationIndex == gainedCandyDataList.size) {
            return
        }
        candyGenerationStartTimeCounter += dt
        if (candyGenerationStartTimeCounter < 1.5f) {
            return
        }
        candyGenerationClock += dt
        if (candyGenerationClock > 0.04f) {
            candyGenerationClock = 0f
            val candySizeRatio = 1f
            val data = gainedCandyDataList[candyGenerationIndex]
            val candy = CandyActorBox2D.getCandy(
                    assets,
                    data.candyAtlas,
                    data.size * candySizeRatio,
                    data.isAnimatedInGlass,
                    data.frameDurationSupplier,
                    Constants.WORLD_WIDTH * 0.5f + MathUtils.random(-0.3f, 0.3f),
                    Constants.WORLD_HEIGHT * 0.5f + 0.2f,
                    world)
            candyGenerationIndex++
            candy.setVelocity(MathUtils.random(-2f, 2f), 2.8f)
            mainStage1.addActor(candy)
            Box2DActorDelayedRemover.add(candy)
        }
    }
}