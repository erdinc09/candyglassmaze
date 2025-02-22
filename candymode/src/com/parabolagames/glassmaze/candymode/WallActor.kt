package com.parabolagames.glassmaze.candymode

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.utils.Pool
import com.badlogic.gdx.utils.Pool.Poolable
import com.badlogic.gdx.utils.Pools
import com.parabolagames.glassmaze.framework.Box2DActor
import com.parabolagames.glassmaze.shared.ActorType
import com.parabolagames.glassmaze.shared.ActorTypeData

internal class WallActor : Box2DActor(), Poolable {
    private val typeData: ActorTypeData = ActorTypeData(ActorType.WALL)

    override fun setParent(parent: Group) {
        super.setParent(parent)
        if (parent == null) {
            destroyBody()
            WALL_ACTOR_POOL.free(this)
            Gdx.app.debug("WallActor", "freed")
        }
    }

    fun poolInit(width: Float, height: Float, x: Float, y: Float, world: World) {
        setPosition(x, y)
        setSize(width, height)
        if (fixtureDef.shape == null) {
            setPhysicsProperties(1f, 1f, 1f)
            setStatic()
            setShapeRectangle()
        } else {
            resetShapeRectanglePosition()
        }
        initializePhysics(world)
    }

    override fun initializePhysics(w: World) {
        super.initializePhysics(w)
        bodyUserData = typeData
    }

    override fun reset() {
        poolReset()
    }

    companion object {
        val WALL_ACTOR_POOL: Pool<WallActor> = Pools.get(WallActor::class.java, 100)

        fun create(width: Float, height: Float, x: Float, y: Float, world: World): WallActor = WALL_ACTOR_POOL.obtain().apply {
            poolInit(width, height, x, y, world)
        }
    }
}