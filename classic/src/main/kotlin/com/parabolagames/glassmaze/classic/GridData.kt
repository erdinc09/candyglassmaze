package com.parabolagames.glassmaze.classic

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.Pool.Poolable
import com.badlogic.gdx.utils.Pools
import com.google.common.base.Preconditions.checkArgument

internal class GridData : Poolable {
    var spinyGlassCount = 0
    private val grid: Array<Array<GridValue?>>
    private val breakableGrid: Array<Array<IBreakableInGrid?>>
    var row = 0
        private set
    var col = 0
        private set
    var candyCount = 0
    var glassCount = 0
    var candyGlassCountToManualBreak = 0
    var spinyGlassCountToManualBreak = 0
    var spinyCountCurrent: Int = 0
        get() {
            var count = 0
            for (r in 0 until row) {
                for (c in 0 until col) {
                    if (getGridValue(r, c) == GridValue.SPINY_BALL) {
                        count++
                    }
                }
            }
            return count
        }

    init {
        grid = Array(MAX_ROW) { arrayOfNulls(MAX_COL) }
        breakableGrid = Array(MAX_ROW) { arrayOfNulls(MAX_COL) }
    }

    fun poolInit(row: Int, col: Int) {
        this.row = row
        this.col = col
    }

    fun freeToPool() = Pools.get(GridData::class.java).free(this)

    fun setGridValue(row: Int, col: Int, gridValue: GridValue?) {
        checkArgument(row >= 0 && row < this.row, "invalid row = %s", row)
        checkArgument(col >= 0 && col < this.col, "invalid col = %s", col)
        grid[row][col] = gridValue
    }

    fun setBreakableGridValue(row: Int, col: Int, gridValue: IBreakableInGrid?) {
        checkArgument(row >= 0 && row < this.row, "invalid row = %s", row)
        checkArgument(col >= 0 && col < this.col, "invalid col = %s", col)
        breakableGrid[row][col] = gridValue
    }

    fun getGridValue(row: Int, col: Int): GridValue? {
        checkArgument(row >= 0 && row < this.row, "invalid row = %s", row)
        checkArgument(col >= 0 && col < this.col, "invalid col = %s", col)
        return grid[row][col]
    }


    fun getBreakableGridValue(row: Int, col: Int): IBreakableInGrid? {
        checkArgument(row >= 0 && row < this.row, "invalid row = %s", row)
        checkArgument(col >= 0 && col < this.col, "invalid col = %s", col)
        return breakableGrid[row][col]
    }

    override fun reset() {
        for (row in 0 until MAX_ROW) {
            for (col in 0 until MAX_COL) {
                grid[row][col] = null
                breakableGrid[row][col] = null
            }
        }
    }

    companion object {
        private const val MAX_COL = 10
        private const val MAX_ROW = 15

        fun disposeObjectPools() {
            Gdx.app.debug(" com.parabolagames.glassmaze.GridData", "disposing pools")
            Pools.get(GridData::class.java).clear()
        }
    }
}