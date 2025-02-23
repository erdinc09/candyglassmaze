package com.parabolagames.glassmaze.shared.candy

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.actions.Actions.scaleBy
import com.badlogic.gdx.utils.Pool.Poolable
import com.google.common.base.Supplier
import com.parabolagames.glassmaze.framework.Box2DActor
import com.parabolagames.glassmaze.shared.ActorType
import com.parabolagames.glassmaze.shared.ActorTypeData
import com.parabolagames.glassmaze.shared.Assets

abstract class CandyActorBox2D internal constructor() : Box2DActor(), Poolable {
    private val typeData: ActorTypeData = ActorTypeData(ActorType.CANDY)

    init {
        touchable = Touchable.disabled
    }

    override fun initializePhysics(world: World) {
        if (fixtureDef.shape == null) {
            setDynamic()
            setShapeCircle()
            setFixedRotation()
            setPhysicsProperties(1f, 0.5f, 0.1f)
            setSensor()
        } else {
            resetShapeCirclePosition()
        }
        super.initializePhysics(world)
        bodyUserData = typeData
    }

    private fun reInit(
            assets: Assets,
            candyAtlasName: String,
            size: Float,
            loopAnimation: Boolean,
            frameDurationSupplier: Supplier<Float>,
            x: Float,
            y: Float,
            world: World) {
        if (animation == null) {
            loadAnimationFromTextureRegions(
                    assets.getTexturesFromTextureAtlas(candyAtlasName),
                    frameDurationSupplier.get()!!,
                    if (loopAnimation) PlayMode.LOOP else PlayMode.LOOP_PINGPONG)
        }
        animation.playMode = if (loopAnimation) PlayMode.LOOP else PlayMode.LOOP_PINGPONG
        setSize(size, size)
        clearActions()
        setScale(1f)
        poolReset()
        setPosition(x, y)
        initializePhysics(world)
        addScaleAction()
    }

    private fun addScaleAction() = addAction(scaleBy(0.5f, 0.5f, 2f))

    override fun reset() {
        clearActions()
        resetAnimation()
    }

    companion object {
        fun getCandy(
                assets: Assets,
                candyAtlasName: String,
                size: Float,
                loopAnimation: Boolean,
                frameDurationSupplier: Supplier<Float>,
                x: Float,
                y: Float,
                world: World): CandyActorBox2D =
                CandyMap.CANDY_MAP.get(candyAtlasName).obtain()
                        .apply {
                            reInit(assets, candyAtlasName, size, loopAnimation, frameDurationSupplier, x, y, world)
                        }

        fun disposeObjectPools() = CandyMap.disposeObjectPools()
    }
}