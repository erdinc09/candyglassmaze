package com.parabolagames.glassmaze.classic

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.Pool.Poolable
import com.badlogic.gdx.utils.Pools
import com.google.common.base.Preconditions.checkState

internal class Level : Poolable, ILevelNumberProvider {
    override var levelNumber = 0
        private set
    private lateinit var gridData: GridData
    private var isReset = false

    fun poolInit(levelNumber: Int, gridData: GridData) {
        this.levelNumber = levelNumber
        this.gridData = gridData
        isReset = false
    }

    fun freeToPool() {
        checkState(!isReset, "already freed")
        gridData.freeToPool()
        Pools.get(Level::class.java).free(this)
    }

    fun getGridValue(row: Int, col: Int) = gridData.getGridValue(row, col)
    fun getBreakableGridValue(row: Int, col: Int) = gridData.getBreakableGridValue(row, col)
    fun setBreakableGridValue(row: Int, col: Int, value: IBreakableInGrid) = gridData.setBreakableGridValue(row, col, value)

    val col: Int get() = gridData.col
    val row: Int get() = gridData.row
    val candyCount: Int get() = gridData.candyCount
    val glassCount: Int get() = gridData.glassCount
    override val candyGlassCountToManualBreak: Int get() = gridData.candyGlassCountToManualBreak
    override val spinyGlassCountToManualBreak: Int get() = gridData.spinyGlassCountToManualBreak
    override val spinyGlassCount: Int get() = gridData.spinyGlassCount
    override val spinyGlassCountCurrent: Int get() = gridData.spinyCountCurrent

    override fun reset() {
        isReset = true
    }

    fun clearGridValue(row: Int, col: Int) {
        gridData.setGridValue(row, col, null)
        gridData.setBreakableGridValue(row, col, null)
    }

    companion object {
        fun disposeObjectPools() {
            Gdx.app.debug(" com.parabolagames.glassmaze.Level", "disposing pools")
            Pools.get(Level::class.java).clear()
        }
    }
}