package com.parabolagames.glassmaze.store

import com.parabolagames.glassmaze.framework.ForApp
import javax.inject.Inject

@ForApp
class EagerComponents @Inject internal constructor(private val storeController: StoreController) {
}