package com.parabolagames.glassmaze.classic

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions.scaleBy
import com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.utils.Align
import com.parabolagames.glassmaze.framework.ForGame
import com.parabolagames.glassmaze.framework.TableActor
import com.parabolagames.glassmaze.framework.run2
import com.parabolagames.glassmaze.shared.Assets
import com.parabolagames.glassmaze.shared.ui.GM_Container
import javax.inject.Inject
import javax.inject.Named

@ForGame
internal class CandyCountActor
@Inject constructor(
        assets: Assets,
        private val dataPersistenceManager: IClassicMazePersistenceManager,
        @param:Named(ClassicModeModule2.CLASSIC_MAIN_STAGE_0) private val mainStage0: Stage)
    : TableActor(), ICandyCounter, ICandyCounterPositionProvider {

    private val candyCountContainer: GM_Container<Label>
    private val lblCandyCount: Label

    override var candy: Int
        set(value) {
            dataPersistenceManager.totalScore = value
        }
        get() = dataPersistenceManager.totalScore


    override var candyInLevel: Int = 0
    override var isCandyGiftForAllGlassCrackedGained = false
    private var actionFinished = true

    val candyCountWidth: Float
        get() = lblCandyCount.width

    override val posX: Float
        get() = mainStage0Coordinates.x
    override val posY: Float
        get() = mainStage0Coordinates.y

    private val mainStage0Coordinates = Vector2()

    init {
        loadAnimationFromTextureRegions(assets.getTexturesFromTextureAtlas(Assets.CANDY14_ATLAS), 0.01f, true)
        setSize(HEIGHT * (width / height), HEIGHT)

        lblCandyCount = Label(dataPersistenceManager.totalScore.also { candy = it }.toString(),
                LabelStyle(assets.getBitmapFont(Assets.FONT_COSMIC_SANS_GREEN), null))
                .apply {
                    setAlignment(Align.left)
                    wrap = false
                    setFontScale(0.0020f)
                    setSize(this@CandyCountActor.width, this@CandyCountActor.height)
                }.also { lbl ->
                    candyCountContainer = GM_Container(lbl)
                            .apply {
                                setPosition(width + 0.05f, 0.10f)
                            }.also {
                                addActor(it)
                            }
                }
    }


    override fun positionChanged() {
        mainStage0Coordinates.set(0f + width / 2, 0f + height / 2)
        localToScreenCoordinates(mainStage0Coordinates)
        mainStage0.screenToStageCoordinates(mainStage0Coordinates)
    }

    override fun incrementCandy(): Int {
        candy++
        candyInLevel++
        lblCandyCount.setText(candy.toString())
        startCountAnimation()
        return candy
    }

    override fun updateFromDataPersistenceManager() = lblCandyCount.setText("$candy")

    override fun giftCandiesWillBeGained() {
        isCandyGiftForAllGlassCrackedGained = true
    }

    private fun startCountAnimation() {
        if (actionFinished) {
            with(candyCountContainer) {
                clearActions()
                addAction(
                        sequence(
                                run2 { actionFinished = false },
                                scaleBy(0.2f, 0.2f, 0.1f),
                                scaleBy(-0.2f, -0.2f, 0.1f),
                                scaleBy(0.4f, 0.2f, 0.1f),
                                scaleBy(-0.4f, -0.2f, 0.1f),
                                run2() { actionFinished = true }))
            }
        }
    }


    companion object {
        private const val HEIGHT = 0.20f
    }
}