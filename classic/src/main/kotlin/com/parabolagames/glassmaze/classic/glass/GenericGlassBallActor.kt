package com.parabolagames.glassmaze.classic.glass

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.utils.Pool.Poolable
import com.badlogic.gdx.utils.Pools
import com.google.common.base.Preconditions.checkState
import com.parabolagames.glassmaze.classic.ExplanationOrGame
import com.parabolagames.glassmaze.classic.GridActor
import com.parabolagames.glassmaze.classic.IBreakableInGrid
import com.parabolagames.glassmaze.classic.IGlassBallBreakListener
import com.parabolagames.glassmaze.framework.TableActor
import com.parabolagames.glassmaze.shared.Assets
import com.parabolagames.glassmaze.shared.GenericGlassBallCrackedPieceActorData
import com.parabolagames.glassmaze.shared.IBreakable
import com.parabolagames.glassmaze.shared.SoundPlayer
import com.parabolagames.glassmaze.shared.crack.GenericGlassCrackedPieceActor
import kotlin.properties.Delegates

internal class GenericGlassBallActor : TableActor(), IBreakable, Poolable, IBreakableInGrid {
    private var isReset = true
    private lateinit var soundPlayer: SoundPlayer
    private var isCracked = false
    private var isMarkedForBreak = false
    private lateinit var assets: Assets
    private var soundWillBePlayed = false
    private lateinit var data: GenericGlassBallCrackedPieceActorData
    private var crackPadX = 0f
    private var crackPadY = 0f
    private var mode: ExplanationOrGame? = null
    private var breakListener: IGlassBallBreakListener? = null
    private var row by Delegates.notNull<Int>()
    private var col by Delegates.notNull<Int>()


    fun poolInitGame(
            assets: Assets,
            soundPlayer: SoundPlayer,
            data: GenericGlassBallCrackedPieceActorData,
            breakListener: IGlassBallBreakListener,
            row: Int,
            col: Int,
            xGridActor: Float,
            yGridActor: Float) {
        this.soundPlayer = soundPlayer
        this.data = data
        this.assets = assets
        this.isCracked = false
        this.isMarkedForBreak = false
        this.breakListener = breakListener
        this.mode = ExplanationOrGame.GAME
        this.row = row
        this.col = col
        init(assets, data, row, col, xGridActor, yGridActor, GridActor.CELL_SIZE, OFFSET)
        isReset = false
    }

    fun poolInitExplanation(
            assets: Assets,
            soundPlayer: SoundPlayer,
            data: GenericGlassBallCrackedPieceActorData,
            row: Int,
            col: Int,
            xGridActor: Float,
            yGridActor: Float,
            cellSize: Float,
            crackPadX: Float,
            crackPadY: Float) {
        this.soundPlayer = soundPlayer
        this.data = data
        this.assets = assets
        this.crackPadX = crackPadX
        this.crackPadY = crackPadY
        this.isCracked = false
        this.isMarkedForBreak = false
        this.mode = ExplanationOrGame.EXPLANATION
        this.row = row
        this.col = col
        init(assets, data, row, col, xGridActor, yGridActor, cellSize, INSETS_FOR_EXP_DIALOG)
        isReset = false
    }

    private fun init(
            assets: Assets,
            data: GenericGlassBallCrackedPieceActorData,
            row: Int,
            col: Int,
            xGridActor: Float,
            yGridActor: Float,
            cellSize: Float,
            offset: Float) {
        if (animation == null) {
            loadTexture(assets.getTexture(data.glassAtlas))
        }
        setSize(cellSize - offset, cellSize - offset)
        setPosition(xGridActor + cellSize * col + offset / 2, yGridActor + cellSize * row + offset / 2)
    }

    override fun act(dt: Float) {
        super.act(dt)
        if (!isCracked && isMarkedForBreak) {
            isCracked = true
            soundWillBePlayed = true
            breakTheGlass()
        }
    }

    override val centerX: Float
        get() = x + width / 2
    override val centerY: Float
        get() = y + height / 2
    override val radius: Float
        get() = width / 2

    override fun breakTheGlass() {
        if (soundWillBePlayed) {
            soundPlayer.playRandomBreak()
        }
        breakListener?.genericGlassBroken(row, col)
        GenericGlassCrackedPieceActor.createAllPiecesWithSplit(
                x + crackPadX,
                y + crackPadY,
                assets,
                width,
                data,
                DEFAULT_FADE_OUT_TIME,
                DEFAULT_DELAY_TIME,
                stage,
                if (mode === ExplanationOrGame.EXPLANATION) 3f else 1.toFloat(),
                if (mode === ExplanationOrGame.EXPLANATION) 400f else 1.toFloat())
        remove()
    }

    override fun setParent(parent: Group?) {
        super.setParent(parent)
        if (parent == null) {
            checkState(!isReset)
            GENERIC_GLASS_BALL_ACTOR_POOL.free(this)
            Gdx.app.debug("com.parabolagames.glassmaze.GenericGlassBallActor", "freed")
        }
    }


    override fun markForBreak(soundWillBePlayed: Boolean) {
        this.soundWillBePlayed = soundWillBePlayed
        isMarkedForBreak = true
    }

    override fun reset() {
        poolReset()
        clearActions()
        crackPadX = 0f
        crackPadY = 0f
        isCracked = false
        isMarkedForBreak = false
        isReset = true
        breakListener = null
        mode = null
    }

    companion object {
        fun disposeObjectPools() {
            GENERIC_GLASS_BALL_ACTOR_POOL.clear()
            Gdx.app.debug("com.parabolagames.glassmaze.GenericGlassBallActor","disposing pools")
        }

        const val DEFAULT_FADE_OUT_TIME = 0.5f
        const val DEFAULT_DELAY_TIME = 0.5f
        const val INSETS_FOR_EXP_DIALOG = 20f
        const val OFFSET = 0.01f
        private val GENERIC_GLASS_BALL_ACTOR_POOL = Pools.get(GenericGlassBallActor::class.java)
    }
}