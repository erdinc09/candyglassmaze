package com.parabolagames.glassmaze.candymode

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.google.common.base.Supplier
import com.parabolagames.glassmaze.framework.Box2DActor
import com.parabolagames.glassmaze.shared.ActorType
import com.parabolagames.glassmaze.shared.ActorTypeData
import com.parabolagames.glassmaze.shared.Assets
import com.parabolagames.glassmaze.shared.Constants

internal class GenericCandyActor(
        worldCoordinate: Vector3,
        private val world: World,
        assets: Assets,
        candyAtlasName: String,
        private val size: Float,
        private val loopAnimation: Boolean,
        private val frameDurationSupplier: Supplier<Float>) : Box2DActor() {

    init {
        init(worldCoordinate, assets, candyAtlasName)
    }

    private fun init(worldCoordinate: Vector3, assets: Assets, candyAtlasName: String) {
        if (loopAnimation) {
            loadAnimationFromTextureRegions(
                    assets.getTexturesFromTextureAtlas(candyAtlasName),
                    frameDurationSupplier.get(),
                    PlayMode.LOOP) // 0.01
        } else {
            loadAnimationFromTextureRegions(
                    assets.getTexturesFromTextureAtlas(candyAtlasName),
                    frameDurationSupplier.get(),
                    PlayMode.LOOP_PINGPONG) // 0.01
        }
        setSize(size, size)
        setPosition(worldCoordinate.x, worldCoordinate.y)
        debug = Constants.DEBUG_DRAW
        touchable = Touchable.disabled
    }

    fun initializePhysics() {
        setDynamic()
        setShapeCircle()
        setFixedRotation() // image shadows must be consistent
        setPhysicsProperties(1f, 0.5f, 0.1f)
        super.initializePhysics(world)
        bodyUserData = ActorTypeData(ActorType.CANDY)
    }

    companion object {
        const val DEFAULT_SIZE = 0.2f
    }
}