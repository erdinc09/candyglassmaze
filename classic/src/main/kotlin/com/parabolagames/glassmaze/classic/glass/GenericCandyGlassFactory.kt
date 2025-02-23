package com.parabolagames.glassmaze.classic.glass

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.Pools
import com.google.common.collect.ImmutableList
import com.parabolagames.glassmaze.classic.ClassicModeModule2
import com.parabolagames.glassmaze.classic.ICandyCounterPositionProvider
import com.parabolagames.glassmaze.classic.ICandyGainer
import com.parabolagames.glassmaze.classic.ICandyGlassHandActor
import com.parabolagames.glassmaze.framework.ForGame
import com.parabolagames.glassmaze.shared.Assets
import com.parabolagames.glassmaze.shared.SoundPlayer
import javax.inject.Inject
import javax.inject.Named
import kotlin.reflect.KClass

@ForGame
internal class GenericCandyGlassFactory @Inject constructor(
        private val assets: Assets,
        @param:Named(ClassicModeModule2.CLASSIC_WORLD) private val world: World,
        @param:Named(ClassicModeModule2.CLASSIC_MAIN_STAGE_0) private val mainStage0: Stage,
        @param:Named(ClassicModeModule2.CLASSIC_MAIN_STAGE_1) private val mainStage1: Stage,
        private val candyCounterPositionProvider: ICandyCounterPositionProvider,
        private val soundPlayer: SoundPlayer,
        private val candyGainer: ICandyGainer,
        private val handCounter: ICandyGlassHandActor) {


    fun getRandomGenericCandyGlassBallActor(
            row: Int, col: Int, xGridActor: Float, yGridActor: Float): GenericCandyGlassBallActor {
        val ballIndex = MathUtils.random(genericCandyGlassBallActorKClassList.size - 1)
        val glassClazz = genericCandyGlassBallActorKClassList[ballIndex].java
        return Pools.get(glassClazz).obtain()
                .apply {
                    initPoolForGame(
                            assets,
                            mainStage0,
                            mainStage1,
                            soundPlayer,
                            world,
                            candyGainer,
                            handCounter,
                            row,
                            col,
                            xGridActor,
                            yGridActor,
                            candyCounterPositionProvider)
                }

    }

    companion object {
        fun disposeObjectPools() {
            Gdx.app.debug("com.parabolagames.glassmaze.GenericCandyGlassFactory", "disposing pools")

            //since R8 is removing companion classes, we make disposeObjectPools JvmStatic and call them via java native reflection, not kotlin reflection
            // maybe we can also call by with it.functions.first { func -> func.name == "disposeObjectPools" }.call(null)
            genericCandyGlassBallActorKClassList.forEach {
                it.java.getMethod("disposeObjectPools").invoke(null)
            }
        }

        val genericCandyGlassBallActorKClassList: List<KClass<out GenericCandyGlassBallActor>> = ImmutableList.of(
                CandyGlassBall2Actor::class,
                CandyGlassBall7Actor::class,
                CandyGlassBall12Actor::class,
                CandyGlassBall13Actor::class,
                CandyGlassBall14Actor::class,
                CandyGlassBall17Actor::class,
                CandyGlassBall16Actor::class,
                CandyGlassBall15Actor::class,
                CandyGlassBall3Actor::class,
                CandyGlassBall5Actor::class,
                CandyGlassBall10Actor::class,
                CandyGlassBall6Actor::class,
                CandyGlassBall8Actor::class,
                CandyGlassBall4Actor::class,
                CandyGlassBall9Actor::class,
                CandyGlassBall11Actor::class,
                CandyGlassBall4Actor::class,
                CandyGlassBall1Actor::class,
                CandyGlassBall15Actor::class)
    }
}