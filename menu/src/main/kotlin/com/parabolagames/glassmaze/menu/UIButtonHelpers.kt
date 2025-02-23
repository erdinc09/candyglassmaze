package com.parabolagames.glassmaze.menu

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener

internal object UIButtonHelpers {

    private const val TAG = "UIButtonHelpers"

    @JvmStatic
    fun addRingToStage(ringActor: Actor, stage: Stage) {
        val ringX = ringActor.x
        val ringY = ringActor.y
        val ringStageCoordinates = ringActor.parent.localToStageCoordinates(Vector2(ringX, ringY))
        ringActor.remove()
        ringActor.setPosition(ringStageCoordinates.x, ringStageCoordinates.y)
        stage.addActor(ringActor)
        val scaleX = ringActor.scaleX
        val scaleY = ringActor.scaleY
        ringActor.addAction(
                Actions.forever(
                        Actions.sequence(
                                Actions.scaleTo(scaleX * 1.1f, scaleY * 1.1f, 0.5f),
                                Actions.scaleTo(scaleX, scaleY, 0.5f))))
    }

    @JvmStatic
    fun debugClickCoordinates(clickListener: ClickListener, actor: Actor) {
        val x = clickListener.touchDownX
        val y = clickListener.touchDownY

        // returns the relative world coordinate of the touched position
        Gdx.app.debug(TAG, String.format("(x,y)=%f, %f", x, y))

        // returns world coordinates of left bottom vertex, which we inserted the shape
        Gdx.app.debug(TAG, String.format("getX(),getY()=%f, %f", actor.x, actor.y))
    }
}