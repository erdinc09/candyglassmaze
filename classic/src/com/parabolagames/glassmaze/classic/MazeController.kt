package com.parabolagames.glassmaze.classic

import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.Pools
import com.google.common.base.Preconditions.checkState
import com.google.common.eventbus.EventBus
import com.parabolagames.glassmaze.classic.CubeActor.Companion.getCubeActorFromPool
import com.parabolagames.glassmaze.classic.CubeActor.Companion.setAtlasTurnRandomly
import com.parabolagames.glassmaze.classic.glass.GenericCandyGlassFactory
import com.parabolagames.glassmaze.classic.glass.GenericGlassBallActor
import com.parabolagames.glassmaze.classic.glass.SpinyBallActor
import com.parabolagames.glassmaze.framework.ForGame
import com.parabolagames.glassmaze.shared.Assets
import com.parabolagames.glassmaze.shared.GenericGlassBallCrackedPieceActorData
import com.parabolagames.glassmaze.shared.SoundPlayer
import dagger.Lazy
import java.util.*
import javax.inject.Inject
import javax.inject.Named

@ForGame
internal class MazeController @Inject constructor(
        @param:Named(ClassicModeModule2.CLASSIC_MAIN_STAGE_0) private val mainStage0: Stage,
        private val soundPlayer: SoundPlayer,
        private val assets: Assets,
        private val level: Level?,
        private val genericCandyGlassFactory: Lazy<GenericCandyGlassFactory>,
        private val spinyGlassHandActor: ISpinyGlassHandActor,
        private val candyGlassHandActor: ICandyGlassHandActor,
        private val glassBallListener: Lazy<IGlassBallBreakListener>,
        private val classicModeInputController: Lazy<IClassicModeInputController>,
        private val eventBus: EventBus)
    : ISpinyBallRemovalListener, IHandActorListenerClassicMode, IMazeBallBreakController {

    private val spinyList: MutableList<IClassicBreakable>
    private val candyGlassList: MutableList<IClassicBreakable>
    private lateinit var gridActor: GridActor
    private lateinit var mazeBallActor: MazeBallActor
    private var isGameFinished = false
    private var isMazeControllerInitialized = false

    init {
        spinyList = ArrayList()
        candyGlassList = ArrayList()
        setAtlasTurnRandomly()
    }

    val isMazeBallMoving: Boolean get() = mazeBallActor.isMoving

    fun gameWillBeFinished() {
        isGameFinished = true
    }

    fun generateMaze() {
        checkState(level != null, "cannot be called while level is null")
        generateGrid()
        generateMazeWalls()
        addGridValues()
        isMazeControllerInitialized = true
    }

    private fun generateGrid() {
        gridActor = GridActor(level!!.row, level.col,assets)
        mainStage0.addActor(gridActor)
    }

    override fun spinyBallRemoved(row: Int, col: Int) {
        checkState(level != null, "cannot be called while level is null")
        level!!.clearGridValue(row, col)
    }

    private var enableHandSpinyPassiveEvent = true
    private var enableHandCandyPassiveEvent = true

    override fun handSpinyIsActive() {

        try {
            enableHandSpinyPassiveEvent = false
            for (breakable in spinyList) {
                breakable.markAndEnableForBreakable()
            }
            candyGlassHandActor.makePassive()
            classicModeInputController.get().enableGameInput(false)
            eventBus.post(EventEnableDisableUIButtons(true))
        } finally {
            enableHandSpinyPassiveEvent = true
        }
    }

    override fun handCandyGlassIsActive() {
        try {
            enableHandCandyPassiveEvent = false
            for (breakable in candyGlassList) {
                breakable.markAndEnableForBreakable()
            }
            spinyGlassHandActor.makePassive()
            classicModeInputController.get().enableGameInput(false)
            eventBus.post(EventEnableDisableUIButtons(true))
        } finally {
            enableHandCandyPassiveEvent = true
        }
    }

    override fun handCandyGlassIsPassive() {
        for (breakable in candyGlassList) {
            breakable.unMarkAndDisableForBreakable()
        }
        classicModeInputController.get().enableGameInput(true)
        if (enableHandCandyPassiveEvent) eventBus.post(EventEnableDisableUIButtons(false))
    }

    override fun handSpinyIsPassive() {
        for (breakable in spinyList) {
            breakable.unMarkAndDisableForBreakable()
        }
        classicModeInputController.get().enableGameInput(true)
        if (enableHandSpinyPassiveEvent) eventBus.post(EventEnableDisableUIButtons(false))
    }

    private fun addGridValues() {
        for (row in 0 until level!!.row) {
            for (col in 0 until level.col) {
                val gridValue = level.getGridValue(row, col) ?: continue
                when (gridValue) {
                    GridValue.BALL -> {
                        checkState(!::mazeBallActor.isInitialized)
                        mainStage0.addActor(
                                MazeBallActor.createForGame(
                                        assets, soundPlayer, row, col, gridActor.x, gridActor.y, this)
                                        .also {
                                            mazeBallActor = it
                                        })
                    }
                    GridValue.SPINY_BALL -> {
                        SPINY_BALL_ACTOR_POOL.obtain().apply {
                            poolInitGame(
                                    assets,
                                    soundPlayer,
                                    spinyGlassHandActor,
                                    this@MazeController,
                                    row,
                                    col,
                                    gridActor.x,
                                    gridActor.y)
                        }.also {
                            spinyList.add(it)
                            mainStage0.addActor(it)
                        }
                    }
                    GridValue.CANDY_GLASS -> {
                        mainStage0.addActor(
                                genericCandyGlassFactory
                                        .get()
                                        .getRandomGenericCandyGlassBallActor(row, col, gridActor.x, gridActor.y)
                                        .also {
                                            candyGlassList.add(it)
                                            level.setBreakableGridValue(row, col, it)
                                        })

                    }
                    GridValue.GLASS -> {
                        GENERIC_GLASS_BALL_ACTOR_POOL_POOL.obtain().apply {
                            poolInitGame(
                                    assets,
                                    soundPlayer,
                                    GenericGlassBallCrackedPieceActorData.GLASS_2,
                                    glassBallListener.get(),
                                    row,
                                    col,
                                    gridActor.x,
                                    gridActor.y)
                        }.also {
                            mainStage0.addActor(it)
                            level.setBreakableGridValue(row, col, it)
                        }
                    }
                    GridValue.OBSTACLE -> mainStage0.addActor(
                            getCubeActorFromPool(
                                    assets, row, col, gridActor.x, gridActor.y))
                    else -> throw IllegalArgumentException("Undefined = $gridValue")
                }
            }
        }
    }

    private fun generateMazeWalls() {
        for (row in 0 until level!!.row) {
            mainStage0.addActor(
                    getCubeActorFromPool(assets, row, -1, gridActor.x, gridActor.y))
            mainStage0.addActor(
                    getCubeActorFromPool(
                            assets, row, level.col, gridActor.x, gridActor.y))
        }
        for (col in -1..level.col) {
            mainStage0.addActor(
                    getCubeActorFromPool(
                            assets, level.row, col, gridActor.x, gridActor.y))
            mainStage0.addActor(
                    getCubeActorFromPool(assets, -1, col, gridActor.x, gridActor.y))
        }
    }

    fun moveMazeCube(direction: MazeCubeDirection) {
        checkState(level != null, "cannot be called while level is null")
        if (!::mazeBallActor.isInitialized || mazeBallActor.isMoving || isGameFinished) {
            return
        }
        if (direction === MazeCubeDirection.UP) {
            val nextRow = findNextRowUp(mazeBallActor.row, mazeBallActor.col)
            if (nextRow != mazeBallActor.row) {
                mazeBallActor.setRow(nextRow, MazeCubeDirection.UP)
            }
        } else if (direction === MazeCubeDirection.DOWN) {
            val nextRow = findNextRowDown(mazeBallActor.row, mazeBallActor.col)
            if (nextRow != mazeBallActor.row) {
                mazeBallActor.setRow(nextRow, MazeCubeDirection.DOWN)
            }
        } else if (direction === MazeCubeDirection.LEFT) {
            val nextCol = findNextColLeft(mazeBallActor.row, mazeBallActor.col)
            if (nextCol != mazeBallActor.col) {
                mazeBallActor.setCol(nextCol, MazeCubeDirection.LEFT)
            }
        } else if (direction === MazeCubeDirection.RIGHT) {
            val nextCol = findNextColRight(mazeBallActor.row, mazeBallActor.col)
            if (nextCol != mazeBallActor.col) {
                mazeBallActor.setCol(nextCol, MazeCubeDirection.RIGHT)
            }
        }
    }

    private fun findNextColRight(currentRow: Int, currentCol: Int): Int {
        var col: Int = currentCol
        while (col < level!!.col) {
            val gridValue = level.getGridValue(currentRow, col)
            if (gridValue !== GridValue.OBSTACLE && gridValue !== GridValue.SPINY_BALL) {
                col++
                continue
            } else {
                break
            }
        }
        return col - 1
    }

    private fun findNextColLeft(currentRow: Int, currentCol: Int): Int {
        var col: Int = currentCol
        while (col >= 0) {
            val gridValue = level!!.getGridValue(currentRow, col)
            if (gridValue !== GridValue.OBSTACLE && gridValue !== GridValue.SPINY_BALL) {
                col--
                continue
            } else {
                break
            }
        }
        return col + 1
    }

    private fun findNextRowUp(currentRow: Int, currentCol: Int): Int {
        var row: Int = currentRow
        while (row < level!!.row) {
            val gridValue = level.getGridValue(row, currentCol)
            if (gridValue !== GridValue.OBSTACLE && gridValue !== GridValue.SPINY_BALL) {
                row++
                continue
            } else {
                break
            }
        }
        return row - 1
    }

    private fun findNextRowDown(currentRow: Int, currentCol: Int): Int {
        var row: Int = currentRow
        while (row >= 0) {
            val gridValue = level!!.getGridValue(row, currentCol)
            if (gridValue !== GridValue.OBSTACLE && gridValue !== GridValue.SPINY_BALL) {
                row--
                continue
            } else {
                break
            }
        }
        return row + 1
    }

    fun dispose() {
        if (isMazeControllerInitialized) {
            gridActor.resetLinesToPool()
        }
    }


    override fun mazeBallPositionUpdated(x: Float, y: Float, ballDirection: MazeCubeDirection, size: Float, rowCurrent: Int, colCurrent: Int) {
        when (ballDirection) {
            MazeCubeDirection.DOWN -> {
                if (rowCurrent > 0) {
                    var breakable = level!!.getBreakableGridValue(rowCurrent - 1, colCurrent)
                    if (breakable != null && breakable.centerY + breakable.radius >= y) {
                        breakable.markForBreak(true)
                    }
                }
            }
            MazeCubeDirection.UP -> {
                if (rowCurrent < level!!.row - 1) {
                    var breakable = level.getBreakableGridValue(rowCurrent + 1, colCurrent)
                    if (breakable != null && breakable.centerY - breakable.radius <= y + size) {
                        breakable.markForBreak(true)
                    }
                }
            }
            MazeCubeDirection.LEFT -> {
                if (colCurrent > 0) {
                    var breakable = level!!.getBreakableGridValue(rowCurrent, colCurrent - 1)
                    if (breakable != null && breakable.centerX + breakable.radius >= x) {
                        breakable.markForBreak(true)
                    }
                }
            }
            MazeCubeDirection.RIGHT -> {
                if (colCurrent < level!!.col - 1) {
                    var breakable = level.getBreakableGridValue(rowCurrent, colCurrent + 1)
                    if (breakable != null && breakable.centerX - breakable.radius <= x + size) {
                        breakable.markForBreak(true)
                    }
                }
            }
        }
    }

    companion object {
        private val SPINY_BALL_ACTOR_POOL = Pools.get(SpinyBallActor::class.java)
        private val GENERIC_GLASS_BALL_ACTOR_POOL_POOL = Pools.get(GenericGlassBallActor::class.java)
    }
}