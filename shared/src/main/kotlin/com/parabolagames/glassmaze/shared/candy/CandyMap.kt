package com.parabolagames.glassmaze.shared.candy

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.ObjectMap
import com.badlogic.gdx.utils.Pool
import com.parabolagames.glassmaze.shared.Assets

internal object CandyMap {
    const val MAX_POOLED_CANDY_COUNT = 200
    val CANDY_MAP = ObjectMap<String, Pool<out CandyActorBox2D>>()

    init {
        CANDY_MAP.put(Assets.CANDY1_ATLAS, CandyActor1.POOL)
        CANDY_MAP.put(Assets.CANDY2_ATLAS, CandyActor2.POOL)
        CANDY_MAP.put(Assets.CANDY3_ATLAS, CandyActor3.POOL)
        CANDY_MAP.put(Assets.CANDY4_ATLAS, CandyActor4.POOL)
        CANDY_MAP.put(Assets.CANDY5_ATLAS, CandyActor5.POOL)
        CANDY_MAP.put(Assets.CANDY6_ATLAS, CandyActor6.POOL)
        CANDY_MAP.put(Assets.CANDY7_ATLAS, CandyActor7.POOL)
        CANDY_MAP.put(Assets.CANDY8_ATLAS, CandyActor8.POOL)
        CANDY_MAP.put(Assets.CANDY9_ATLAS, CandyActor9.POOL)
        CANDY_MAP.put(Assets.CANDY10_ATLAS, CandyActor10.POOL)
        CANDY_MAP.put(Assets.CANDY11_ATLAS, CandyActor11.POOL)
        CANDY_MAP.put(Assets.CANDY12_ATLAS, CandyActor12.POOL)
        CANDY_MAP.put(Assets.CANDY13_ATLAS, CandyActor13.POOL)
        CANDY_MAP.put(Assets.CANDY14_ATLAS, CandyActor14.POOL)
        CANDY_MAP.put(Assets.CANDY15_ATLAS, CandyActor15.POOL)
        CANDY_MAP.put(Assets.CANDY16_ATLAS, CandyActor16.POOL)
        CANDY_MAP.put(Assets.CANDY17_ATLAS, CandyActor17.POOL)
    }

    fun disposeObjectPools() {
        Gdx.app.debug("com.parabolagames.glassmaze.CandyMap", "disposing pools")
        CANDY_MAP.forEach { it.value.clear() }
    }
}