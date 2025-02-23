package com.parabolagames.glassmaze.store

import com.parabolagames.glassmaze.store.classicmaze.IPurchaseWithCandyClassicMaze
import com.parabolagames.glassmaze.store.classicmaze.StoreControllerWithoutMoneyController
import dagger.Binds
import dagger.Module

@Module
abstract class StoreModule {
    @Binds
    internal abstract fun bindIPurchaseWithCandyClassicMaze(storeControllerWithoutMoneyController: StoreControllerWithoutMoneyController): IPurchaseWithCandyClassicMaze
}