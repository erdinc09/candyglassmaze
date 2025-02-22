package com.parabolagames.glassmaze.candymode

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.FitViewport
import com.parabolagames.glassmaze.candymode.HandActorCandyGlass.Companion.createHandActorForFiniteMode
import com.parabolagames.glassmaze.framework.BaseScreen
import com.parabolagames.glassmaze.framework.DialogStage
import com.parabolagames.glassmaze.framework.ForGame
import com.parabolagames.glassmaze.framework.IScreenDrawer
import com.parabolagames.glassmaze.shared.Assets
import com.parabolagames.glassmaze.shared.Constants
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
internal abstract class CandyModeModule {
    @ForGame
    @Binds
    abstract fun getScreen(candyModeScreen: CandyModeScreen): BaseScreen

    @ForGame
    @Binds
    abstract fun provideScreenDrawer(candyModeScreen: CandyModeScreen): IScreenDrawer

    @ForGame
    @Binds
    abstract fun provideCandyGainer(candyModeGameController: CandyModeGameController): ICandyGainer

    @ForGame
    @Binds
    abstract fun provideCandyModeScreenGameFinishedListener(candyModeScreen: CandyModeScreen): ICandyModeScreenGameFinishedListener

    @ForGame
    @Binds
    abstract fun provideIGlassMissedListener(candyModeGameController: CandyModeGameController): IGlassMissedListener

    @ForGame
    @Binds
    abstract fun provideICandyCounter(candyCountActor: CandyCountActor): ICandyCounter

    @ForGame
    @Binds
    abstract fun provideIHandCounter(handCountActor: HandActorCandyGlass): IHandCounter

    @ForGame
    @Binds
    abstract fun provideIBestScoreCounter(bestScoreActor: BestScoreActor): IBestScoreCounter

    @ForGame
    @Binds
    abstract fun provideILifeCounter(lifeActor: LifeActor): ILifeCounter

    companion object {
        const val CANDY_MAIN_STAGE__1 = "candyModeMainStage__1"
        const val CANDY_MAIN_STAGE_0 = "candyModeMainStage_0"
        const val CANDY_MAIN_STAGE_1 = "candyModeMainStage_1"
        const val CANDY_UI_STAGE = "candyModeUiStage"
        const val CANDY_DIALOG_UI_STAGE = "candyModeDialogUiStage"
        const val CANDY_DIALOG_UI_STAGE_FOR_INFORMATION = "candyModeDialogUiStageForInformation"
        const val CANDY_UI_STAGE_PLAY_PAUSE = "candyModeUiStagePlayPause"
        const val CANDY_WORLD = "candyWorld"

        @Provides
        @Named(CANDY_MAIN_STAGE_0)
        @ForGame
        fun provideMain0Stage() = Stage(FitViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT))

        @Provides
        @Named(CANDY_MAIN_STAGE_1)
        @ForGame
        fun provideMain1Stage() = Stage(FitViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT))

        @Provides
        @Named(CANDY_MAIN_STAGE__1)
        @ForGame
        fun provideMain_1Stage() = Stage(FitViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT))

        @Provides
        @Named(CANDY_UI_STAGE)
        @ForGame
        fun provideUiStage() = Stage(FitViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT))

        @Provides
        @Named(CANDY_DIALOG_UI_STAGE)
        @ForGame
        fun provideDialogUiStage():Stage = DialogStage(FitViewport(
                Constants.WORLD_WIDTH * Constants.DIALOG_SIZE_RATIO,
                Constants.WORLD_HEIGHT * Constants.DIALOG_SIZE_RATIO))

        @Provides
        @Named(CANDY_DIALOG_UI_STAGE_FOR_INFORMATION)
        @ForGame
        fun provideDialogUiStageForInformation():Stage = DialogStage(FitViewport(
                Constants.WORLD_WIDTH * Constants.DIALOG_SIZE_RATIO,
                Constants.WORLD_HEIGHT * Constants.DIALOG_SIZE_RATIO))

        @Provides
        @Named(CANDY_UI_STAGE_PLAY_PAUSE)
        @ForGame
        fun provideUiStagePlayPause() = Stage(FitViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT))

        @Provides
        @Named(CANDY_WORLD)
        @ForGame
        fun provideMenuWorld(contactController: ContactController) = World(Vector2(0f, -2.8f), true)
                .apply { setContactListener(contactController) }

        @Provides
        @ForGame
        fun provideContactController() = ContactController()

        @Provides
        @ForGame
        fun provideHandActor(assets: Assets, dataPersistenceManager: ICandyCountPersistenceManager) =
                createHandActorForFiniteMode(assets, dataPersistenceManager)
    }
}