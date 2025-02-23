package com.parabolagames.glassmaze.classic

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.actions.Actions.*
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Pool
import com.badlogic.gdx.utils.Pools
import com.parabolagames.glassmaze.classic.glass.CandyParticleEffect
import com.parabolagames.glassmaze.framework.TableActor
import com.parabolagames.glassmaze.framework.labelStyle
import com.parabolagames.glassmaze.framework.run2
import com.parabolagames.glassmaze.shared.Assets
import javax.inject.Inject

internal class BonusCandyHandActor : TableActor(), Pool.Poolable {

    private lateinit var candyHandActorBonusActorInterface: ICandyHandActorBonusActorInterface

    private var lblCount: Label? = null

    private fun poolInit(assets: Assets,
                         candyHandActorBonusActorInterface: ICandyHandActorBonusActorInterface): BonusCandyHandActor {
        if (animation == null) {
            loadTexture(assets.getTexture(Assets.STORE_CANDY_GLASS_HAND))
            candyEffect.load(Gdx.files.internal("effects/candy17.p"), assets.getTextureAtlas(Assets.FIRE_ATLAS))

            lblCount = Label("1", labelStyle(assets.getBitmapFont(Assets.FONT_COSMIC_SANS_ORANGE)))
                    .apply {
                        setAlignment(Align.left)
                        wrap = false
                        setPosition(width, 0f)
                    }
            addActor(lblCount)
        }
        setWidthHeightFromHeight(0.5f)
        setOrigin(width / 2, height / 2)
        this.candyHandActorBonusActorInterface = candyHandActorBonusActorInterface
        return this
    }

    override fun sizeChanged() {
        super.sizeChanged()
        lblCount?.setPosition(width, -lblCount!!.height)
        lblCount?.setFontScale(width * 0.004f)
    }

    override fun setParent(parent: Group?) {
        super.setParent(parent)
        if (parent == null) {
            POOL.free(this)
            Gdx.app.debug(TAG, "freed")
        }
    }

    override fun reset() {
        poolReset()
        clearActions()
        setScale(1f)
        alpha(1f)
        setColor(color.r, color.g, color.b, 1f)
    }

    var candyEffectShouldDraw = false
    private val velocityAngle = Vector2()
    private val candyEffect = CandyParticleEffect()


    fun addBonusAction() {
        candyEffect.setTarget(candyHandActorBonusActorInterface)
        addAction(sequence(
                alpha(0f),
                parallel(fadeIn(0.5f),
                        moveTo(x, 0.4f, 0.5f, Interpolation.pow3)),
                parallel(
                        moveTo(candyHandActorBonusActorInterface.posX - width / 2, candyHandActorBonusActorInterface.posY - height / 2, 1.5f),
                        sequence(delay(1.25f), fadeOut(0.25f)),
                        scaleBy(-0.5f, -0.5f, 1f),
                        run2 {
                            velocityAngle.set(candyHandActorBonusActorInterface!!.posX - (x + width / 2),
                                    candyHandActorBonusActorInterface!!.posY - (y + height / 2))
                            candyEffect.emitters.forEach {
                                it.velocity.setHigh(8f)
                                it.velocity.setLow(it.velocity.highMin)
                                it.angle.setHigh(velocityAngle.angleDeg())
                                it.angle.setLow(it.angle.highMin)
                            }

                            candyEffect.start()
                            candyEffectShouldDraw = true
                        }
                ),
                run2 {
                    candyEffectShouldDraw = false
                    candyEffect.reset(true)
                },
                run2 {
                    candyHandActorBonusActorInterface.giftGained()
                },
                removeActor()
        ))
    }

    override fun act(dt: Float) {
        super.act(dt)
        if (candyEffectShouldDraw) {
            candyEffect.emitters.forEach {
                it.setPosition(x + width / 2, y + height / 2)
                it.scaleSize(color.a)
                it.xScale.setHigh(0.22f * color.a * color.a)
                it.xScale.setLow(it.xScale.highMin)
                it.yScale.setHigh(it.xScale.highMin)
                it.yScale.setLow(it.xScale.highMin)
                it.update(Gdx.app.graphics.deltaTime)
            }
        }
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        if (candyEffectShouldDraw) {
            candyEffect.draw(batch)
        }
        super.draw(batch, parentAlpha)
    }

    internal class BonusCandyHandActorFactory @Inject constructor(private val assets: Assets,
                                                                  private val candyHandActorBonusActorInterface: ICandyHandActorBonusActorInterface) {

        fun getBonusCandyHandActor() = POOL.obtain().poolInit(assets, candyHandActorBonusActorInterface)
    }

    companion object {
        fun disposeObjectPools() {
            Gdx.app.debug(TAG, "disposing object pools")
            POOL.clear()
        }

        private val POOL: Pool<BonusCandyHandActor> = Pools.get(BonusCandyHandActor::class.java)

        private const val TAG = "com.parabolagames.glassmaze.BonusCandyHandActor"
    }
}