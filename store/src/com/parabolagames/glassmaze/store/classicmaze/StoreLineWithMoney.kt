package com.parabolagames.glassmaze.store.classicmaze

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.pay.Information
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.utils.Pool.Poolable
import com.badlogic.gdx.utils.Pools
import com.parabolagames.glassmaze.framework.format
import com.parabolagames.glassmaze.shared.Assets
import com.parabolagames.glassmaze.shared.Constants
import com.parabolagames.glassmaze.shared.Utils.textButtonStyleCurrency
import com.parabolagames.glassmaze.store.CurrencyHelper
import com.parabolagames.glassmaze.store.IPurchase
import java.util.*

internal class StoreLineWithMoney private constructor() : AbstractStoreLine(), Poolable {
    private lateinit var buyButton: TextButton
    private lateinit var iapID: String

    fun init(assets: Assets, lineData: LineData, information: Information, purchase: IPurchase) {

        super.init(assets, lineData.spinyCount, lineData.candyCount)
        this.iapID = lineData.iapID


        if (!::buyButton.isInitialized) {
            buyButton = TextButton("", textButtonStyleCurrency(assets))
                    .apply {
                        label.setFontScale(2f)
                        addListener(object : ChangeListener() {
                            override fun changed(event: ChangeEvent, actor: Actor) = purchase.purchase(iapID)
                        })
                    }
        }

        add(buyButton).size(0.7f * Constants.DIALOG_SIZE_RATIO, 0.3f * Constants.DIALOG_SIZE_RATIO)
                .expand()
                .fill()
                .right()
                .pad(0.01f * Constants.DIALOG_SIZE_RATIO)
                .padLeft(0.2f * Constants.DIALOG_SIZE_RATIO)
                .padRight(0.1f * Constants.DIALOG_SIZE_RATIO)

        buyButton.setText(getFormattedPrice(information))
    }

    private fun getFormattedPrice(information: Information): String {
        return if (information.priceAsDouble != null) {
            val defaultFractionDigit = try {
                Currency.getInstance(information.priceCurrencyCode).defaultFractionDigits
            } catch (e: Exception) {
                Gdx.app.debug(TAG, e.stackTraceToString())
                2
            }
            "${CurrencyHelper.getSymbolOrCode(information.priceCurrencyCode)} ${information.priceAsDouble!!.format(defaultFractionDigit)}"
        } else
            "--"
    }

    override fun free() {
        POOL.free(this)
        Gdx.app.debug(TAG, "freed")
    }

    companion object {
        private val POOL = Pools.get(StoreLineWithMoney::class.java)
        private const val TAG = "com.parabolagames.glassmaze.StoreLineWithMoney"
        fun create(assets: Assets, lineData: LineData, information: Information, purchase: IPurchase): StoreLineWithMoney =
                POOL.obtain().apply { init(assets, lineData, information, purchase) }

        fun disposeObjectPools() {
            POOL.clear()
            Gdx.app.debug(TAG, "disposing pools")
        }
    }
}