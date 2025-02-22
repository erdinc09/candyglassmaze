package com.parabolagames.glassmaze.menu

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.FitViewport
import com.parabolagames.glassmaze.framework.DialogStage
import com.parabolagames.glassmaze.framework.ForApp
import com.parabolagames.glassmaze.shared.Constants
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
abstract class MenuModule {
    @Binds
    internal abstract fun bindIBackgroundScaleProvider(backgroundScaleProviderImpl: MenuBackGroundActor): IBackgroundScaleProvider

    @Binds
    internal abstract fun bindICandyModeButtonActorController(screen: MenuScreen): ICandyModeButtonActorController

    @Binds
    internal abstract fun bindIClassicModeButtonActorController(screen: MenuScreen): IClassicModeButtonActorController

    @Binds
    internal abstract fun bindIRandomModeButtonActorController(screen: MenuScreen): IRandomModeButtonActorController

    companion object {
        const val MAIN_STAGE = "menuMainStage"
        const val UI_STAGE = "menuUiStage"
        const val MENU_WORLD = "menuWorld"
        const val DIALOG_UI_STAGE = "menuDialogUiStage"
        const val DIALOG_UI_STAGE_FOR_INFORMATION = "menuDialogForInformationUiStage"

        @JvmStatic
        @Provides
        @Named(MAIN_STAGE)
        @ForApp
        fun provideMainStage(): Stage = Stage(FitViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT))

        @JvmStatic
        @Provides
        @Named(UI_STAGE)
        @ForApp
        fun provideUiStage(): Stage = Stage(FitViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT))

        @JvmStatic
        @Provides
        @Named(DIALOG_UI_STAGE)
        @ForApp
        fun provideDialogUiStage(): Stage = DialogStage(FitViewport(
                Constants.WORLD_WIDTH * Constants.DIALOG_SIZE_RATIO,
                Constants.WORLD_HEIGHT * Constants.DIALOG_SIZE_RATIO))

        @JvmStatic
        @Provides
        @Named(DIALOG_UI_STAGE_FOR_INFORMATION)
        @ForApp
        fun provideDialogUiStageForInformation(): Stage = DialogStage(FitViewport(
                Constants.WORLD_WIDTH * Constants.DIALOG_SIZE_RATIO,
                Constants.WORLD_HEIGHT * Constants.DIALOG_SIZE_RATIO))

        @JvmStatic
        @Provides
        @Named(MENU_WORLD)
        @ForApp
        fun provideMenuWorld(): World = World(Vector2(0f, -2.8f), true)
    }
}