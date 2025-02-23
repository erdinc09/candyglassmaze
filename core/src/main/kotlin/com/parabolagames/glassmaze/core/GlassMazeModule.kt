package com.parabolagames.glassmaze.core

import com.badlogic.gdx.pay.PurchaseManager
import com.google.common.base.Supplier
import com.google.common.eventbus.EventBus
import com.parabolagames.glassmaze.candymode.CandyModeComponent
import com.parabolagames.glassmaze.candymode.ICandyCountPersistenceManager
import com.parabolagames.glassmaze.candymode.IGlassMazeCandyMode
import com.parabolagames.glassmaze.classic.ClassicModeComponent
import com.parabolagames.glassmaze.classic.IClassicMazePersistenceManager
import com.parabolagames.glassmaze.classic.IGlassMazeClassic
import com.parabolagames.glassmaze.classic.ILastLevelNumberProvider
import com.parabolagames.glassmaze.core.persistence.CandyMode
import com.parabolagames.glassmaze.core.persistence.ClassicMaze
import com.parabolagames.glassmaze.core.persistence.GeneralPersistence
import com.parabolagames.glassmaze.framework.ForApp
import com.parabolagames.glassmaze.framework.ICurrentScreenProvider
import com.parabolagames.glassmaze.framework.IPauseResumeFromGame
import com.parabolagames.glassmaze.loading.IGlassMazeLoading
import com.parabolagames.glassmaze.menu.IGlassMazeMenu
import com.parabolagames.glassmaze.menu.IMenuScreenPersistenceManager
import com.parabolagames.glassmaze.settings.ITermsOfServiceAndPrivacyPolicyPersistence
import com.parabolagames.glassmaze.shared.*
import com.parabolagames.glassmaze.store.IStorePersistenceManager
import com.parabolagames.glassmaze.store.classicmaze.IStorePersistenceManagerClassicMaze
import dagger.Module
import dagger.Provides
import javax.inject.Inject
import javax.inject.Named
import com.parabolagames.glassmaze.settings.EagerComponents as SettingsEagerComponents
import com.parabolagames.glassmaze.store.EagerComponents as StoreEagerComponents

@Module(subcomponents = [CandyModeComponent::class, ClassicModeComponent::class])
internal class GlassMazeModule(private val game: GlassMazeMain) {

    @Provides
    fun providePurchaseManager(): PurchaseManager = game.purchaseManager

    @Named(Version.VERSION_DAGGER_NAME_FOR_INJECTION)
    @Provides
    fun provideVersion(): String = Version.VERSION

    @Provides
    fun providePlatformFix(): IPlatformUiFix = game.platformFix

    @Provides
    fun provideBaseGameLoading(): IGlassMazeLoading = game

    @Provides
    fun provideBaseGameClassic(): IGlassMazeClassic = game

    @Provides
    fun provideICurrentScreenProvider(): ICurrentScreenProvider = game

    @Provides
    fun provideBaseGameMenu(): IGlassMazeMenu = game

    @Provides
    fun provideBaseGameCandyMode(): IGlassMazeCandyMode = game

    @Provides
    fun provideAddsController(): IAddsController = game.addsController

    @Provides
    fun provideSoundControl(soundPlayer: SoundPlayer): ISoundControl = soundPlayer

    @Provides
    fun provideIPausePlayFromGame(): Supplier<IPauseResumeFromGame> = game

    @Provides
    fun provideGameExplanationDialogAppearanceController(controller: GameExplanationDialogAppearanceController): IGameExplanationDialogAppearanceController = controller

    @Provides
    @ForApp
    fun provideEventBus(): EventBus = EventBus()

    @Provides
    @ForApp
    fun provideISoundPersistenceManager(generalPersistence: GeneralPersistence): ISoundPersistenceManager = generalPersistence

    @Provides
    @ForApp
    fun provideIClassicMazePersistenceManager(classicMaze: ClassicMaze): IClassicMazePersistenceManager = classicMaze

    @Provides
    @ForApp
    fun provideILastLevelNumberProvider(classicMaze: ClassicMaze): ILastLevelNumberProvider = classicMaze

    @Provides
    @ForApp
    fun provideICandyCountPersistenceManager(candyMode: CandyMode): ICandyCountPersistenceManager = candyMode

    @Provides
    @ForApp
    fun provideIMenuScreePersistenceManager(generalPersistence: GeneralPersistence): IMenuScreenPersistenceManager = generalPersistence

    @Provides
    @ForApp
    fun provideITermsOfServiceAndPrivacyPolicyPersistence(generalPersistence: GeneralPersistence): ITermsOfServiceAndPrivacyPolicyPersistence = generalPersistence

    @Provides
    @ForApp
    fun provideIStorePersistenceManager(generalPersistence: GeneralPersistence): IStorePersistenceManager = generalPersistence

    @Provides
    @ForApp
    fun provideIStorePersistenceManagerClassicMaze(classicMaze: ClassicMaze): IStorePersistenceManagerClassicMaze = classicMaze


    class EagerDependencies @Inject constructor() {
        @JvmField
        @Inject
        var storeEagerComponents: StoreEagerComponents? = null

        @JvmField
        @Inject
        var settingsEagerComponents: SettingsEagerComponents? = null
    }
}