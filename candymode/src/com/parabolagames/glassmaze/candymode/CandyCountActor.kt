package com.parabolagames.glassmaze.candymode

import com.badlogic.gdx.scenes.scene2d.actions.Actions.scaleBy
import com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence
import com.badlogic.gdx.scenes.scene2d.ui.Container
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.utils.Align
import com.parabolagames.glassmaze.framework.ForGame
import com.parabolagames.glassmaze.framework.TableActor
import com.parabolagames.glassmaze.framework.run2
import com.parabolagames.glassmaze.shared.Assets
import javax.inject.Inject

@ForGame
internal class CandyCountActor @Inject constructor(assets: Assets,
                                                   bestScoreActor: BestScoreActor,
                                                   private val dataPersistenceManager: ICandyCountPersistenceManager) : TableActor(), ICandyCounter {
    private val candyCountContainer: Container<Label>
    private val lblCandyCount: Label
    override var candy
        get() = dataPersistenceManager.totalScore
        set(value) {
            dataPersistenceManager.totalScore = value
        }
    private var actionFinished = true

    init {
        loadAnimationFromTextureRegions(
                assets.getTexturesFromTextureAtlas(Assets.CANDY14_ATLAS), 0.01f, true)
        setSize(HEIGHT * (width / height), HEIGHT)

        lblCandyCount = Label(candy.toString(), LabelStyle(assets.getBitmapFont(Assets.FONT_COSMIC_SANS_GREEN), null))
                .apply {
                    setAlignment(Align.left)
                    wrap = false
                }.also {
                    candyCountContainer = Container(it)
                            .apply {
                                setOrigin(it.width / 2, it.height / 2)
                                isTransform = true
                                setSize(it.width, it.height)
                                setPosition(this@CandyCountActor.width + 0.12f, 0.15f)
                            }
                    addActor(candyCountContainer)
                }
        addActor(bestScoreActor)
        bestScoreActor.setPosition(0.05f, -height + 0.15f)
    }

    override fun incrementCandy(): Int {
        candy++
        lblCandyCount.setText("$candy")
        return candy
    }

    override fun giftCandiesGained(count: Int) {
        candy += count
        lblCandyCount.setText(candy.toString())
        if (actionFinished) {
            candyCountContainer.clearActions()
            candyCountContainer.addAction(
                    sequence(
                            run2 { actionFinished = false },
                            scaleBy(0.2f, 0.2f, 0.1f),
                            scaleBy(-0.2f, -0.2f, 0.1f),
                            scaleBy(0.4f, 0.2f, 0.2f),
                            scaleBy(-0.4f, -0.2f, 0.2f),
                            run2 { actionFinished = true }))
        }
    }

    companion object {
        private const val HEIGHT = 0.30f
    }
}