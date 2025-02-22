package com.parabolagames.glassmaze.candymode

import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.actions.Actions.scaleBy
import com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Align
import com.parabolagames.glassmaze.framework.ForGame
import com.parabolagames.glassmaze.framework.run2
import com.parabolagames.glassmaze.shared.Assets
import javax.inject.Inject

@ForGame
internal class BestScoreActor @Inject constructor(assets: Assets, dataPersistenceManager: ICandyCountPersistenceManager)
    : Group(), IBestScoreCounter {
    private var actionFinished = true
    private val bestScoreActorImpl: BestScoreActorImpl

    init {
        bestScoreActorImpl = BestScoreActorImpl(assets, dataPersistenceManager)
        addActor(bestScoreActorImpl)
    }

    override fun setBestScore(bestScore: Int) {
        bestScoreActorImpl.setText("BEST: $bestScore")
        if (actionFinished) {
            clearActions()
            addAction(
                    sequence(
                            run2 { actionFinished = false },
                            scaleBy(0.2f, 0.2f, 0.1f),
                            scaleBy(-0.2f, -0.2f, 0.1f),
                            run2 { actionFinished = true }))
        }
    }

    private class BestScoreActorImpl constructor(assets: Assets, dataPersistenceManager: ICandyCountPersistenceManager) : Label(
            "BEST: ${dataPersistenceManager.bestScore}",
            LabelStyle(assets.getBitmapFont(Assets.FONT_COSMIC_SANS_ORANGE), null)) {
        init {
            setAlignment(Align.left)
            wrap = false
            setSize(1f, 0.2f)
            setFontScale(0.002f)
        }
    }
}