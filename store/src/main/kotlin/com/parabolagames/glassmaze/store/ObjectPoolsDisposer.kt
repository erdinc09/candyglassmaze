package com.parabolagames.glassmaze.store

import com.badlogic.gdx.Gdx
import com.parabolagames.glassmaze.store.classicmaze.StoreLineWithCandy
import com.parabolagames.glassmaze.store.classicmaze.StoreLineWithMoney

object ObjectPoolsDisposer {

    fun disposeAll() {
        Gdx.app.debug("com.parabolagames.glassmaze.store.ObjectPoolsDisposeListener", "disposing pools")
        StoreLineWithCandy.disposeObjectPools()
        StoreLineWithMoney.disposeObjectPools()
    }
}