package com.parabolagames.glassmaze.shared.crack

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions.*
import com.badlogic.gdx.utils.ObjectMap
import com.badlogic.gdx.utils.Pool.Poolable
import com.badlogic.gdx.utils.Pools
import com.parabolagames.glassmaze.framework.TableActor
import com.parabolagames.glassmaze.shared.Assets
import com.parabolagames.glassmaze.shared.GenericGlassBallCrackedPieceActorData

// 1 asagi 2 yukari
// 1 2
// 1 	2_1 2_2
// 1_1 1_2 	2
// 1_1 1_2 2_1 2_2
abstract class GenericGlassCrackedPieceActor private constructor() : TableActor(), Poolable {
    private val animationTable = ObjectMap<String, Animation<TextureRegion>>()
    private var loadedAnimationAssetName: String? = null

    protected fun initialize(
            width: Float,
            height: Float,
            x: Float,
            y: Float,
            assets: Assets,
            atlasName: String,
            fadeOutTime: Float,
            delayTime: Float,
            frameDuration: Float) {
        if (animation == null || atlasName != loadedAnimationAssetName) {
            loadedAnimationAssetName = atlasName
            var animationFromTable = animationTable.get(loadedAnimationAssetName)
            if (animationFromTable == null) {
                val animation = loadAnimationFromTextureRegions(
                        assets.getTexturesFromTextureAtlas(atlasName),
                        frameDuration,
                        Animation.PlayMode.LOOP_PINGPONG)
                animationTable.put(loadedAnimationAssetName, animation)
                animationFromTable = animation
                Gdx.app.debug("GenericGlassCrackedPieceActor", "new animation created")
            } else {
                Gdx.app.debug("GenericGlassCrackedPieceActor", "cached animation used")
            }
            setAnimation(animationFromTable)
        } else {
            Gdx.app.debug("GenericGlassCrackedPieceActor", "current animation used")
        }
        setPosition(x, y)
        setSize(width, height)
        if (fadeOutTime > 0) {
            addAction(sequence(delay(delayTime), fadeOut(fadeOutTime)))
        }
    }

    protected abstract fun init(
            width: Float,
            height: Float,
            x: Float,
            y: Float,
            assets: Assets,
            data: GenericGlassBallCrackedPieceActorData,
            fadeOutTime: Float,
            delayTime: Float,
            crackDelta: Float)

    override fun reset() {
        poolReset()
        setColor(color.r, color.g, color.b, 1f)
        clearActions()
    }

    //BUG:
    //Execution failed for task ':android:minifyReleaseWithR8'.
    //> com.android.tools.r8.CompilationFailedException: Compilation failed to complete, position: Ltr/com/karali/glassmazewithcandies/shared/crack/GenericGlassCrackedPieceActor
    // ;access$setParent$s-2000748310(Ltr/com/karali/glassmazewithcandies/shared/crack/GenericGlassCrackedPieceActor;Lcom/badlogic/gdx/scenes/scene2d/Group;)V,
    // origin: /Users/erdincyilmaz/git/glassmazewithcandies/shared/build/libs/shared-1.0.jar:tr/com/karali/glassmazewithcandies/shared/crack/GenericGlassCrackedPieceActor.class

    override fun setParent(parent: Group?) {
        super.setParent(parent)
    }

    private class GenericGlassCrackedPieceActor1 private constructor() : GenericGlassCrackedPieceActor() {
        public override fun init(
                width: Float,
                height: Float,
                x: Float,
                y: Float,
                assets: Assets,
                data: GenericGlassBallCrackedPieceActorData,
                fadeOutTime: Float,
                delayTime: Float,
                crackDeltaV: Float) {
            super.initialize(
                    width, height, x, y, assets, data.piece1Atlas, fadeOutTime, delayTime, 0.04f)
            addAction(
                    sequence(
                            moveBy(0f, -3 * crackDeltaV, 1.3f, Interpolation.slowFast),
                            removeActor()))
        }

        override fun setParent(parent: Group?) {
            super.setParent(parent)
            if (parent == null) {
                GENERIC_GLASS_CRACKED_PIECE_ACTOR_1_POOL.free(this)
                Gdx.app.debug("GenericGlassCrackedPieceActor1", "freed")
            }
        }
    }

    private class GenericGlassCrackedPieceActor1_1 private constructor() : GenericGlassCrackedPieceActor() {
        public override fun init(
                width: Float,
                height: Float,
                x: Float,
                y: Float,
                assets: Assets,
                data: GenericGlassBallCrackedPieceActorData,
                fadeOutTime: Float,
                delayTime: Float,
                crackDeltaV: Float) {
            super.initialize(
                    width, height, x, y, assets, data.piece1_1Atlas, fadeOutTime, delayTime, 0.04f)
            addAction(
                    sequence(
                            parallel(
                                    moveBy(width / 3, 0f, 0.2f),
                                    moveBy(0f, -3 * crackDeltaV, 1.3f, Interpolation.slowFast)),
                            removeActor()))
        }

