package com.parabolagames.glassmaze.shared

import com.badlogic.gdx.scenes.scene2d.actions.Actions.*
import com.parabolagames.glassmaze.framework.Box2DActor

object Box2DActorDelayedRemover {


    fun add(actor: Box2DActor) {
        add(actor, 0f)
    }

    fun add(actor: Box2DActor, heightOffset: Float) {
        when (actor) {
            is IBox2DActorRemovalListener -> actor.addAction(forever(sequence(delay(0.1f), run(Box2DActorRemover(actor, heightOffset)))))
            else -> actor.addAction(forever(sequence(delay(1f), run(Box2DActorRemover(actor, heightOffset)))))
        }
    }

    internal class Box2DActorRemover(private val actor: Box2DActor, private val heightOffset: Float) : Runnable {
        override fun run() {
            if (actor.color.a <= 0.01f || actor.y + actor.height < 0 - heightOffset
                    || actor.y > Constants.WORLD_HEIGHT || actor.x + actor.width < -1 || actor.x > Constants.WORLD_WIDTH) {
                val parent = actor.parent
                if (parent != null && parent.children.size == 1) { // TODO: parent null gelebiliyor
                    parent.remove()
                }
                if (actor is IBox2DActorRemovalListener) {
                    (actor as IBox2DActorRemovalListener).actorRemoved(actor.x, actor.y)
                }

                actor.destroyBody()
                actor.remove()
            }
        }
    }
}