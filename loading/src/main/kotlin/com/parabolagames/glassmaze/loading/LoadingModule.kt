package com.parabolagames.glassmaze.loading

import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.FitViewport
import com.parabolagames.glassmaze.framework.ForApp
import dagger.Module
import dagger.Provides
import com.parabolagames.glassmaze.shared.Constants
import javax.inject.Named

@Module
object LoadingModule {
    const val MAIN_STAGE = "loadingMainStage"
    const val UI_STAGE = "loadingUiStage"

    @JvmStatic
    @Provides
    @Named(MAIN_STAGE)
    @ForApp
    fun provideMainStage(): Stage = Stage(FitViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT))

    @JvmStatic
    @Provides
    @Named(UI_STAGE)
    @ForApp
    fun provideUiStage(): Stage = Stage(FitViewport(
            Constants.WORLD_WIDTH * Constants.DIALOG_SIZE_RATIO,
            Constants.WORLD_HEIGHT * Constants.DIALOG_SIZE_RATIO))
}