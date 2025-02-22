package com.parabolagames.glassmaze.menu

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.actions.Actions.*
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import dagger.Lazy
import com.parabolagames.glassmaze.framework.ForApp
import com.parabolagames.glassmaze.framework.TableActor
import com.parabolagames.glassmaze.menu.UIButtonHelpers.addRingToStage
import com.parabolagames.glassmaze.menu.UIButtonHelpers.debugClickCoordinates
import com.parabolagames.glassmaze.shared.*
import com.parabolagames.glassmaze.shared.crack.GenericGlassCrackedPieceActor
import javax.inject.Inject
import javax.inject.Named

@ForApp
internal class RandomModeButtonActor @Inject constructor(
        @param:Named(MenuModule.UI_STAGE) private val mainStage: Stage,
        @param:Named(MenuModule.MENU_WORLD) private val world: World,
        private val controller: Lazy<IRandomModeButtonActorController>,
        private val assets: Assets,
        private val soundPlayer: SoundPlayer) : TableActor() {
    private val ringActor: TableActor
    private val clickListener = ClickListener()
    private val candyAtlas = Assets.CANDY4_ATLAS
    private val candyThrown: GenericCandyActor
    private var disabled = false
    private val candyInsideGlass: TableActor

    init {
        loadTexture(assets.getTexture(Assets.GLASS2))
        setOrigin(Constants.MENU_BUTTON_WIDTH_SMALL / 2, Constants.MENU_BUTTON_WIDTH_SMALL / 2)
        setSize(Constants.MENU_BUTTON_WIDTH_SMALL, Constants.MENU_BUTTON_WIDTH_SMALL)
        ringActor = TableActor().apply {
            loadTexture(assets.getTexture(Assets.RANDOM_RING))
            scaleBy(0.45f)
        }
        resetRingActor()
        addActor(ringActor)
        addListener(clickListener)
        candyInsideGlass = TableActor().apply {
            loadAnimationFromTextureRegions(
                    assets.getTexturesFromTextureAtlas(candyAtlas),
                    MathUtils.random(0.015f, 0.035f),
                    true,
                    MathUtils.random(0, assets.getTexturesFromTextureAtlas(candyAtlas).size))
            isAnimationPaused = false
            addAction(
                    forever(
                            sequence(scaleBy(-0.1f, -0.1f, 1f), scaleBy(0.1f, 0.1f, 1f))))
            setColor(0.6f, 0.6f, 0.6f, 1f)
            setSize(this@RandomModeButtonActor.width / 2f, this@RandomModeButtonActor.height / 2)
            setOrigin(width / 2, height / 2)
            setPosition(this@RandomModeButtonActor.width / 2 - width / 2, this@RandomModeButtonActor.height / 2 - height / 2)
        }.also { addActor(it) }
        candyThrown = GenericCandyActor(assets, candyAtlas, CANDY_SIZE, true,{ MathUtils.random(0.012f, 0.018f) })
    }

    private fun resetRingActor() {
        with(ringActor) {
            setOrigin(Constants.MENU_BUTTON_WIDTH_SMALL / 2, Constants.MENU_BUTTON_WIDTH_SMALL / 2)
            setSize(Constants.MENU_BUTTON_WIDTH_SMALL, Constants.MENU_BUTTON_WIDTH_SMALL)
            setPosition(0f, 0f)
            addAction(forever(rotateBy(-360f, 15f)))
            touchable = Touchable.disabled
            color = Color.WHITE
        }
    }

    override fun act(dt: Float) {
        if (!disabled && clickListener.isPressed) {
            controller.get().randomModeButtonPressed()
            soundPlayer.playRandomBreak()
            debugClickCoordinates(clickListener, this)
            addRingToStage(ringActor, stage)
            remove()
            GenericGlassCrackedPieceActor.createAllPiecesWithSplit(
                    x,
                    y,
                    assets,
                    width,
                    GenericGlassBallCrackedPieceActorData.GLASS_2,
                    GenericGlassCrackedPieceActor.DEFAULT_FADE_OUT_TIME,
                    GenericGlassCrackedPieceActor.DEFAULT_DELAY_TIME,
                    mainStage)
            ringActor.addAction(
                    sequence(
                            delay(2f), Actions.run { controller.get().setRandomModeScreenVisible() }))
            with(candyThrown) {
                reInit(this@RandomModeButtonActor.x + this@RandomModeButtonActor.width / 2 - CANDY_SIZE / 2,
                        this@RandomModeButtonActor.y + this@RandomModeButtonActor.height / 2 - CANDY_SIZE / 2,
                        world)
                applyForce(3f, 6f)
                mainStage.addActor(this)
                setAnimationAlignedFromActor(candyInsideGlass)
                Box2DActorDelayedRemover.add(this)
            }
        }
        super.act(dt)
    }

    private fun resetCandy() {
        with(candyThrown) {
            clearActions()
            if (parent != null) {
                remove()
                destroyBody()
            }
        }
    }

    fun setButtonDisabled() {
        ringActor.clearActions()
        candyInsideGlass.isAnimationPaused = true
        candyInsideGlass.clearActions()
        disabled = true
        color = Color.LIGHT_GRAY
        ringActor.color = Color.LIGHT_GRAY
    }

    fun reset() {
        ringActor.clearActions()
        candyInsideGlass.isAnimationPaused = false
        candyInsideGlass.clearActions()
        disabled = false
        color = Color.WHITE
        resetRingActor()
        addActor(ringActor)
        resetCandy()
    }

    companion object {
        private const val TAG = "ClassicModeButtonActor"
        private const val CANDY_SIZE = 0.25f
    }
}