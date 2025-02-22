package com.parabolagames.glassmaze.classic

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.FitViewport
import com.parabolagames.glassmaze.framework.BaseScreen
import com.parabolagames.glassmaze.framework.DialogStage
import com.parabolagames.glassmaze.framework.ForGame
import com.parabolagames.glassmaze.framework.IScreenDrawer
import com.parabolagames.glassmaze.shared.Assets
import com.parabolagames.glassmaze.shared.Constants
import dagger.Lazy
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
internal object ClassicModeModule2 {

    @ForGame
    @Named(CLASSIC_RANDOM_PLAY_LEVEL_LIMIT)
    @Provides
    fun provideLevelLimitForRandomPlay(): Int = 10

    @ForGame
    @Provides
    fun getScreen(screen: ClassicModeScreen): BaseScreen = screen

    @ForGame
    @Provides
    fun provideScreenDrawer(screen: ClassicModeScreen): IScreenDrawer = screen

    @ForGame
    @Provides
    fun provideClassicModeInputController(screen: ClassicModeScreen): IClassicModeInputController = screen

    @ForGame
    @Provides
    fun provideILevelNumber(level: Level?): ILevelNumberProvider? = level

    @Provides
    @Named(CLASSIC_MAIN_STAGE_0)
    @ForGame
    fun provideMain0Stage(): Stage = Stage(FitViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT))

    @Provides
    @Named(CLASSIC_UI_STAGE_PLAY_PAUSE)
    @ForGame
    fun provideUiStagePlayPause(): Stage = Stage(FitViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT))

    @Provides
    @Named(CLASSIC_MAIN_STAGE_1)
    @ForGame
    fun provideMain1Stage(): Stage = Stage(FitViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT))

    @Provides
    @Named(CLASSIC_MAIN_STAGE__1)
    @ForGame
    fun provideMain_1Stage(): Stage = Stage(FitViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT))

    @Provides
    @Named(CLASSIC_EXPLANATION_CANDY_STAGE_1)
    @ForGame
    fun provideExplanationCandyStage(): Stage = Stage(FitViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT))

    @Provides
    @Named(CLASSIC_UI_STAGE)
    @ForGame
    fun provideUiStage(): Stage = Stage(FitViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT))

    @Provides
    @Named(CLASSIC_DIALOG_UI_STAGE)
    @ForGame
    fun provideDialogUiStage(): Stage = DialogStage(FitViewport(Constants.WORLD_WIDTH * Constants.DIALOG_SIZE_RATIO,
            Constants.WORLD_HEIGHT * Constants.DIALOG_SIZE_RATIO))

    @Provides
    @Named(CLASSIC_DIALOG_UI_STAGE_FOR_INFORMATION)
    @ForGame
    fun provideDialogUiStageForInformation(): Stage = DialogStage(FitViewport(Constants.WORLD_WIDTH * Constants.DIALOG_SIZE_RATIO,
            Constants.WORLD_HEIGHT * Constants.DIALOG_SIZE_RATIO))

    @Provides
    @Named(CLASSIC_WORLD)
    @ForGame
    fun provideWorld(): World = World(Vector2(0f, -2.8f), true)

    @Provides
    @ForGame
    fun provideHandActorSpinyGlass(
            assets: Assets,
            dataPersistenceManager: IClassicMazePersistenceManager,
            listener: Lazy<IHandActorListenerClassicMode>,
            levelNumberProvider: ILevelNumberProvider?,
            noHandCountListener: Lazy<INoHandCountListener>,
            @Named(ClassicModeModule2.CLASSIC_MAIN_STAGE_0) mainStage0: Stage): HandActorSpinyGlass =
            HandActorSpinyGlass.createHandActorForClassicMode(assets, dataPersistenceManager, listener, levelNumberProvider, noHandCountListener, mainStage0)

    @Provides
    @ForGame
    internal fun provideHandActorCandyGlass(
            assets: Assets,
            dataPersistenceManager: IClassicMazePersistenceManager,
            listener: Lazy<IHandActorListenerClassicMode>,
            noHandCountListener: Lazy<INoHandCountListener>,
            @Named(ClassicModeModule2.CLASSIC_MAIN_STAGE_0) mainStage0: Stage): HandActorCandyGlass =
            HandActorCandyGlass.createHandActorForClassicMode(assets, dataPersistenceManager, listener, noHandCountListener, mainStage0)

    @ForGame
    @Provides
    fun provideLevel(levelController: LevelController): Level? = levelController.createLevel()

    @ForGame
    @Provides
    fun provideIHandCounter(actor: HandActorCandyGlass): ICandyGlassHandActor = actor

    @ForGame
    @Provides
    fun provideISpinyGlassHandActor(actor: HandActorSpinyGlass): ISpinyGlassHandActor = actor

    @ForGame
    @Provides
    fun provideICandyCounter(candyCountActor: CandyCountActor): ICandyCounter = candyCountActor

    @ForGame
    @Provides
    fun provideICandyCounterPositionProvider(candyCountActor: CandyCountActor): ICandyCounterPositionProvider = candyCountActor

    @ForGame
    @Provides
    fun provideICandyHandActorPositionProvider(actor: HandActorCandyGlass): ICandyHandActorBonusActorInterface = actor

    @ForGame
    @Provides
    fun provideISpinyHandActorPositionProvider(actor: HandActorSpinyGlass): ISpinyHandActorBonusActorInterface = actor


    @ForGame
    @Provides
    fun provideIMazeCubeControl(controller: ClassicModeGameController): IMazeCubeControl = controller

    @ForGame
    @Provides
    fun provideICandyGainer(controller: ClassicModeGameController): ICandyGainer = controller

    @ForGame
    @Provides
    fun provideIGlassBallBreakListener(controller: ClassicModeGameController): IGlassBallBreakListener = controller

    @ForGame
    @Provides
    fun provideINoHandCountListener(controller: ClassicModeGameController): INoHandCountListener = controller


    @ForGame
    @Provides
    fun provideIHandActorListenerClassicMode(mazeController: MazeController): IHandActorListenerClassicMode = mazeController

    @ForGame
    @Provides
    fun provideICandyModeScreenGameFinishedListener(screen: ClassicModeScreen): IClassicModeScreenGameFinishedListener = screen

    @ForGame
    @Provides
    fun provideITouchCoordinatesInGameAreaController(screen: ClassicModeScreen): ITouchCoordinatesInGameAreaController = screen


    const val CLASSIC_MAIN_STAGE__1 = "classicModeMainStage__1"
    const val CLASSIC_MAIN_STAGE_0 = "classicModeMainStage_0"
    const val CLASSIC_MAIN_STAGE_1 = "classicModeMainStage_1"
    const val CLASSIC_UI_STAGE = "classicModeUiStage"
    const val CLASSIC_DIALOG_UI_STAGE = "classicModeDialogUiStage"
    const val CLASSIC_DIALOG_UI_STAGE_FOR_INFORMATION = "classicModeDialogUiStageForInformation"
    const val CLASSIC_EXPLANATION_CANDY_STAGE_1 = "classicModeExpCandyStage"
    const val CLASSIC_UI_STAGE_PLAY_PAUSE = "classicModeUiStagePlayPause"
    const val CLASSIC_WORLD = "classicWorld"
    const val CLASSIC_RANDOM_PLAY_LEVEL_LIMIT = "classicRandomPlayLevelLimit"
}