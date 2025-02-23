package com.parabolagames.glassmaze.menu

import com.badlogic.gdx.Screen
import com.parabolagames.glassmaze.framework.ForApp
import javax.inject.Inject
import javax.inject.Provider

@ForApp
class MenuScreenProvider @Inject internal constructor(private val menuScreenProvider: Provider<MenuScreen>) {

    fun getScreen(): Screen = menuScreenProvider.get()
}