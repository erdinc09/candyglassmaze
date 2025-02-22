package com.parabolagames.glassmaze.classic

import com.badlogic.gdx.Screen
import com.parabolagames.glassmaze.framework.ForApp
import javax.inject.Inject

@ForApp
class ClassicModeBuilderProxy @Inject internal constructor(private val classicModeComponentBuilder: ClassicModeComponent.Builder) {

    fun getScreenClassicMode(): Screen = classicModeComponentBuilder
            .classicModeModule(ClassicModeModule3(ClassicModeSubMode.CLASSIC))
            .build()
            .screen

    fun getScreenRandomMode(): Screen = classicModeComponentBuilder
            .classicModeModule(ClassicModeModule3(ClassicModeSubMode.RANDOM))
            .build()
            .screen

    fun getScreenClassicMode(levelNumber: Int) = classicModeComponentBuilder
            .classicModeModule(ClassicModeModule3(levelNumber, ClassicModeSubMode.CLASSIC))
            .build()
            .screen

    fun getScreenRandomMode(levelNumber: Int) = classicModeComponentBuilder
            .classicModeModule(ClassicModeModule3(levelNumber, ClassicModeSubMode.RANDOM))
            .build()
            .screen
}