        override fun setParent(parent: Group?) {
            super.setParent(parent)
            if (parent == null) {
                GENERIC_GLASS_CRACKED_PIECE_ACTOR_1_1_POOL.free(this)
                Gdx.app.debug("GenericGlassCrackedPieceActor1_1", "freed")
            }
        }
    }

    private class GenericGlassCrackedPieceActor1_2 private constructor() : GenericGlassCrackedPieceActor() {
        public override fun init(
                width: Float,
                height: Float,
                x: Float,
                y: Float,
                assets: Assets,
                data: GenericGlassBallCrackedPieceActorData,
                fadeOutTime: Float,
                delayTime: Float,
                crackDeltaV: Float) {
            super.initialize(
                    width, height, x, y, assets, data.piece1_2Atlas, fadeOutTime, delayTime, 0.04f)
            addAction(
                    sequence(
                            parallel(
                                    moveBy(-width / 3, 0f, 0.2f),
                                    moveBy(0f, -3 * crackDeltaV, 1.3f, Interpolation.slowFast)),
                            removeActor()))
        }

        override fun setParent(parent: Group?) {
            super.setParent(parent)
            if (parent == null) {
                GENERIC_GLASS_CRACKED_PIECE_ACTOR_1_2_POOL.free(this)
                Gdx.app.debug("GenericGlassCrackedPieceActor1_2", "freed")
            }
        }
    }

    private class GenericGlassCrackedPieceActor2 private constructor() : GenericGlassCrackedPieceActor() {
        public override fun init(
                width: Float,
                height: Float,
                x: Float,
                y: Float,
                assets: Assets,
                data: GenericGlassBallCrackedPieceActorData,
                fadeOutTime: Float,
                delayTime: Float,
                crackDeltaV: Float) {
            super.initialize(
                    width, height, x, y, assets, data.piece2Atlas, fadeOutTime, delayTime, 0.04f)
            addAction(
                    sequence(
                            moveBy(0f, width * 1.33f, CLIMB_DURATION, Interpolation.fastSlow),
                            moveBy(0f, -3 * crackDeltaV, 1.3f, Interpolation.slowFast),
                            removeActor()))
        }

        override fun setParent(parent: Group?) {
            super.setParent(parent)
            if (parent == null) {
                GENERIC_GLASS_CRACKED_PIECE_ACTOR_2_POOL.free(this)
                Gdx.app.debug("GenericGlassCrackedPieceActor2", "freed")
            }
        }
    }

    private class GenericGlassCrackedPieceActor2_1 private constructor() : GenericGlassCrackedPieceActor() {
        public override fun init(
                width: Float,
                height: Float,
                x: Float,
                y: Float,
                assets: Assets,
                data: GenericGlassBallCrackedPieceActorData,
                fadeOutTime: Float,
                delayTime: Float,
                crackDeltaV: Float) {
            super.initialize(
                    width, height, x, y, assets, data.piece2_1Atlas, fadeOutTime, delayTime, 0.04f)
            addAction(
                    parallel(
                            moveBy(width / 3, 0f, 0.3f),
                            sequence(
                                    moveBy(0f, width * 1.33f, CLIMB_DURATION, Interpolation.fastSlow),
                                    moveBy(0f, -3 * crackDeltaV, 1.3f, Interpolation.slowFast),
                                    removeActor())))
        }

        override fun setParent(parent: Group?) {
            super.setParent(parent)
            if (parent == null) {
                GENERIC_GLASS_CRACKED_PIECE_ACTOR_2_1_POOL.free(this)
                Gdx.app.debug("GenericGlassCrackedPieceActor2_1", "freed")
            }
        }
    }

    private class GenericGlassCrackedPieceActor2_2 private constructor() : GenericGlassCrackedPieceActor() {
        public override fun init(
                width: Float,
                height: Float,
                x: Float,
                y: Float,
                assets: Assets,
                data: GenericGlassBallCrackedPieceActorData,
                fadeOutTime: Float,
                delayTime: Float,
                crackDeltaV: Float) {
            super.initialize(
                    width, height, x, y, assets, data.piece2_2Atlas, fadeOutTime, delayTime, 0.04f)
            addAction(
                    parallel(
                            moveBy(-width / 3, 0f, 0.3f),
                            sequence(
                                    moveBy(0f, width * 1.33f, CLIMB_DURATION, Interpolation.fastSlow),
                                    moveBy(0f, -3 * crackDeltaV, 1.3f, Interpolation.slowFast),
                                    removeActor())))
        }

