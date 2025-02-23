package com.parabolagames.glassmaze.store.classicmaze

import com.google.common.eventbus.EventBus
import com.parabolagames.glassmaze.framework.ForApp
import com.parabolagames.glassmaze.shared.Constants
import com.parabolagames.glassmaze.shared.event.EventSpinyAndCandyHandPurchased
import com.parabolagames.glassmaze.store.DialogController
import javax.inject.Inject

@ForApp
internal class StoreControllerWithoutMoneyController @Inject constructor(private val dialogController: DialogController,
            private val dataPersistenceManager: IStorePersistenceManagerClassicMaze,
            private val eventBus: EventBus)
    : IPurchaseWithCandyClassicMaze {

    override fun purchase(spinyCount: Int, candyCount: Int, candyCountForMoney: Int) {
        if (dataPersistenceManager.classicCandyGlassHandNumberCount == Constants.INFINITY ||
                dataPersistenceManager.classicSpinyGlassHandNumberCount == Constants.INFINITY) {
            dialogController.showInfoDialog("You have already infinite hands!")
        } else {
            if (dataPersistenceManager.totalScore >= candyCountForMoney) {
                dialogController.showCandyExchangeConfirmationDialog(spinyCount,candyCount,candyCountForMoney){
                    dataPersistenceManager.totalScore -= candyCountForMoney
                    dataPersistenceManager.classicCandyGlassHandNumberCount += candyCount
                    dataPersistenceManager.classicSpinyGlassHandNumberCount += spinyCount
                    dataPersistenceManager.saveClassicMaze()
                    eventBus.post(EventSpinyAndCandyHandPurchased())
                    dialogController.showPurchaseResultDialog("For $candyCountForMoney candies you get:", spinyCount, candyCount)
                }
            } else {
                dialogController.showInfoDialog("""Not enough candy!
                |
                |You need ${candyCountForMoney - dataPersistenceManager.totalScore} more candies...
            """.trimMargin())
            }
        }
    }
}