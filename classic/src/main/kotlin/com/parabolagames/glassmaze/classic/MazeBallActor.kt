package com.parabolagames.glassmaze.classic

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.actions.Actions.*
import com.badlogic.gdx.utils.Pool.Poolable
import com.badlogic.gdx.utils.Pools
import com.parabolagames.glassmaze.framework.TableActor
import com.parabolagames.glassmaze.framework.run2
import com.parabolagames.glassmaze.shared.Assets
import com.parabolagames.glassmaze.shared.Constants
import com.parabolagames.glassmaze.shared.SoundPlayer
import java.util.*
import kotlin.math.abs

internal class MazeBallActor : TableActor(), Poolable {
    private val positions = LinkedList<Vector2>()
    private var xGridActor = 0f
    private var yGridActor = 0f
    private lateinit var soundPlayer: SoundPlayer
    private var direction: MazeCubeDirection? = null
    private var destination = 0f
    private var breakController: IMazeBallBreakController? = null

    var row = 0
        private set
    var col = 0
        private set

    private var rowOriginCurrent = 0
    private var colOriginCurrent = 0

    init {
        color = Color.ORANGE
    }

    private fun initGame(
            assets: Assets,
            soundPlayer: SoundPlayer,
            row: Int,
            col: Int,
            xGridActor: Float,
            yGridActor: Float,
            breakController: IMazeBallBreakController) {
        this.row = row
        this.col = col
        this.rowOriginCurrent = row
        this.colOriginCurrent = col
        this.xGridActor = xGridActor
        this.yGridActor = yGridActor
        this.soundPlayer = soundPlayer
        this.breakController = breakController
        if (animation == null) {
            loadTexture(assets.getTexture(Assets.IRON_BALL))
        }
        setSize(GridActor.CELL_SIZE - offset, GridActor.CELL_SIZE - offset)
        setOrigin(width / 2, height / 2)
        setPosition(
                xGridActor + GridActor.CELL_SIZE * col + offset / 2,
                yGridActor + GridActor.CELL_SIZE * row + offset / 2)
        val growthRatio = 0.3f
        addAction(
                sequence(
                        scaleBy(growthRatio, growthRatio, 0.2f),
                        scaleBy(-growthRatio, -growthRatio, 0.2f),
                        scaleBy(growthRatio, growthRatio, 0.2f),
                        scaleBy(-growthRatio, -growthRatio, 0.2f),
                        scaleBy(growthRatio, growthRatio, 0.2f),
                        scaleBy(-growthRatio, -growthRatio, 0.2f)))
    }

    private fun initExplanation(
            assets: Assets,
            soundPlayer: SoundPlayer,
            row: Int,
            col: Int,
            xGridActor: Float,
            yGridActor: Float,
            cellSize: Float,
            alpha: Float) {
        this.row = row
        this.col = col
        this.rowOriginCurrent = row
        this.colOriginCurrent = col
        this.xGridActor = xGridActor
        this.yGridActor = yGridActor
        this.soundPlayer = soundPlayer
        this.breakController = null
        val offset = 40f
        if (animation == null) {
            loadTexture(assets.getTexture(Assets.IRON_BALL))
        }
        setSize(cellSize - offset, cellSize - offset)
        setPosition(xGridActor + cellSize * col + offset / 2, yGridActor + cellSize * row + offset / 2)
        setOrigin(width / 2, height / 2)
        color.a = alpha
    }


    fun setRow(row: Int, direction: MazeCubeDirection) {
        this.row = row
        this.direction = direction
        soundPlayer.playGlassAppearSound()
        destination = yGridActor + GridActor.CELL_SIZE * row + offset / 2
        addAction(sequence(moveTo(x, destination, destination.getDurationY()), run2 {
            this.direction = null
            positions.clear()
            this.rowOriginCurrent = row
        }))
    }

    fun setCol(col: Int, direction: MazeCubeDirection) {
        this.col = col
        this.direction = direction
        soundPlayer.playGlassAppearSound()
        destination = xGridActor + GridActor.CELL_SIZE * col + offset / 2
        addAction(sequence(moveTo(destination, y, destination.getDurationX()), run2 {
            this.direction = null
            positions.clear()
            this.colOriginCurrent = col
        }))
    }