        override fun setParent(parent: Group?) {
            super.setParent(parent)
            if (parent == null) {
                GENERIC_GLASS_CRACKED_PIECE_ACTOR_2_2_POOL.free(this)
                Gdx.app.debug("GenericGlassCrackedPieceActor2_2", "freed")
            }
        }
    }

    private class RandomPieceActor private constructor() : GenericGlassCrackedPieceActor() {
        public override fun init(
                width: Float,
                height: Float,
                x: Float,
                y: Float,
                assets: Assets,
                data: GenericGlassBallCrackedPieceActorData,
                fadeOutTime: Float,
                delayTime: Float,
                crackDeltaV: Float) {
            super.initialize(
                    width,
                    height,
                    x,
                    y,
                    assets,
                    data.crack1Atlas,
                    data.crackFadeOutTime
                            ?: DEFAULT_FADE_OUT_TIME / 2,
                    data.crackDelayTime
                            ?: DEFAULT_DELAY_TIME / 3,
                    0.02f)
            addAction(
                    sequence(
                            moveBy(0f, -3 * crackDeltaV, 1.3f, Interpolation.slowFast),
                            removeActor()))
        }

        override fun setParent(parent: Group?) {
            super.setParent(parent)
            if (parent == null) {
                RANDOM_PIECE_ACTOR_POOL.free(this)
                Gdx.app.debug("RandomPieceActor", "freed")
            }
        }
    }

