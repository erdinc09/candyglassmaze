package com.parabolagames.glassmaze.shared

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.google.common.base.Supplier
import com.parabolagames.glassmaze.framework.Box2DActor

class GenericCandyActor(
        assets: Assets,
        candyAtlasName: String,
        size: Float,
        loopAnimation: Boolean,
        frameDurationSupplier: Supplier<Float>,
        var effect: IGenericCandyActorEffectManager? = null) : Box2DActor() {

    private val typeData: ActorTypeData = ActorTypeData(ActorType.CANDY)

    init {
        if (loopAnimation) {
            loadAnimationFromTextureRegions(
                    assets.getTexturesFromTextureAtlas(candyAtlasName!!),
                    frameDurationSupplier.get(),
                    PlayMode.LOOP)
        } else {
            loadAnimationFromTextureRegions(
                    assets.getTexturesFromTextureAtlas(candyAtlasName!!),
                    frameDurationSupplier.get(),
                    PlayMode.LOOP_PINGPONG)
        }
        touchable = Touchable.disabled
        setSize(size, size)
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

    fun reInit(x: Float, y: Float, world: World) {
        clearActions()
        setScale(1f)
        poolReset()
        setPosition(x, y)
        setColor(color.r, color.g, color.b, 1f)
        isVisible = true
        initializePhysics(world)
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        if (effect?.candyEffectShouldDraw == true) {
            effect!!.candyEffect.draw(batch)
        }
        super.draw(batch, parentAlpha)
    }
}