    private fun Float.getDurationX() = abs(x - this) / VELOCITY

    private fun Float.getDurationY() = abs(y - this) / VELOCITY

    override fun act(dt: Float) {
        super.act(dt)
        if (elapsedTime > 0.025f) {
            elapsedTime = 0f
            positions.addLast(Vector2(x, y))
            if (positions.size == 10) {
                positions.removeFirst()
            }
        }

        if (direction != null) {
            var rowOriginCurrentNow = ((y + originY - yGridActor) / GridActor.CELL_SIZE).toInt()
            var colOriginCurrentNow = ((x + originX - xGridActor) / GridActor.CELL_SIZE).toInt()

            when (direction) {
                MazeCubeDirection.RIGHT -> {
                    while (colOriginCurrent < colOriginCurrentNow) {
                        breakController?.mazeBallPositionUpdated(x, y, direction!!, width, row, colOriginCurrent)
                        colOriginCurrent++
                    }

                }
                MazeCubeDirection.LEFT -> {
                    while (colOriginCurrent > colOriginCurrentNow) {
                        breakController?.mazeBallPositionUpdated(x, y, direction!!, width, row, colOriginCurrent)
                        colOriginCurrent--
                    }
                }
                MazeCubeDirection.DOWN -> {
                    while (rowOriginCurrent > rowOriginCurrentNow) {
                        breakController?.mazeBallPositionUpdated(x, y, direction!!, width, rowOriginCurrent, col)
                        rowOriginCurrent--
                    }
                }
                MazeCubeDirection.UP -> {
                    while (rowOriginCurrent < rowOriginCurrentNow) {
                        breakController?.mazeBallPositionUpdated(x, y, direction!!, width, rowOriginCurrent, col)
                        rowOriginCurrent++
                    }
                }

                null -> TODO("Can Not Happen")
            }
        }
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        super.draw(batch, parentAlpha)
        if (direction != null) {
            val size = positions.size
            for ((idx, pos) in positions.withIndex()) {
                if (animation != null && isVisible && isAnimationVisible) {
                    val c = color
                    batch.setColor(c.r, c.g, c.b, idx.toFloat() / (size + 10))
                    batch.draw(
                            animation.getKeyFrame(elapsedTime),
                            pos.x,
                            pos.y,
                            originX - (x - pos.x),
                            originY - (y - pos.y),
                            width,
                            height,
                            scaleX,
                            scaleY,
                            rotation)
                }
            }
        }
    }

    val isMoving: Boolean
        get() = direction != null

    override fun reset() {
        breakController = null
        poolReset()
        clearActions()
        color.a = 1f
    }

    override fun setParent(parent: Group?) {
        super.setParent(parent)
        if (parent == null) {
            POOL.free(this)
            Gdx.app.debug(TAG, "freed")
            direction = null
        }
    }

    companion object {
        private const val TAG = "com.parabolagames.glassmaze.MazeBallActor"
        private val POOL = Pools.get(MazeBallActor::class.java)
        private const val offset = 0.08f
        private const val VELOCITY = 1.5f * Constants.WORLD_HEIGHT//1.5f
        fun createForGame(
                assets: Assets,
                soundPlayer: SoundPlayer,
                row: Int,
                col: Int,
                xGridActor: Float,
                yGridActor: Float,
                breakController: IMazeBallBreakController): MazeBallActor = POOL.obtain().apply { initGame(assets, soundPlayer, row, col, xGridActor, yGridActor, breakController) }

        @JvmStatic
        fun createForExplanation(
                assets: Assets,
                soundPlayer: SoundPlayer,
                row: Int,
                col: Int,
                xGridActor: Float,
                yGridActor: Float,
                cellSize: Float,
                alpha: Float): MazeBallActor = POOL.obtain().apply { initExplanation(assets, soundPlayer, row, col, xGridActor, yGridActor, cellSize, alpha) }

        fun disposeObjectPools() {
            POOL.clear()
            Gdx.app.debug(TAG, "disposing pools")
        }
    }
}