    companion object {
        const val DEFAULT_FADE_OUT_TIME = 2f
        const val DEFAULT_DELAY_TIME = 0.5f
        private const val MAX_CRACK_POOL_INSTANCE_COUNT = 200
        private val GENERIC_GLASS_CRACKED_PIECE_ACTOR_1_POOL = Pools.get(GenericGlassCrackedPieceActor1::class.java, MAX_CRACK_POOL_INSTANCE_COUNT)
        private val GENERIC_GLASS_CRACKED_PIECE_ACTOR_2_1_POOL = Pools.get(GenericGlassCrackedPieceActor2_1::class.java, MAX_CRACK_POOL_INSTANCE_COUNT)
        private val GENERIC_GLASS_CRACKED_PIECE_ACTOR_1_1_POOL = Pools.get(GenericGlassCrackedPieceActor1_1::class.java, MAX_CRACK_POOL_INSTANCE_COUNT)
        private val GENERIC_GLASS_CRACKED_PIECE_ACTOR_1_2_POOL = Pools.get(GenericGlassCrackedPieceActor1_2::class.java, MAX_CRACK_POOL_INSTANCE_COUNT)
        private val GENERIC_GLASS_CRACKED_PIECE_ACTOR_2_POOL = Pools.get(GenericGlassCrackedPieceActor2::class.java, MAX_CRACK_POOL_INSTANCE_COUNT)
        private val GENERIC_GLASS_CRACKED_PIECE_ACTOR_2_2_POOL = Pools.get(GenericGlassCrackedPieceActor2_2::class.java, MAX_CRACK_POOL_INSTANCE_COUNT)
        private val RANDOM_PIECE_ACTOR_POOL = Pools.get(RandomPieceActor::class.java, MAX_CRACK_POOL_INSTANCE_COUNT)
        const val CLIMB_DURATION = 0.4f
        private var crackTurn = 0

        @JvmOverloads
        fun createAllPiecesWithSplit(
                x: Float,
                y: Float,
                assets: Assets,
                sizeInMeter: Float,
                data: GenericGlassBallCrackedPieceActorData,
                fadeOutTime: Float,
                delayTime: Float,
                stage: Stage,
                crackDelta: Float = 1f,
                crackDeltaV: Float = 1f) {
            when (crackTurn) {
                0 -> {
                    GENERIC_GLASS_CRACKED_PIECE_ACTOR_1_POOL.obtain()
                            .apply {
                                init(sizeInMeter, sizeInMeter, x, y, assets, data, fadeOutTime, delayTime, crackDeltaV)
                            }.also { stage.addActor(it) }

                    GENERIC_GLASS_CRACKED_PIECE_ACTOR_2_POOL.obtain()
                            .apply {
                                init(
                                        sizeInMeter, sizeInMeter, x, y, assets, data, fadeOutTime, delayTime, crackDeltaV)
                            }.also { stage.addActor(it) }
                }
                1 -> {
                    GENERIC_GLASS_CRACKED_PIECE_ACTOR_1_POOL.obtain()
                            .apply {
                                init(sizeInMeter, sizeInMeter, x, y, assets, data, fadeOutTime, delayTime, crackDeltaV)
                            }.also { stage.addActor(it) }

                    GENERIC_GLASS_CRACKED_PIECE_ACTOR_2_1_POOL.obtain()
                            .apply {
                                init(sizeInMeter, sizeInMeter, x, y, assets, data, fadeOutTime, delayTime, crackDeltaV)
                            }.also { stage.addActor(it) }

                    GENERIC_GLASS_CRACKED_PIECE_ACTOR_2_2_POOL.obtain()
                            .apply {
                                init(sizeInMeter, sizeInMeter, x, y, assets, data, fadeOutTime, delayTime, crackDeltaV)
                            }.also { stage.addActor(it) }
                }
                2 -> {
                    GENERIC_GLASS_CRACKED_PIECE_ACTOR_1_1_POOL.obtain()
                            .apply {
                                init(sizeInMeter, sizeInMeter, x, y, assets, data, fadeOutTime, delayTime, crackDeltaV)
                            }.also { stage.addActor(it) }
                    GENERIC_GLASS_CRACKED_PIECE_ACTOR_1_2_POOL.obtain()
                            .apply {
                                init(sizeInMeter, sizeInMeter, x, y, assets, data, fadeOutTime, delayTime, crackDeltaV)
                            }.also { stage.addActor(it) }

                    GENERIC_GLASS_CRACKED_PIECE_ACTOR_2_POOL.obtain()
                            .apply {
                                init(sizeInMeter, sizeInMeter, x, y, assets, data, fadeOutTime, delayTime, crackDeltaV)
                            }.also { stage.addActor(it) }
                }
                3 -> {
                    GENERIC_GLASS_CRACKED_PIECE_ACTOR_1_1_POOL.obtain()
                            .apply {
                                init(sizeInMeter, sizeInMeter, x, y, assets, data, fadeOutTime, delayTime, crackDeltaV)
                            }.also { stage.addActor(it) }
                    GENERIC_GLASS_CRACKED_PIECE_ACTOR_1_2_POOL.obtain()
                            .apply {
                                init(sizeInMeter, sizeInMeter, x, y, assets, data, fadeOutTime, delayTime, crackDeltaV)
                            }.also { stage.addActor(it) }
                    GENERIC_GLASS_CRACKED_PIECE_ACTOR_2_1_POOL.obtain()
                            .apply {
                                init(sizeInMeter, sizeInMeter, x, y, assets, data, fadeOutTime, delayTime, crackDeltaV)
                            }.also { stage.addActor(it) }

                    GENERIC_GLASS_CRACKED_PIECE_ACTOR_2_2_POOL.obtain().apply {
                        init(sizeInMeter, sizeInMeter, x, y, assets, data, fadeOutTime, delayTime, crackDeltaV)
                    }.also { stage.addActor(it) }
                }
                else -> throw IllegalStateException()
            }
            if (data.crack1Atlas != null) {
                val delta = sizeInMeter * crackDelta
                for (i in MathUtils.random(5, 11) downTo 1) {
                    val deltaX = MathUtils.random(-delta / 4, delta / 4)
                    val deltaY = MathUtils.random(-delta / 4, delta / 4)
                    RANDOM_PIECE_ACTOR_POOL.obtain().apply {
                        init(sizeInMeter / MathUtils.random(1.2f, 9f),
                                sizeInMeter / MathUtils.random(1.2f, 9f),
                                x + sizeInMeter / 2 + deltaX,
                                y + sizeInMeter / 2 + deltaY,
                                assets,
                                data, -1f, -1f,
                                crackDeltaV)
                        val x_XV = MathUtils.random(delta / 2, 3 * delta / 2)
                        addAction(moveBy(if (deltaX < 0) -x_XV else x_XV, 0f, x_XV, Interpolation.fastSlow))
                    }.also { stage.addActor(it) }
                }
            }
            crackTurn = (crackTurn + 1) % 4
        }

        fun disposeObjectPools() {
            Gdx.app.debug("com.parabolagames.glassmaze.GenericGlassCrackedPieceActor", "disposing pools")
            GENERIC_GLASS_CRACKED_PIECE_ACTOR_1_POOL.clear()
            GENERIC_GLASS_CRACKED_PIECE_ACTOR_2_1_POOL.clear()
            GENERIC_GLASS_CRACKED_PIECE_ACTOR_1_1_POOL.clear()
            GENERIC_GLASS_CRACKED_PIECE_ACTOR_1_2_POOL.clear()
            GENERIC_GLASS_CRACKED_PIECE_ACTOR_2_POOL.clear()
            GENERIC_GLASS_CRACKED_PIECE_ACTOR_2_2_POOL.clear()
            RANDOM_PIECE_ACTOR_POOL.clear()
        }
    }
}