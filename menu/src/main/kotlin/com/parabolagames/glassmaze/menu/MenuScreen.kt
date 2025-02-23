package com.parabolagames.glassmaze.menu

import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions.*
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.google.common.base.Preconditions.checkState
import com.google.common.eventbus.EventBus
import com.google.common.eventbus.Subscribe
import com.parabolagames.glassmaze.framework.BaseScreen
import com.parabolagames.glassmaze.framework.CurrentScreen
import com.parabolagames.glassmaze.framework.ForApp
import com.parabolagames.glassmaze.framework.proguard.Keep
import com.parabolagames.glassmaze.shared.*
import com.parabolagames.glassmaze.shared.event.EventAdsRemoveButtonPressed
import com.parabolagames.glassmaze.shared.event.EventAdsRemoved
import com.parabolagames.glassmaze.shared.event.EventOpenSettingsDialog
import com.parabolagames.glassmaze.shared.event.EventOpenStoreDialog
import javax.inject.Inject
import javax.inject.Named
import kotlin.system.exitProcess

@ForApp
internal class MenuScreen @Inject constructor(
        @Named(MenuModule.MAIN_STAGE) mainStage: Stage,
        @Named(MenuModule.UI_STAGE) uiStage: Stage,
        @Named(MenuModule.DIALOG_UI_STAGE) dialogUiStage: Stage,
        @Named(MenuModule.DIALOG_UI_STAGE_FOR_INFORMATION) dialogUiStageForInformation: Stage,
        private val menuBackGroundActor: MenuBackGroundActor,
        private val randomModeButtonActor: RandomModeButtonActor,
        private val candyTextActor: CandyTextActor,
        private val candyModeButtonActor: CandyModeButtonActor,
        private val classicModeButtonActor: ClassicModeButtonActor,
        @param:Named(MenuModule.MENU_WORLD) private val world: World,
        private val assets: Assets,
        private val game: IGlassMazeMenu,
        private val gameExplanationDialogAppearanceController: IGameExplanationDialogAppearanceController,
        private val soundPlayer: SoundPlayer,
        private val addsController: IAddsController,
        private val eventBus: EventBus,
        private val menuScreenPersistenceManager: IMenuScreenPersistenceManager)
    : BaseScreen(mainStage, uiStage, dialogUiStage, dialogUiStageForInformation),
        ICandyModeButtonActorController,
        IClassicModeButtonActorController,
        IRandomModeButtonActorController {

    private val worldRenderer: Box2DDebugRenderer = Box2DDebugRenderer()
    private val settingsButton: Button = Button()
    private val addsRemoveButton: Button = Button()
    private val storeButton: Button = Button()
    private val candyModeWillBeAdded = java.lang.Boolean.getBoolean("candyMode")
    override val currentScreen: CurrentScreen = CurrentScreen.MENU
    private var initialized = false

    init {
        eventBus.register(this)
    }

    @Inject
    fun initialize() {
        checkState(!initialized)
        initializeAndAddSettingsButton()
        if (!menuScreenPersistenceManager.isAdsRemoved) {
            initializeAndAddAdsRemoveButton()
        }
        initializeAndAddStoreButton()
        addMenuBackGround()
        addCandyText()
        if (candyModeWillBeAdded) {
            addCandyModeButton()
        }
        addClassicModeButton()
        addRandomModeButton()
        world.setContactListener(ContactController())
        initialized = true
    }

    // do not dispose, reset the screen since we are reusing it
    override fun dispose() = Gdx.app.postRunnable(::reset)


    private fun initializeAndAddSettingsButton() {
        with(settingsButton) {
            val textureAtlas = assets.getTextureAtlas(Assets.BUTTONS_SETTINGS)
            style = ButtonStyle(
                    TextureRegionDrawable(textureAtlas.findRegion("settings_released")),
                    TextureRegionDrawable(textureAtlas.findRegion("settings_pressed")),
                    null)
            addListener(
                    object : ChangeListener() {
                        override fun changed(event: ChangeEvent, actor: Actor) {
                            eventBus.post(EventOpenSettingsDialog())
                        }
                    })
        }
        resetSettingsButton()
    }

    private fun initializeAndAddAdsRemoveButton() {
        with(addsRemoveButton) {
            val textureAtlas = assets.getTextureAtlas(Assets.BUTTONS_ADS_REMOVE)
            style = ButtonStyle(
                    TextureRegionDrawable(textureAtlas.findRegion("ads_remove_released")),
                    TextureRegionDrawable(textureAtlas.findRegion("ads_remove_pressed")),
                    null)
            addsRemoveButton.addListener(
                    object : ChangeListener() {
                        override fun changed(event: ChangeEvent, actor: Actor) {
                            eventBus.post(EventAdsRemoveButtonPressed())
                        }
                    })
        }
        resetAdsRemoveButton()
    }

    private fun initializeAndAddStoreButton() {
        with(storeButton) {
            val textureAtlas = assets.getTextureAtlas(Assets.BUTTONS_STORE)
            style = ButtonStyle(
                    TextureRegionDrawable(textureAtlas.findRegion("store_released")),
                    TextureRegionDrawable(textureAtlas.findRegion("store_pressed")),
                    null)
            storeButton.addListener(
                    object : ChangeListener() {
                        override fun changed(event: ChangeEvent, actor: Actor) = eventBus.post(EventOpenStoreDialog())
                    })
        }
        resetStoreButton()
    }

    private fun resetAdsRemoveButton() {
        addsRemoveButton.clearActions()
        addsRemoveButton
                .apply {
                    setSize(0.3f, 0.3f)
                    setPosition(Constants.WORLD_WIDTH - width - 0.02f, settingsButton.y + settingsButton.height + 0.08f)
                    val rightPad = settingsButton.width + 0.1f
                    moveBy(rightPad, 0f)
                    addAction(sequence(delay(0.5f), moveBy(-rightPad, 0f, 0.5f)))
                }.also { uiStage.addActor(it) }
    }

    private fun resetStoreButton() {
        storeButton.clearActions()
        storeButton.apply {
            setSize(0.3f, 0.3f)
            setPosition(Constants.WORLD_WIDTH - width - 0.02f, settingsButton.y - height - 0.08f)
            val rightPad = settingsButton.width + 0.1f
            moveBy(rightPad, 0f)
            addAction(sequence(delay(0.5f), moveBy(-rightPad, 0f, 0.5f)))
        }.also { uiStage.addActor(it) }
    }

    private fun resetSettingsButton() {
        settingsButton.clearActions()
        settingsButton.apply {
            setSize(0.3f, 0.3f)
            setPosition(Constants.WORLD_WIDTH - width - 0.02f,
                    if (!menuScreenPersistenceManager.isAdsRemoved) Constants.WORLD_HEIGHT - height - 0.4f
                    else Constants.WORLD_HEIGHT - 0.4f)
            val rightPad = width + 0.1f
            moveBy(rightPad, 0f)
            addAction(sequence(delay(0.5f), moveBy(-rightPad, 0f, 0.5f)))
        }.also { uiStage.addActor(it) }

    }

    override fun show() {
        super.show()
        settingsButton.isVisible = true
        addsRemoveButton.isVisible = true
        storeButton.isVisible = true
        gameExplanationDialogAppearanceController.isItTimeToShowGameExplanationDialog = true
        soundPlayer.playMenuMusic()
    }

    override fun hide() {
        super.hide()
        soundPlayer.stopMenuMusic()
    }

    private fun addCandyText() {
        mainStage0.addActor(candyTextActor)
        candyTextActor.setPositionAfterStageSet()
        candyTextActor.startAnimation()
    }

    private fun addMenuBackGround() = mainStage0.addActor(menuBackGroundActor)

    private fun addClassicModeButton() {
        classicModeButtonActor.setPosition(Constants.MENU_BUTTON_PAD, Constants.MENU_BUTTON_PAD)
        uiStage.addActor(classicModeButtonActor)
    }

    private fun addRandomModeButton() {
        randomModeButtonActor.setPosition(5f * Constants.MENU_BUTTON_PAD, classicModeButtonActor.y + classicModeButtonActor.height + 0.5f * Constants.MENU_BUTTON_PAD)
        uiStage.addActor(randomModeButtonActor)
    }

    private fun addCandyModeButton() {
        candyModeButtonActor.setPosition(Constants.WORLD_WIDTH - Constants.MENU_BUTTON_WIDTH_SMALL - Constants.MENU_BUTTON_PAD, Constants.MENU_BUTTON_PAD * 1.5f)
        uiStage.addActor(candyModeButtonActor)
    }

    override fun update(dt: Float) = world.step(1 / 60f, 6, 2)

    override fun keyDown(keycode: Int): Boolean {
        if (keycode == Input.Keys.BACK || keycode == Input.Keys.DEL || keycode == Input.Keys.ESCAPE) {
            if (isPaused) {
                return false
            }
            Gdx.app.postRunnable {
                ApprovalDialog(
                        assets,
                        "Quit Game ?",
                        {
                            when (val platform = Gdx.app.type) {
                                Application.ApplicationType.Desktop -> exitProcess(0)
                                Application.ApplicationType.Android -> {
                                    //Schedule an exit from the application. On android, this will cause a call to pause() and dispose() some time in the future,
                                    // it will not immediately finish your application. On iOS this should be avoided in production as it breaks Apples guidelines
                                    Gdx.app.exit()
                                }
                                Application.ApplicationType.iOS -> {
                                    /*You never ever exit an app on ios!*/
                                }
                                else -> error("unsupported platform $platform")
                            }
                        },
                        null,
                        addsController).apply { show(dialogUiStage) }
            }
            return true
        }
        return false
    }

    private fun reset() {
        if (randomModeButtonActor.parent == null) {
            addRandomModeButton()
        }
        randomModeButtonActor.reset()
        if (classicModeButtonActor.parent == null) {
            addClassicModeButton()
        }
        classicModeButtonActor.reset()
        if (candyModeWillBeAdded) {
            if (candyModeButtonActor.parent == null) {
                addCandyModeButton()
            }
            candyModeButtonActor.reset()
        }
        resetSettingsButton()
        if (!menuScreenPersistenceManager.isAdsRemoved) {
            resetAdsRemoveButton()
        }
        resetStoreButton()
        candyTextActor.setPositionAfterStageSet()
        candyTextActor.stopAnimation()
        candyTextActor.startAnimation()
    }

    override fun renderDebug(dt: Float) = worldRenderer.render(world, mainStage0.camera.combined)

    override fun candyModeButtonPressed() {
        settingsButton.isVisible = false
        addsRemoveButton.isVisible = false
        storeButton.isVisible = false
        candyTextActor.stopAnimation()
        classicModeButtonActor.setButtonDisabled()
        randomModeButtonActor.setButtonDisabled()
    }

    override fun classicModeButtonPressed() {
        settingsButton.isVisible = false
        addsRemoveButton.isVisible = false
        storeButton.isVisible = false
        candyTextActor.stopAnimation()
        if (candyModeWillBeAdded) {
            candyModeButtonActor.setButtonDisabled()
        }
        randomModeButtonActor.setButtonDisabled()
    }

    override fun randomModeButtonPressed() {
        settingsButton.isVisible = false
        addsRemoveButton.isVisible = false
        storeButton.isVisible = false
        candyTextActor.stopAnimation()
        if (candyModeWillBeAdded) {
            candyModeButtonActor.setButtonDisabled()
        }
        classicModeButtonActor.setButtonDisabled()
    }

    override fun setCandyModeScreenVisible() = game.setCandyModeScreenVisible()

    override fun setClassicModeScreenVisible() = game.setClassicModeScreenVisible()

    override fun setRandomModeScreenVisible() = game.setRandomModeScreenVisible()

    @Subscribe
    @Keep
    private fun ear(event: EventAdsRemoved) = Gdx.app.postRunnable {
        addsRemoveButton.remove()
        settingsButton.remove()
        storeButton.remove()
        reset()
    }

    companion object {
        private const val TAG = "com.parabolagames.glassmaze.MenuScreen"
    }
}