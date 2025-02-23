package com.parabolagames.glassmaze.core

import com.parabolagames.glassmaze.framework.ForApp
import com.parabolagames.glassmaze.loading.LoadingModule
import com.parabolagames.glassmaze.menu.MenuModule
import com.parabolagames.glassmaze.store.StoreModule
import dagger.Component

@ForApp
@Component(modules = [GlassMazeModule::class, MenuModule::class, LoadingModule::class, StoreModule::class])
internal interface GlassMazeComp {
    fun inject(candyGlass: GlassMazeMain)
    fun createEagerDependencies(): GlassMazeModule.EagerDependencies

    //	@Component.Builder
    //	interface Builder {
    //		@BindsInstance
    //		Builder game(ICandyGlass game);
    //
    //		CandyGlassComp build();
    //	}
}