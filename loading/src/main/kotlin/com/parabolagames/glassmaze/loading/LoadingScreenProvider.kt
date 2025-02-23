package com.parabolagames.glassmaze.loading

import com.badlogic.gdx.Screen
import com.parabolagames.glassmaze.framework.ForApp
import javax.inject.Inject

@ForApp
class LoadingScreenProvider @Inject internal constructor(private val loadingScreen: LoadingScreen) {

    fun getScreen(): Screen = loadingScreen
}