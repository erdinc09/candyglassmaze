package com.parabolagames.glassmaze.core

import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.pay.PurchaseManager
import com.google.common.base.Preconditions.checkArgument
import com.google.common.base.Preconditions.checkState
import com.google.common.base.Supplier
import com.google.common.eventbus.EventBus
import com.parabolagames.glassmaze.candymode.CandyModeComponent
import com.parabolagames.glassmaze.candymode.IGlassMazeCandyMode
import com.parabolagames.glassmaze.classic.ClassicModeBuilderProxy
import com.parabolagames.glassmaze.classic.IGlassMazeClassic
import com.parabolagames.glassmaze.core.persistence.GeneralPersistence
import com.parabolagames.glassmaze.framework.*
import com.parabolagames.glassmaze.loading.IGlassMazeLoading
import com.parabolagames.glassmaze.loading.LoadingScreenProvider
import com.parabolagames.glassmaze.menu.IGlassMazeMenu
import com.parabolagames.glassmaze.menu.MenuScreenProvider
import com.parabolagames.glassmaze.shared.Assets
import com.parabolagames.glassmaze.shared.IAddsController
import com.parabolagames.glassmaze.shared.IPlatformUiFix
import com.parabolagames.glassmaze.shared.event.EventGameFullyLoaded
import javax.inject.Inject
import com.parabolagames.glassmaze.classic.ObjectPoolsDisposer as ClassicObjectPoolsDisposer
import com.parabolagames.glassmaze.store.ObjectPoolsDisposer as StoreObjectPoolsDisposer

internal class GlassMazeMain(val platformFix: IPlatformUiFix,
                             val addsController: IAddsController,
                             val purchaseManager: PurchaseManager)
    : BaseGame(),
        IGlassMazeLoading,
        IGlassMazeClassic,
        IGlassMazeMenu,
        IGlassMazeCandyMode,
        Supplier<IPauseResumeFromGame>,
        ICurrentScreenProvider {

    @Inject
    lateinit var loadingScreenProvider: LoadingScreenProvider

    @Inject
    lateinit var candyModeComponentBuilder: CandyModeComponent.Builder

    @Inject
    lateinit var classicModeBuilder: ClassicModeBuilderProxy

    private lateinit var menuScreen: Screen

    @Inject
    lateinit var menuScreenProvider: MenuScreenProvider

    @Inject
    lateinit var generalPersistence: GeneralPersistence

    @Inject
    lateinit var eventBus: EventBus

    @Inject
    lateinit var assets: Assets

    private lateinit var glassMazeComp: GlassMazeComp

    override fun create() {
        Gdx.app.logLevel = Application.LOG_NONE
        super.create()
        glassMazeComp = DaggerGlassMazeComp.builder().glassMazeModule(GlassMazeModule(this)).build()
        glassMazeComp.createEagerDependencies()
        glassMazeComp.inject(this)
        setScreen(loadingScreenProvider.getScreen())
        addsController.initAds()
    }

    override fun assetLoadingHasFinished() {
        checkState(!::menuScreen.isInitialized)
        menuScreen = menuScreenProvider.getScreen()
        setScreen(menuScreen)
        eventBus.post(EventGameFullyLoaded())
    }

    fun isAdsRemoved(): Boolean = generalPersistence.isAdsRemoved

    override fun setClassicModeScreenVisible() = setScreen(classicModeBuilder.getScreenClassicMode())
    //override fun setClassicModeScreenVisible() = setScreen(classicModeBuilder.getScreenClassicMode(20))

    override fun setRandomModeScreenVisible() = setScreen(classicModeBuilder.getScreenRandomMode())

    override fun setClassicModeScreenVisibleForLevelClassicMode(levelNumber: Int) = setScreen(classicModeBuilder.getScreenClassicMode(levelNumber))

    override fun setClassicModeScreenVisibleForLevelRandomMode(levelNumber: Int) = setScreen(classicModeBuilder.getScreenRandomMode(levelNumber))

    override fun setMenuScreenReVisible() = setScreen(menuScreen)

    override fun setCandyModeScreenVisible() = setScreen(candyModeComponentBuilder.build().screen)

    override fun get(): IPauseResumeFromGame = screen as IPauseResumeFromGame

    override fun setScreen(screen: Screen) {
        checkArgument(screen is IPauseResumeFromGame)
        DialogController.pauseResumeFromGame = screen as IPauseResumeFromGame
        super.setScreen(screen)
    }

    override val currentScreen: CurrentScreen
        get() = (screen as? BaseScreen)?.currentScreen ?: CurrentScreen.NOT_READY

    override fun dispose() {
        Gdx.app.debug(TAG, "disposing")
        ClassicObjectPoolsDisposer.disposeAll()
        StoreObjectPoolsDisposer.disposeAll()
        purchaseManager.dispose()
        assets.dispose()
        Gdx.app.debug(TAG, "disposed everything")
    }

    companion object {
        const val TAG = "com.parabolagames.glassmaze.GlassMazeMain"
    }
}