package com.parabolagames.glassmaze.loading

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Stage
import com.parabolagames.glassmaze.framework.BaseScreen
import com.parabolagames.glassmaze.framework.CurrentScreen
import com.parabolagames.glassmaze.framework.ForApp
import com.parabolagames.glassmaze.shared.Assets
import javax.inject.Inject
import javax.inject.Named

@ForApp
internal class LoadingScreen @Inject internal constructor(
        @Named(LoadingModule.MAIN_STAGE) mainStage: Stage?,
        @Named(LoadingModule.UI_STAGE) uiStage: Stage?,
        private val assets: Assets,
        private val game: IGlassMazeLoading,
        private val loadingActor: LoadingActor,
        private val loadingBar: LoadingBar,
        private val versionActor: VersionActor) : BaseScreen(mainStage, uiStage) {

    private var loadingFinished = false
    override val currentScreen: CurrentScreen = CurrentScreen.LOADING

    @Inject
    fun initialize() {
        mainStage0.addActor(loadingActor)
        mainStage0.addActor(loadingBar)
        uiStage.addActor(versionActor)
        loadingBar.setPositionAfterStageSet(loadingActor.height)
        loadingActor.startAnimation(
                { loadingFinished },
                {
                    Gdx.app.debug(LoadingScreen::class.java.name, "Loading Finished")
                    game.assetLoadingHasFinished()
                    loadingActor.stopAnimation()
                })
    }

    override fun dispose() {
        versionActor.dispose()
        super.dispose()
        Gdx.app.debug(TAG, "disposed")
    }

    override fun update(dt: Float) {
        if (!loadingFinished) {
            loadingFinished = assets.update()
            loadingBar.value = assets.progress
        }
    }

    companion object {
        private const val TAG = "LoadingScreen"
    }
}