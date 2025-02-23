package com.parabolagames.glassmaze.store.classicmaze

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.Pool.Poolable
import com.badlogic.gdx.utils.Pools
import com.parabolagames.glassmaze.shared.Assets
import com.parabolagames.glassmaze.shared.ui.GM_Label
import com.parabolagames.glassmaze.store.CurrencyHelper

internal class StoreLineCurrencyTest private constructor() : AbstractStoreLine(), Poolable {

    fun init(assets: Assets) {
        super.init(assets, 0, 0)

        GM_Label("""
            ${getCurrencyTestLine("BGN")}
            ${getCurrencyTestLine("CHF")}
            ${getCurrencyTestLine("CZK")}
            ${getCurrencyTestLine("DKK")}
            ${getCurrencyTestLine("EUR")}
            ${getCurrencyTestLine("GBP")}
            ${getCurrencyTestLine("HRK")}
            ${getCurrencyTestLine("GEL")}
            ${getCurrencyTestLine("HUF")}
            ${getCurrencyTestLine("NOK")}
            ${getCurrencyTestLine("PLN")}
            ${getCurrencyTestLine("RUB")}
            ${getCurrencyTestLine("RON")}
            ${getCurrencyTestLine("SEK")}
            ${getCurrencyTestLine("TRY")}
            ${getCurrencyTestLine("UAH")}
            
            ${getCurrencyTestLine("AED")}
            ${getCurrencyTestLine("ILS")}
            ${getCurrencyTestLine("KES")}
            ${getCurrencyTestLine("MAD")}
            ${getCurrencyTestLine("NGN")}
            ${getCurrencyTestLine("ZAR")}

            ${getCurrencyTestLine("BRL")}
            ${getCurrencyTestLine("CAD")}
            ${getCurrencyTestLine("CLP")}
            ${getCurrencyTestLine("COP")}
            ${getCurrencyTestLine("MXN")}
            ${getCurrencyTestLine("PEN")}
            ${getCurrencyTestLine("USD")}
            
            ${getCurrencyTestLine("AUD")}
            ${getCurrencyTestLine("BDT")}
            ${getCurrencyTestLine("CNY")}
            ${getCurrencyTestLine("HKD")}
            ${getCurrencyTestLine("IDR")}
            ${getCurrencyTestLine("INR")}
            ${getCurrencyTestLine("JPY")}
            ${getCurrencyTestLine("MYR")}
            ${getCurrencyTestLine("NZD")}
            ${getCurrencyTestLine("PHP")}
            ${getCurrencyTestLine("PKR")}
            ${getCurrencyTestLine("SGD")}
            ${getCurrencyTestLine("KRW")}
            ${getCurrencyTestLine("LKR")}
            ${getCurrencyTestLine("THB")}
            ${getCurrencyTestLine("VND")}
        """.trimIndent(), assets, Assets.FONT_CURRENCY_MONTSERRAT, 1.6f)
                .also {
                    add(it)
                }
    }

    private fun getCurrencyTestLine(code: String) = "$code = ${CurrencyHelper.getSymbolOrCode(code)}"

    override fun free() {
        POOL.free(this)
        Gdx.app.debug(TAG, "freed")
    }

    companion object {
        private val POOL = Pools.get(StoreLineCurrencyTest::class.java)
        private const val TAG = "StoreLineCurrencyTest"
        fun create(assets: Assets): StoreLineCurrencyTest = POOL.obtain().apply { init(assets) }
    }
}