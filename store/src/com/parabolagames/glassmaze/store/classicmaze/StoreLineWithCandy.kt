package com.parabolagames.glassmaze.store.classicmaze

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Pools
import com.parabolagames.glassmaze.framework.labelStyle
import com.parabolagames.glassmaze.shared.Assets
import com.parabolagames.glassmaze.shared.Constants
import com.parabolagames.glassmaze.shared.candy2.CandyActor
import kotlin.properties.Delegates

internal class StoreLineWithCandy private constructor() : AbstractStoreLine() {
    private lateinit var buyButton: CandyButton
    fun init(assets: Assets, spinyCount: Int, candyCount: Int, candyCountForMoney: Int, purchaseWithCandyClassicMaze: IPurchaseWithCandyClassicMaze) {
        super.init(assets, spinyCount, candyCount)
        if (!::buyButton.isInitialized) {
            buyButton = CandyButton(assets, purchaseWithCandyClassicMaze)
        }

        add(buyButton).size(0.7f * Constants.DIALOG_SIZE_RATIO, 0.3f * Constants.DIALOG_SIZE_RATIO)
                .expand()
                .fill()
                .right()
                .pad(0.01f * Constants.DIALOG_SIZE_RATIO)
                .padLeft(0.2f * Constants.DIALOG_SIZE_RATIO)
                .padRight(0.1f * Constants.DIALOG_SIZE_RATIO)

        buyButton.setCandyCount(spinyCount, candyCount, candyCountForMoney)
    }


    override fun free() {
        POOL.free(this)
        Gdx.app.debug(TAG, "freed")
    }

    companion object {
        private val POOL = Pools.get(StoreLineWithCandy::class.java)
        private const val TAG = "com.parabolagames.glassmaze.StoreLineWithCandy"
        fun create(assets: Assets,
                   spinyCount: Int,
                   candyCount: Int,
                   candyCountForMoney: Int,
                   purchaseWithCandyClassicMaze: IPurchaseWithCandyClassicMaze): StoreLineWithCandy = POOL.obtain()
                .apply {
                    init(assets, spinyCount, candyCount, candyCountForMoney, purchaseWithCandyClassicMaze)
                }

        fun disposeObjectPools() {
            POOL.clear()
            Gdx.app.debug(TAG,"disposing pools")
        }
    }

    private class CandyButton(assets: Assets, private val purchaseWithCandyClassicMaze: IPurchaseWithCandyClassicMaze) : TextButton("", TextButtonStyle().apply {
        val textureAtlas = assets.getTextureAtlas(Assets.BUTTONS_RECTANGLE)
        up = TextureRegionDrawable(textureAtlas.findRegion("released"))
        down = TextureRegionDrawable(textureAtlas.findRegion("pressed"))
        font = assets.getBitmapFont(Assets.FONT_COSMIC_SANS_ORANGE)
        downFontColor = Color.GRAY
        fontColor = Color.WHITE
    }) {
        private val lblCandyCount: Label = Label("", labelStyle(assets.getBitmapFont(Assets.FONT_COSMIC_SANS_GREEN)))
                .apply {
                    setAlignment(Align.left)
                    setFontScale(2f)
                    wrap = false
                }
        private val candy: CandyActor = CandyActor.getCandy(assets, Assets.CANDY14_ATLAS, 0.15f * Constants.DIALOG_SIZE_RATIO, true) { 0.01f }

        private var spinyCount by Delegates.notNull<Int>()
        private var candyCount by Delegates.notNull<Int>()
        private var candyCountForMoney by Delegates.notNull<Int>()

        init {
            label = lblCandyCount
            removeActor(lblCandyCount)

            add(candy).left().padLeft(0.01f * Constants.DIALOG_SIZE_RATIO).padRight(0.03f * Constants.DIALOG_SIZE_RATIO)
            add(lblCandyCount).expand().fill()

            addListener(object : ChangeListener() {
                override fun changed(event: ChangeEvent, actor: Actor) = purchaseWithCandyClassicMaze.purchase(spinyCount, candyCount, candyCountForMoney)
            })
        }

        fun setCandyCount(spinyCount: Int, candyCount: Int, candyCountForMoney: Int) {
            lblCandyCount.setText("$candyCountForMoney")
            this.candyCountForMoney = candyCountForMoney
            this.spinyCount = spinyCount
            this.candyCount = candyCount
        }

        override fun draw(batch: Batch?, parentAlpha: Float) {

            candy.color = if (isPressed && style.downFontColor != null)
                style.downFontColor
            else
                style.fontColor
            super.draw(batch, parentAlpha)
        }
    }
}