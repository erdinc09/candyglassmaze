package com.parabolagames.glassmaze.classic

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.utils.Pool
import com.badlogic.gdx.utils.Pools
import com.parabolagames.glassmaze.framework.TableActor
import com.parabolagames.glassmaze.shared.Assets
import com.parabolagames.glassmaze.shared.Constants


internal class GridActor : Group {
    private val row: Int
    private val col: Int
    private val cellSize: Float
    private val borderWidth: Float
    private val columnLineOffsetFromBottom: Float
    private val assets: Assets

    constructor(row: Int, col: Int, assets: Assets) {
        touchable = Touchable.disabled
        this.row = row
        this.col = col
        cellSize = CELL_SIZE
        borderWidth = BORDER_WIDTH
        columnLineOffsetFromBottom = (Constants.WORLD_HEIGHT - 0.5f) / 2 - row * cellSize / 2
        this.assets = assets
        setPosition((Constants.WORLD_WIDTH - cellSize * col) / 2, if (row < 11) columnLineOffsetFromBottom else 0.2f)
        addRowLines()
        addColumnLines()
    }

    constructor(row: Int, col: Int, cellSize: Float, borderWidth: Float, assets: Assets) {
        touchable = Touchable.disabled
        this.row = row
        this.col = col
        this.cellSize = cellSize
        columnLineOffsetFromBottom = 0f
        this.borderWidth = borderWidth
        this.assets = assets
        setSize(row * cellSize, col * cellSize)
        addRowLines()
        addColumnLines()
    }

    private fun addRowLines() {
        for (i in 0..row) {
            ROW_LINE_ACTOR_POOL.obtain().apply {
                init(col, cellSize, borderWidth, assets)
                setPosition(0f, i * cellSize)
            }.also {
                this@GridActor.addActor(it)
            }
        }
    }

    private fun addColumnLines() {
        for (i in 0..col) {
            COL_LINE_ACTOR_POOL.obtain().apply {
                init(row, cellSize, borderWidth, assets)
                setPosition(i * cellSize, 0f)
            }.also {
                this@GridActor.addActor(it)
            }
        }
    }

    fun resetLinesToPool() = children.forEach {
        when (it) {
            is ColLineActor -> COL_LINE_ACTOR_POOL.free(it)
            is RowLineActor -> ROW_LINE_ACTOR_POOL.free(it)
        }
    }


    internal class RowLineActor : TableActor(), Pool.Poolable {

        fun init(col: Int, cellSize: Float, borderWidth: Float, assets: Assets) {
            if (animation == null) {
                touchable = Touchable.disabled
                loadTexture(assets.getTexture(Assets.GRID_TEXTURE))
            }
            setSize(cellSize * col + borderWidth, borderWidth)
        }

        override fun reset() = Gdx.app.debug("com.parabolagames.glassmaze.RowLineActor", "freed")
    }


    internal class ColLineActor : TableActor(), Pool.Poolable {
        fun init(row: Int, cellSize: Float, borderWidth: Float, assets: Assets) {
            if (animation == null) {
                touchable = Touchable.disabled
                loadTexture(assets.getTexture(Assets.GRID_TEXTURE))
            }
            setSize(borderWidth, cellSize * row)
        }

        override fun reset() = Gdx.app.debug("com.parabolagames.glassmaze.ColLineActor", "freed")
    }

    companion object {
        const val BORDER_WIDTH = 0.005f
        const val CELL_SIZE = 0.3f
        val ROW_LINE_ACTOR_POOL: Pool<RowLineActor> = Pools.get(RowLineActor::class.java)
        val COL_LINE_ACTOR_POOL: Pool<ColLineActor> = Pools.get(ColLineActor::class.java)

        fun disposeObjectPools() {
            ROW_LINE_ACTOR_POOL.clear()
            COL_LINE_ACTOR_POOL.clear()
            Gdx.app.debug("com.parabolagames.glassmaze.GridActor","disposing pools")
        }
    }
}