package com.parabolagames.glassmaze.classic.glass

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Pool.Poolable
import com.badlogic.gdx.utils.Pools
import com.google.common.base.Preconditions.checkState
import com.parabolagames.glassmaze.classic.*
import com.parabolagames.glassmaze.framework.TableActor
import com.parabolagames.glassmaze.shared.Assets
import com.parabolagames.glassmaze.shared.Constants
import com.parabolagames.glassmaze.shared.GenericGlassBallCrackedPieceActorData
import com.parabolagames.glassmaze.shared.SoundPlayer
import com.parabolagames.glassmaze.shared.crack.GenericGlassCrackedPieceActor

internal class SpinyBallActor : TableActor(), IClassicBreakable, Poolable {
    private val clickListener: ClickListener = ClickListener()
    private var isReset = true
    private lateinit var soundPlayer: SoundPlayer
    private var isCracked = false
    private var soundWillBePlayed = false
    private lateinit var ringActor: RingActor
    private var handActor: ISpinyGlassHandActor? = null
    private var clickable = false
    private lateinit var assets: Assets
    private var spinyBallRemovalListener: ISpinyBallRemovalListener? = null
    private var row = 0
    private var col = 0
    private var crackPadX = 0f
    private var crackPadY = 0f
    private var mode: ExplanationOrGame? = null
    private var isMarkedForBreak = false

    init {
        addListener(clickListener)
    }

    fun poolInitExplanation(
            assets: Assets,
            soundPlayer: SoundPlayer,
            row: Int,
            col: Int,
            xGridActor: Float,
            yGridActor: Float,
            cellSize: Float,
            crackPadX: Float,
            crackPadY: Float) {
        this.crackPadX = crackPadX
        this.crackPadY = crackPadY
        this.soundPlayer = soundPlayer
        this.assets = assets
        this.handActor = DummyISpinyGlassHandActor.INSTANCE
        this.row = row
        this.col = col
        this.spinyBallRemovalListener = DummyISpinyGlassHandActor.INSTANCE
        this.mode = ExplanationOrGame.EXPLANATION
        initialize(assets, row, col, xGridActor, yGridActor, cellSize)
        this.isReset = false
    }

    fun poolInitGame(
            assets: Assets,
            soundPlayer: SoundPlayer,
            handActor: ISpinyGlassHandActor,
            spinyBallRemovalListener: ISpinyBallRemovalListener,
            row: Int,
            col: Int,
            xGridActor: Float,
            yGridActor: Float) {
        this.soundPlayer = soundPlayer
        this.assets = assets
        this.handActor = handActor
        this.row = row
        this.col = col
        this.spinyBallRemovalListener = spinyBallRemovalListener
        this.mode = ExplanationOrGame.GAME
        initialize(assets, row, col, xGridActor, yGridActor, GridActor.CELL_SIZE)
        this.isReset = false
    }

    private fun initialize(
            assets: Assets,
            row: Int,
            col: Int,
            xGridActor: Float,
            yGridActor: Float,
            cellSize: Float) {
        if (animation == null) {
            loadAnimationFromTextureRegions(
                    assets.getTexturesFromTextureAtlas(Assets.SPINY_GLASS_ATLAS), 0.025f, true)
        }
        setSize(cellSize - offset, cellSize - offset)
        setPosition(xGridActor + cellSize * col + offset / 2, yGridActor + cellSize * row + offset / 2)

        if (!::ringActor.isInitialized) {
            ringActor = RingActor(assets, x + width / 2, y + height / 2)
        } else {
            ringActor.reset()
        }
    }

    override fun act(dt: Float) {
        super.act(dt)
        if (!isCracked
                && (clickListener.isPressed && clickable && handActor!!.isBreakingEnabled
                        || isMarkedForBreak)) {
            isCracked = true
            soundWillBePlayed = true
            spinyBallRemovalListener!!.spinyBallRemoved(row, col)
            if (clickListener.isPressed) {
                handActor!!.decrementHandCount()
            }

            breakTheGlass()
        }
    }

    override fun breakTheGlass() {
        if (soundWillBePlayed) {
            soundPlayer.playRandomBreak()
        }
        GenericGlassCrackedPieceActor.createAllPiecesWithSplit(
                x + crackPadX,
                y + crackPadY,
                assets,
                width,
                GenericGlassBallCrackedPieceActorData.SPINY_GLASS,
                GenericGlassBallActor.DEFAULT_FADE_OUT_TIME,
                GenericGlassBallActor.DEFAULT_DELAY_TIME,
                stage,
                if (mode === ExplanationOrGame.EXPLANATION) 3f else 1.toFloat(),
                if (mode === ExplanationOrGame.EXPLANATION) 400f else 1.toFloat())
        handActor!!.makePassive()
        remove()
    }

    override fun markForBreak(soundWillBePlayed: Boolean) {
        this.soundWillBePlayed = soundWillBePlayed
        isMarkedForBreak = true
    }

    override fun markAndEnableForBreakable() {
        if (parent == null) {
            return
        }
        clickable = true
        if (mode === ExplanationOrGame.GAME) {
            ringActor.poolInit(assets, x + width / 2, y + height / 2, RingActor.SIZE)
            stage.addActor(ringActor)
        } else {
            checkState(mode === ExplanationOrGame.EXPLANATION)
            ringActor.poolInit(
                    assets, width / 2, height / 2, RingActor.SIZE * Constants.DIALOG_SIZE_RATIO)
            addActor(ringActor)
        }
    }

    override fun unMarkAndDisableForBreakable() {
        clickable = false
        ringActor.remove()
        ringActor.resetAnimation()
    }

    override fun reset() {
        poolReset()
        clearActions()
        unMarkAndDisableForBreakable()
        crackPadX = 0f
        crackPadY = 0f
        isCracked = false
        clickable = false
        spinyBallRemovalListener = null
        handActor = null
        isReset = true
        isMarkedForBreak = false
    }

    override fun setParent(parent: Group?) {
        super.setParent(parent)
        if (parent == null) {
            checkState(!isReset)
            SPINY_BALL_ACTOR_POOL.free(this)
            Gdx.app.debug("SpinyBallActor", "freed")
        }
    }

    private class DummyISpinyGlassHandActor : ISpinyGlassHandActor, ISpinyBallRemovalListener {
        override fun decrementHandCount(): Int = 0

        override val handCount: Int = 0

        override fun makePassive() {}
        override fun updateFromDataPersistenceManager() {}

        override val isBreakingEnabled: Boolean = false

        override fun spinyBallRemoved(row: Int, col: Int) {}

        companion object {
            var INSTANCE = DummyISpinyGlassHandActor()
        }
    }

    companion object {
        fun disposeObjectPools() {
            SPINY_BALL_ACTOR_POOL.clear()
            Gdx.app.debug("com.parabolagames.glassmaze.SpinyBallActor", "disposing pools")
        }

        const val offset = 0.01f
        private val SPINY_BALL_ACTOR_POOL = Pools.get(SpinyBallActor::class.java)
    }
}