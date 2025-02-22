package com.parabolagames.glassmaze.shared

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.utils.Align
import com.google.common.base.Preconditions.checkState
import com.parabolagames.glassmaze.framework.BaseScreen
import java.lang.Boolean

abstract class GameScreen : BaseScreen {
    protected val world: World

    @JvmField
    protected val assets: Assets
    private val worldRenderer: Box2DDebugRenderer
    protected var initialized = false
    private var lblFps: Label? = null

    // candy mode screen
    // classic mode screen
    constructor(
            mainStage_1: Stage,
            mainStage0: Stage,
            mainStage1: Stage,
            uiStage: Stage,
            uiStagePlayPause: Stage,
            dialogUiStage: Stage,
            dialogUiStageForInformation: Stage,
            explanationCandyStage: Stage?,
            world: World,
            assets: Assets) : super(
            mainStage_1,
            mainStage0,
            mainStage1,
            uiStage,
            uiStagePlayPause,
            dialogUiStage,
            dialogUiStageForInformation,
            explanationCandyStage) {
        this.world = world
        this.assets = assets
        worldRenderer = Box2DDebugRenderer()
        initializeDrawFps(uiStage, assets)
    }

    override fun show() {
        checkState(!initialized)
        initialize()
        super.show()
        initialized = true
    }

    protected abstract fun initialize()

    override fun dispose() {
        super.dispose()
        worldRenderer.dispose()
    }

    override fun update(dt: Float) {
        lblFps?.setText("fps: " + Gdx.app.graphics.framesPerSecond)
    }

    override fun renderDebug(dt: Float) {
        worldRenderer.render(world, mainStage0.camera.combined)
    }

    private fun initializeDrawFps(uiStage: Stage, assets: Assets) {
        if (Boolean.getBoolean("drawFps")) {
            val labelStyle = LabelStyle()
            labelStyle.font = assets.getBitmapFont(Assets.FONT_COMIC_SANS_MS)
            labelStyle.fontColor = Color.RED
            lblFps = Label("", labelStyle).apply {
                setAlignment(Align.left)
                wrap = false
                setSize(0.65f, 0.25f)
                setPosition(Constants.WORLD_WIDTH / 2 - width - PADDING, 0f)

            }.also { uiStage.addActor(it) }
        }
    }

    companion object {
        private const val PADDING = 0.05f
    }
}