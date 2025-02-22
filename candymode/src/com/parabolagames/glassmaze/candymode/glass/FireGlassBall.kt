package com.parabolagames.glassmaze.candymode.glass

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.ParticleEffect
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Pool
import com.badlogic.gdx.utils.Pools
import com.google.common.base.Preconditions
import com.parabolagames.glassmaze.framework.Box2DActor
import com.parabolagames.glassmaze.shared.*
import com.parabolagames.glassmaze.shared.crack.GenericGlassCrackedPieceActor


internal class FireGlassBall : Box2DActor(), IBreakable, Pool.Poolable {
    private val typeData = ActorTypeData(ActorType.FIRE_GLASS, this)
    private val clickListener: ClickListener = ClickListener()
    private lateinit var soundPlayer: SoundPlayer
    private var stageForPieces: Stage? = null
    private lateinit var assets: Assets

    private var isCracked = false
    private var isMarkedForBreak = false
    private var soundWillBePlayed = false
    private var becameVisible = false

    private val fireEffect = ParticleEffect()
    private val explosionEffect = ParticleEffect()

    private var mode: ExplanationOrGame? = null
    private var isReset = true

    init {
        addListener(clickListener)
    }

    fun poolInitGame(assets: Assets,
                     stageForPieces: Stage,
                     soundPlayer: SoundPlayer,
                     posX: Float,
                     posY: Float,
                     world: World,
                     widthAndHeight: Float): FireGlassBall {
        this.mode = ExplanationOrGame.GAME
        this.isReset = false
        this.assets = assets
        this.stageForPieces = stageForPieces
        this.soundPlayer = soundPlayer
        init(posX, posY, world, widthAndHeight)
        return this
    }

    private fun init(
            posX: Float,
            posY: Float,
            world: World,
            widthAndHeight: Float) {
        if (animation == null) {
            loadTexture(assets.getTexture(GenericGlassBallCrackedPieceActorData.GLASS_4.glassAtlas))
            fireEffect.load(Gdx.files.internal("effects/fire5.p"), assets.getTextureAtlas(Assets.FIRE_ATLAS))
            fireEffect.scaleEffect(0.003f)

            explosionEffect.load(Gdx.files.internal("effects/explosion1.p"), assets.getTextureAtlas(Assets.FIRE_ATLAS))
            explosionEffect.scaleEffect(0.015f)
        }
        addAction(Actions.alpha(0.8f))
        fireEffect.start()

        setSize(widthAndHeight, widthAndHeight)
        setPosition(posX, posY)

        if (fixtureDef.shape == null) {
            setDynamic()
            setShapeCircle()
            setPhysicsProperties(1f, 0.5f, 0.1f)
            setFixedRotation()
        } else {
            resetShapeCirclePosition()
        }

        initializePhysics(world)
    }

    override fun act(dt: Float) {
        super.act(dt)
        fireEffect.setPosition(x + originX, y + originY)
        fireEffect.update(dt)

        if (exploded) {
            explosionEffect.update(dt)
        }

        if (!isCracked && (clickListener.isPressed || isMarkedForBreak)) {
            isCracked = true
            soundWillBePlayed = true
            breakTheGlass()
        }

        if (!becameVisible && y + height > 0) {
            becameVisible = true
            soundPlayer.playGlassAppearSound()
        }
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        if (exploded) {
            if (!explosionEffect.isComplete) {
                explosionEffect.draw(batch)
            }
        } else {
            fireEffect.draw(batch)
        }
        super.draw(batch, parentAlpha)
    }

    private var exploded = false
    override fun breakTheGlass() {
        if (soundWillBePlayed) {
            soundPlayer.playRandomBreak()
        }

        GenericGlassCrackedPieceActor.createAllPiecesWithSplit(
                x,
                y,
                assets,
                width,
                GenericGlassBallCrackedPieceActorData.GLASS_4,
                GenericCandyGlassBallActor.DEFAULT_FADE_OUT_TIME,
                GenericCandyGlassBallActor.DEFAULT_DELAY_TIME,
                stageForPieces!!,
                1f,
                1f)

        addAction(Actions.alpha(0.02f))
        exploded = true
        explosionEffect.setPosition(x + originX, y + originY)
        explosionEffect.start()
    }

    override fun setParent(parent: Group?) {
        super.setParent(parent)
        if (parent == null) {
            Preconditions.checkState(!isReset)
            FIRE_GLASS_BALL_ACTOR_POOL.free(this)
            Gdx.app.debug(TAG, "freed")
            if (exploded) {
                explosionEffect.reset(false)
                exploded = false
            }
        }
    }

    override fun initializePhysics(w: World) {
        super.initializePhysics(w)
        bodyUserData = typeData
    }

    override fun markForBreak(soundWillBePlayed: Boolean) {
        this.soundWillBePlayed = soundWillBePlayed
        isMarkedForBreak = true
    }

    override fun reset() {
        poolReset()
        clearActions()
        fireEffect.reset(false)
        isCracked = false
        isReset = true
        isMarkedForBreak = false
        stageForPieces = null
        soundWillBePlayed = false
        becameVisible = false
        mode = null
        setColor(color.r, color.g, color.b, 1f)
    }

    companion object {
        private const val TAG = "FireGlassBall"

        val FIRE_GLASS_BALL_ACTOR_POOL: Pool<FireGlassBall> = Pools.get(FireGlassBall::class.java)
    }
}