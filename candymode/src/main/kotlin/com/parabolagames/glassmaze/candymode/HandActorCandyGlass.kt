package com.parabolagames.glassmaze.candymode

import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.actions.Actions.*
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup
import com.badlogic.gdx.utils.Align
import com.google.common.base.Preconditions.checkState
import com.parabolagames.glassmaze.framework.TableActor
import com.parabolagames.glassmaze.framework.run2
import com.parabolagames.glassmaze.shared.Assets
import com.parabolagames.glassmaze.shared.Constants

internal class HandActorCandyGlass private constructor(assets: Assets) : WidgetGroup(), IHandCounter {
    private val handActorImpl: HandActorImpl
    private val candyGlassActor: CandyGlassActor
    var infiniteMode = false
    private lateinit var count: Label
    override var handCount = INVALID_HAND_COUNT
        get(): Long {
            if (infiniteMode) {
                return INVALID_HAND_COUNT
            }
            checkState(field != INVALID_HAND_COUNT)
            return field
        }
    private var actionFinished = true

    init {
        candyGlassActor = CandyGlassActor(assets)
        addActor(candyGlassActor)
        handActorImpl = HandActorImpl(assets)
        addActor(handActorImpl)
        handActorImpl.setPosition(-handActorImpl.width, -handActorImpl.height)
        debug = Constants.DEBUG_DRAW
    }

    private fun initializeForCandyMode(assets: Assets, handCount: Long): HandActorCandyGlass {
        this.handCount = handCount
        handActorImpl.initializeForClassicMode(assets)
        val grCount = Group()
        grCount.addActor(count)
        addActor(grCount)
        grCount.setOrigin(count.x, count.y)
        return this
    }

    private fun initializeForInfiniteMode(assets: Assets): HandActorCandyGlass {
        handActorImpl.initializeForInfiniteMode(assets)
        return this
    }

    override fun decrementHandCount(): Long {
        if (infiniteMode) {
            return INVALID_HAND_COUNT
        }
        checkState(handCount != INVALID_HAND_COUNT)
        if (handCount > 0) {
            handCount--
        }
        count.setText(handCount.toString())
        if (handCount == 0L) {
            handActorImpl.addAction(alpha(0.4f))
        }
        return handCount
    }

//    override fun getHandCount(): Long {
//        if (infiniteMode) {
//            return INVALID_HAND_COUNT
//        }
//        checkState(handCount != INVALID_HAND_COUNT)
//        return handCount
//    }

    override fun pressedWhenHandCountIsZero() {
        if (actionFinished) {
            count.parent
                    .addAction(
                            sequence(
                                    run2 { actionFinished = false },
                                    scaleBy(0.2f, 0.5f, 0.1f),
                                    scaleBy(-0.2f, -0.5f, 0.1f),
                                    scaleBy(0.3f, 0.7f, 0.1f),
                                    scaleBy(-0.3f, -0.7f, 0.1f),
                                    run2 { actionFinished = true }))
        }
    }

    private class CandyGlassActor(assets: Assets) : TableActor() {
        init {
            loadTexture(assets.getTexture(Assets.GLASS2))
            setSize(GLASS_SIZE, GLASS_SIZE)
            setPosition(-width - 0.18f, -height + 0.06f)
            setOrigin(width / 2, height / 2)
        }
    }

    private inner class HandActorImpl constructor(assets: Assets) : TableActor() {
        init {
            loadTexture(assets.getTexture(Assets.HAND_CANDY_GLASS))
            setSize(HEIGHT * (width / height), HEIGHT)
        }

        fun initializeForClassicMode(assets: Assets) {
            count = Label(handCount.toString(), LabelStyle(assets.getBitmapFont(Assets.FONT_COSMIC_SANS_ORANGE_BOLD), null))
                    .apply {
                        setAlignment(Align.left)
                        wrap = false
                        setSize(1f, 0.2f)
                        setFontScale(0.0015f)
                        setPosition(-0.05f, -this@HandActorImpl.height - 0.08f)
                    }
        }

        fun initializeForInfiniteMode(assets: Assets) {
            count = Label("8", LabelStyle(assets.getBitmapFont(Assets.FONT_COURIER_ORANGE), null))
                    .apply {
                        setAlignment(Align.left)
                        wrap = false
                        setSize(1f, 0.2f)
                        setFontScale(0.002f)
                        setPosition(this@HandActorImpl.width, 0.2f)
                    }.also {
                        val gr = Group()
                        gr.addActor(it)
                        gr.addAction(Actions.rotateBy(90f))
                        gr.setOrigin(count.width / 2, count.height / 2)
                        addActor(gr)
                    }

            infiniteMode = true
        }


    }

    companion object {
        private const val INVALID_HAND_COUNT: Long = -100
        private const val HEIGHT = 0.35f
        private const val GLASS_SIZE = 0.20f

        @JvmStatic
        fun createHandActorForFiniteMode(
                assets: Assets, dataPersistenceManager: ICandyCountPersistenceManager) = HandActorCandyGlass(assets)
                .initializeForCandyMode(assets, dataPersistenceManager.handNumberCount.toLong())

        fun createHandActorForInfiniteMode(assets: Assets) = HandActorCandyGlass(assets).initializeForInfiniteMode(assets)
    }
}