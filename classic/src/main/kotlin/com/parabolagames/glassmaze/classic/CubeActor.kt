package com.parabolagames.glassmaze.classic

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.Pool
import com.badlogic.gdx.utils.Pool.Poolable
import com.badlogic.gdx.utils.Pools
import com.google.common.base.Preconditions.checkState
import com.parabolagames.glassmaze.framework.TableActor
import com.parabolagames.glassmaze.shared.Assets
import java.util.*

internal class CubeActor : TableActor(), Poolable {

    private var isReset = true

    fun poolInit(assets: Assets, row: Int, col: Int, xGridActor: Float, yGridActor: Float) {
        if (!isTexturesInitialized) {
            loadCubeTextures(assets)
        }

        val selectedTextures = ANIMATION_MAP[atlasTurn]
        setAnimation(selectedTextures!!.get(ANIMATION_RANDOM.nextInt(selectedTextures.size)))
        setSize(GridActor.CELL_SIZE - offset, GridActor.CELL_SIZE - offset)
        setPosition(
                xGridActor + GridActor.CELL_SIZE * col + offset,
                yGridActor + GridActor.CELL_SIZE * row + offset)
        isReset = false
    }

    private fun loadCubeTextures(assets: Assets) {
        for (textureType in enumValues<TextureType>()) {
            var regions = assets.getTextureAtlas(textureType.path).regions
            val animations = Array<Animation<TextureRegion>>()
            for (region in regions) {
                animations.add(loadTextureRegion(region))
            }
            ANIMATION_MAP[textureType] = animations
        }
        isTexturesInitialized = true
    }

    override fun setParent(parent: Group?) {
        super.setParent(parent)
        if (parent == null) {
            checkState(!isReset)
            CUBE_ACTOR_POOL.free(this)
            Gdx.app.debug(TAG, "freed")
        }
    }

    override fun reset() {
        super.poolReset()
        isReset = true
    }

    companion object {
        val CUBE_ACTOR_POOL: Pool<CubeActor> = Pools.get(CubeActor::class.java, 200)
        val ANIMATION_MAP: MutableMap<TextureType, Array<Animation<TextureRegion>>> = mutableMapOf()
        const val offset = 0.01f
        private val ANIMATION_RANDOM = Random()
        private val TEXTURE_TYPE_RANDOM = Random()
        private var isTexturesInitialized = false
        private lateinit var atlasTurn: TextureType
        private val TEXTURE_TYPE_ARRAY = enumValues<TextureType>()

        @JvmStatic
        fun getCubeActorFromPool(
                assets: Assets, row: Int, col: Int, xGridActor: Float, yGridActor: Float): CubeActor {
            return CUBE_ACTOR_POOL.obtain().apply { poolInit(assets, row, col, xGridActor, yGridActor) }
        }

        @JvmStatic
        fun setAtlasTurnRandomly() {
            atlasTurn = TEXTURE_TYPE_ARRAY[TEXTURE_TYPE_RANDOM.nextInt(TEXTURE_TYPE_ARRAY.size)]
        }

        fun disposeObjectPools() {
            CUBE_ACTOR_POOL.clear()
            ANIMATION_MAP.clear()
            isTexturesInitialized = false
            Gdx.app.debug(TAG, "disposing pools")
        }

        private const val TAG = "com.parabolagames.glassmaze.CubeActor"
    }

    internal enum class TextureType(val path: String) {
        TYPE0(Assets.MAZE_CUBES),
        TYPE1(Assets.MAZE_CUBES_2),
        TYPE2(Assets.MAZE_CUBES_3),
        TYPE3(Assets.MAZE_CUBES_4),
        TYPE4(Assets.MAZE_CUBES_5)
    }
}