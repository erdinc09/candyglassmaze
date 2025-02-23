package com.parabolagames.glassmaze.candymode

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.MathUtils.clamp
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.actions.Actions.*
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Pool
import com.badlogic.gdx.utils.Pools
import com.google.common.base.Preconditions
import com.parabolagames.glassmaze.framework.labelStyle
import com.parabolagames.glassmaze.shared.*
import java.util.*

internal class ThrowableIronBallActor
    : ThrowableBallActor(), IBox2DActorRemovalListener, ICrackListener, Pool.Poolable {

    private val crackedList: MutableList<Vector2> = ArrayList()
    private val actorType = ActorTypeData(ActorType.IRON_BALL, this)
    private var isReset = true

    private lateinit var assets: Assets
    private var stageForGift: Stage? = null
    private var ironBallGiftListener: IIronBallGiftListener? = null

    fun init(assets: Assets,
             x: Float,
             y: Float,
             ballRadius: Float,
             world: World,
             stageForGift: Stage?,
             ironBallGiftListener: IIronBallGiftListener?) {
        isReset = false
        this.assets = assets
        this.stageForGift = stageForGift
        this.ironBallGiftListener = ironBallGiftListener

        if (animation == null) {
            loadTexture(assets.getTexture(Assets.IRON_BALL))
        }

        setPosition(x, y)
        setSize(ballRadius * 2, ballRadius * 2)

        if (fixtureDef.shape == null) {
            setDynamic()
            setShapeCircle()
            setPhysicsProperties(10f, 1f, 1f)
            setFixedRotation()
        } else {
            resetShapeCirclePosition()
        }

        initializePhysics(world)
    }

    override fun initializePhysics(w: World) {
        super.initializePhysics(w)
        bodyUserData = actorType
    }

    override fun actorRemoved(x: Float, y: Float) {
        if (crackedList.size >= GIFT_LIMIT) {

            Label("${getGoodnessText(crackedList.size)}\n+${crackedList.size}",
                    labelStyle(assets.getBitmapFont(Assets.FONT_COSMIC_SANS_ORANGE_BOLD)))
                    .apply {
                        touchable = Touchable.disabled
                        setFontScale(0.003f)
                        val startPos = crackedList[0]
                        val endPos = crackedList[crackedList.size - 1]
                        setPosition(clamp((startPos.x + endPos.x) / 2, 0f, Constants.WORLD_WIDTH - width),
                                clamp((startPos.y + endPos.y) / 2, 0f, Constants.WORLD_HEIGHT - height))
                        addAction(sequence(delay(1.25f), fadeOut(1f), removeActor()))
                    }.also {
                        stageForGift!!.addActor(it)
                    }

            ironBallGiftListener!!.timesGlassBroken(crackedList.size)
        }
    }

    private fun getGoodnessText(size: Int): String = when (size) {
        GIFT_LIMIT -> "GOOD !"
        GIFT_LIMIT + 1 -> "EXCELLENT !"
        else -> "PERFECT !"
    }

    override fun crackedByMe(pos: Vector2, crackenType: ActorType) {
        if (crackenType === ActorType.GLASS || crackenType === ActorType.SPINY_GLASS) {
            crackedList.add(pos)
        }
    }

    override fun reset() {
        poolReset()
        clearActions()
        ironBallGiftListener = null
        stageForGift = null
        crackedList.clear()
        isReset = true
        setColor(color.r, color.g, color.b, 1f)
    }

    override fun setParent(parent: Group?) {
        super.setParent(parent)
        if (parent == null) {
            Preconditions.checkState(!isReset)
            THROWABLE_IRON_BALL_ACTOR_POOL.free(this)
            Gdx.app.debug(TAG, "freed")
        }
    }

    companion object {
        private const val GIFT_LIMIT = 3
        private val THROWABLE_IRON_BALL_ACTOR_POOL: Pool<ThrowableIronBallActor> = Pools.get(ThrowableIronBallActor::class.java)
        private const val TAG = "ThrowableIronBallActor"
        fun createForGame(
                x: Float,
                y: Float,
                assets: Assets,
                stageForGift: Stage,
                world: World,
                ironBallGiftListener: IIronBallGiftListener?): ThrowableIronBallActor = THROWABLE_IRON_BALL_ACTOR_POOL.obtain()
                .apply {
                    init(assets, x, y, BALL_RADIUS, world, stageForGift, ironBallGiftListener)
                    Box2DActorDelayedRemover.add(this)
                }


        fun createForUI(x: Float, y: Float, assets: Assets, world: World, ballRadius: Float): ThrowableIronBallActor = THROWABLE_IRON_BALL_ACTOR_POOL.obtain()
                .apply {
                    init(assets, x, y, ballRadius, world, null, ironBallGiftListener)
                }
    }
}