package com.parabolagames.glassmaze.candymode

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Vector2
import com.parabolagames.glassmaze.framework.Box2DActor
import java.util.*

internal abstract class ThrowableBallActor : Box2DActor() {

    private val positions = LinkedList<Vector2>()


    override fun act(dt: Float) {
        super.act(dt)
        elapsedTime += dt
        if (elapsedTime > 0.05f) {
            elapsedTime = 0f
            positions.addLast(Vector2(x, y))
            if (positions.size == 5) {
                positions.removeFirst()
            }
        }
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        super.draw(batch, parentAlpha)
        if (velocity.len() > 8f) {
            val size = positions.size
            for ((i, pos) in positions.withIndex()) {
                if (animation != null && isVisible && isAnimationVisible) {
                    val c = color
                    batch.setColor(c.r, c.g, c.b, i.toFloat() / (size + 1))
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

    companion object {
        const val BALL_RADIUS = 0.16f
    }
}