package com.parabolagames.glassmaze.candymode.glass

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.Pools
import com.google.common.collect.ImmutableList
import com.parabolagames.glassmaze.candymode.CandyModeModule
import com.parabolagames.glassmaze.candymode.ICandyGainer
import com.parabolagames.glassmaze.candymode.IGlassMissedListener
import com.parabolagames.glassmaze.candymode.IHandCounter
import com.parabolagames.glassmaze.framework.ForGame
import com.parabolagames.glassmaze.shared.Assets
import com.parabolagames.glassmaze.shared.SoundPlayer
import javax.inject.Inject
import javax.inject.Named

@ForGame
internal class CandyGlassBallFactory @Inject constructor(
        private val assets: Assets,
        @param:Named(CandyModeModule.CANDY_MAIN_STAGE_0) private val mainStage0: Stage,
        @param:Named(CandyModeModule.CANDY_MAIN_STAGE_1) private val mainStage1: Stage,
        private val soundPlayer: SoundPlayer,
        @param:Named(CandyModeModule.CANDY_WORLD) private val world: World,
        private val candyGainer: ICandyGainer,
        private val handCounter: IHandCounter,
        private val glassMissedListener: IGlassMissedListener) {

    fun getRandomGenericCandyGlassBallActor(
            posX: Float,
            posY: Float,
            widthAndHeight: Float): GenericCandyGlassBallActor {
        val ballIndex = MathUtils.random(genericCandyGlassBallActorClassList.size - 1)
        val glassClazz = genericCandyGlassBallActorClassList[ballIndex]
        return Pools.get(glassClazz).obtain().initPoolForGame(
                assets,
                mainStage0,
                mainStage1,
                soundPlayer,
                posX,
                posY,
                world,
                candyGainer,
                handCounter,
                glassMissedListener,
                widthAndHeight)
    }

    companion object {
        val genericCandyGlassBallActorClassList: List<Class<out GenericCandyGlassBallActor>> = ImmutableList.of(
                CandyGlassBall2Actor::class.java,
                CandyGlassBall7Actor::class.java,
                CandyGlassBall12Actor::class.java,
                CandyGlassBall13Actor::class.java,
                CandyGlassBall14Actor::class.java,
                CandyGlassBall17Actor::class.java,
                CandyGlassBall16Actor::class.java,
                CandyGlassBall15Actor::class.java,
                CandyGlassBall3Actor::class.java,
                CandyGlassBall5Actor::class.java,
                CandyGlassBall10Actor::class.java,
                CandyGlassBall6Actor::class.java,
                CandyGlassBall8Actor::class.java,
                CandyGlassBall4Actor::class.java,
                CandyGlassBall9Actor::class.java,
                CandyGlassBall11Actor::class.java,
                CandyGlassBall4Actor::class.java,
                CandyGlassBall1Actor::class.java,
                CandyGlassBall15Actor::class.java)
    }
}