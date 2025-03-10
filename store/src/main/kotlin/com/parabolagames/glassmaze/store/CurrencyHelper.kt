package com.parabolagames.glassmaze.store

//Gdx.app.debug(TAG, "GPB = ${Utils.getCurrencySymbol("GBP")}")
//https://developers.google.com/adsense/management/appendix/currencies
//https://stackoverflow.com/questions/36258511/how-to-get-currency-symbol-by-currency-name/36258674
//https://transferwise.com/gb/blog/world-currency-symbols
//https://www.xe.com/symbols.php
//https://developers.google.com/adsense/management/appendix/currencies
//Gdx.app.debug(TAG, "Currency.getInstance(\"GBP\") = ${Currency.getInstance("GBP").symbol}")
//Gdx.app.debug(TAG, "Currency.getInstance(\"TRY\") = ${Currency.getInstance("TRY").symbol}")
//Gdx.app.debug(TAG, "Currency.getInstance(\"BGN\") = ${Currency.getInstance("BGN").symbol}")
//Gdx.app.debug(TAG, "Currency.getInstance(\"VND\") = ${Currency.getInstance("VND").symbol}")
//Gdx.app.debug(TAG, "Currency.getInstance(Locale.US).symbol = ${Currency.getInstance(Locale.US).symbol}")
//Gdx.app.debug(TAG, "&#x20AC;")
//https://fonts.google.com/specimen/Baloo+Bhai+2#about

//https://fonts.google.com/?preview.text=%E2%82%BA%C2%A5%D0%BB%D0%B2%E2%82%AC%E0%A7%B3%E2%82%B9%E2%82%BD%E2%82%B4K%C4%8D%E0%B8%BF&preview.text_type=custom   ₺¥лв€৳₹₽₴Kč฿
//https://fonts.google.com/cd?preview.text=%E2%82%BA%C2%A5%D0%BB%D0%B2%E2%82%AC%E0%A7%B3%E2%82%B9%E2%82%BD%E2%82%B4K%C4%8D%E0%B8%BF&preview.text_type=custom
//https://fonts.google.com/specimen/Fira+Sans?preview.text=%E2%82%BA%C2%A5%D0%BB%D0%B2%E2%82%AC%E0%A7%B3%E2%82%B9%E2%82%BD%E2%82%B4K%C4%8D%E0%B8%BF&preview.text_type=custom#standard-styles
//https://fonts.google.com/specimen/Montserrat?preview.text=%E2%82%BA%C2%A5%D0%BB%D0%B2%E2%82%AC%E0%A7%B3%E2%82%B9%E2%82%BD%E2%82%B4K%C4%8D%E2%82%A9%E0%B8%BF&preview.text_type=custom&selection.family=Montserrat:wght@500
internal object CurrencyHelper {
    private val CURRENCY_MAP = mapOf(
//
//            ABCDEFGHIJKLMNOPQRSTUVWXYZ
//                    abcdefghijklmnopqrstuvwxyz
//                    1234567890
//            "!`?'.,;:()[]{}<>|/@\^$-%+=#_&~* 

            //лвCHFKčkr€£knftkr
            //₽leikr₺₴

            //List of currency symbols: Europe
            "BGN" to "лв", //Bulgaria	Bulgarian lev
            "CHF" to "CHF",//Switzerland	Swiss franc
            "CZK" to "Kč",//Czechia	Czech koruna
            "DKK" to "kr",//Denmark	Danish krone
            "EUR" to "€",//Euro area countries	Euro
            "GBP" to "£",//United Kingdom	Pounds sterling
            "HRK" to "kn",//Croatia	Croatian Kuna
            //"GEL" to "₾",//Georgia	Georgian lari
            "HUF" to "ft",//Hungary	Hungarian forint
            "NOK" to "kr",//Norway	Norwegian krone
            "PLN" to "zł",//Poland	Polish zloty
            "RUB" to "₽",//Russia	Russian ruble
            "RON" to "lei",//Romania	Romanian leu
            "SEK" to "kr",//Sweden	Swedish krona
            "TRY" to "₺",//Turkey	Turkish lira
            "UAH" to "₴",//Ukraine	Ukrainian hryvna

            //Ksh₦R
            //List of currency symbols: Middle East and Africa
            //"AED" to "د.إ",//UAE	Emirati dirham
            //"ILS" to "₪",//Israel	Israeli shekel
            "KES" to "Ksh",//Kenya	Kenyan shilling
            //"MAD" to ".د.م",//Morocco	Moroccan dirham
            "NGN" to "₦",//Nigeria	Nigerian naira
            "ZAR" to "R",//South Africa	South african rand

            //R$$S/.
            //List of currency symbols: The Americas
            "BRL" to "R$",//Brazil	Brazilian real
            "CAD" to "$",//Canada	Canadian dollars
            "CLP" to "$",//Chile	Chilean peso
            "COP" to "$",//Colombia	Colombian peso
            "MXN" to "$",//Mexico	Mexican peso
            "PEN" to "S/.",//Peru	Peruvian sol
            "USD" to "$",//USA	US dollar

            //¥HK$Rp₹RM$₱Rs$₫
            //List of currency symbols: Asia &* The Pacific region
            "AUD" to "$",//Australia	Australian dollars
            //"BDT" to "৳",//Bangladesh	Bangladeshi taka
            "CNY" to "¥",//China	Chinese yuan
            "HKD" to "HK$",//Hong Kong	Hong Kong dollar
            "IDR" to "Rp",//Indonesia	Indonesian rupiah
            "INR" to "₹",//India	Indian rupee
            "JPY" to "¥",//Japan	Japanese yen
            "MYR" to "RM",//Malaysia	Malaysian ringgit
            "NZD" to "$",//New Zealand	New Zealand dollar
            "PHP" to "₱",//Philippines	Philippine peso
            "PKR" to "Rs",//Pakistan	Pakistani rupee
            "SGD" to "$",//Singapore	Singapore dollar
            "KRW" to "₩",//South Korea	South Korean won
            "LKR" to "Rs",//Sri Lanka	Sri Lankan rupee
            //"THB" to "฿",//Thailand	Thai baht
            "VND" to "₫",//Vietnam	Vietnamese dong

            //List of currency symbols: Cryptocurrencies
    )

    fun getSymbolOrCode(code: String) = CURRENCY_MAP[code] ?: code
}