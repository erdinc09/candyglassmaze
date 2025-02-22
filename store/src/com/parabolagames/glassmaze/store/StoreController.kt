package com.parabolagames.glassmaze.store

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.pay.*
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.Pools
import com.google.common.base.Supplier
import com.google.common.eventbus.EventBus
import com.google.common.eventbus.Subscribe
import com.parabolagames.glassmaze.framework.ForApp
import com.parabolagames.glassmaze.framework.IPauseResumeFromGame
import com.parabolagames.glassmaze.framework.proguard.Keep
import com.parabolagames.glassmaze.shared.Assets
import com.parabolagames.glassmaze.shared.Constants
import com.parabolagames.glassmaze.shared.event.*
import com.parabolagames.glassmaze.store.classicmaze.*
import javax.inject.Inject

@ForApp
internal class StoreController
@Inject constructor(private val eventBus: EventBus,
                    private val assets: Assets,
                    private val pauseResumeFromGameProvider: Supplier<IPauseResumeFromGame>,
                    private val purchaseManager: PurchaseManager,
                    private val persistenceGeneral: IStorePersistenceManager,
                    private val persistenceClassicMaze: IStorePersistenceManagerClassicMaze,
                    private val dialogController: DialogController,
                    private val purchaseWithCandyClassicMaze: IPurchaseWithCandyClassicMaze)
    : PurchaseObserver, IPurchase {

    private lateinit var informationMap: Map<String, Information>
    private var storeDialog: StoreDialog? = null

    init {
        eventBus.register(this)
    }


    @Subscribe
    @Keep
    private fun eosd(event: EventOpenStoreDialog) {
        if (!::informationMap.isInitialized) {
            dialogController.showErrorDialog()
        } else {
            storeDialog = StoreDialog(assets, addMazeLines(), eventBus)
                    .apply {
                        show(pauseResumeFromGameProvider.get().dialogUiStage)
                    }
        }
    }

    @Subscribe
    @Keep
    private fun ecsd(event: EventCloseStoreDialog) {
        storeDialog?.close()
        storeDialog = null
    }

    @Subscribe
    @Keep
    private fun earbp(event: EventAdsRemoveButtonPressed) {
        Gdx.app.debug(TAG, "EventAdsRemoveButtonPressed")
        if (!::informationMap.isInitialized) {
            dialogController.showErrorDialog()
        } else {
            try {
                Gdx.app.debug(TAG, "IAP_REMOVE_ADS will be purchased")
                dialogController.showWaitDialog()
                purchaseManager.purchase(IAP_REMOVE_ADS)
            } catch (e: Exception) {
                dialogController.closeWaitDialog()
                dialogController.showErrorDialog()
            }
        }
    }

    @Subscribe
    @Keep
    private fun egfll(event: EventGameFullyLoaded) = initPurchaseManager()

    @Subscribe
    @Keep
    private fun erbc(event: EventRestoreButtonClicked) {
        dialogController.showWaitDialog()
        purchaseManager.purchaseRestore()
    }

    override fun purchase(iap: String) {
        if (persistenceClassicMaze.classicCandyGlassHandNumberCount == Constants.INFINITY ||
                persistenceClassicMaze.classicSpinyGlassHandNumberCount == Constants.INFINITY) {
            dialogController.showInfoDialog("You have already infinite hands!")
        } else {
            try {
                dialogController.showWaitDialog()
                purchaseManager.purchase(iap)
            } catch (e: Exception) {
                dialogController.closeWaitDialog()
                dialogController.showErrorDialog()
            }
        }
    }

    override fun handlePurchase(transaction: Transaction) = Gdx.app.postRunnable {
        dialogController.closeWaitDialog()
        handlePurchaseImpl(transaction, false)
    }


    override fun handlePurchaseCanceled() = Gdx.app.postRunnable {
        dialogController.closeWaitDialog()
        dialogController.showInfoDialog("Purchase Cancelled")
    }

    override fun handlePurchaseError(e: Throwable) = Gdx.app.postRunnable {
        Gdx.app.debug(TAG, "handlePurchaseError(e: Throwable) e=$e")

        dialogController.closeWaitDialogIfOpen()

        if (e is ItemAlreadyOwnedException) {
            dialogController.showInfoDialog("""Already owned!
                |
                |Please try "Restore" ...""".trimMargin())
        } else {
            dialogController.showErrorDialog()
        }
    }

    override fun handleInstall() = Gdx.app.postRunnable {
        informationMap = linesWithMoney.map { it.key to purchaseManager.getInformation(it.key) }.toMap()
    }


    override fun handleInstallError(e: Throwable) = Gdx.app.postRunnable {
        Gdx.app.debug(TAG, "handleInstallError(e: Throwable) e=$e")
        dialogController.showErrorDialog()
    }

    override fun handleRestore(transactions: kotlin.Array<Transaction>) = Gdx.app.postRunnable {
        Gdx.app.debug(TAG, "handleRestore()")
        dialogController.closeWaitDialog()
        transactions.iterator().forEach { handlePurchaseImpl(it, true) }
        if (transactions.isEmpty()) {
            dialogController.showInfoDialog("Nothing to restore!")
        }
    }

    override fun handleRestoreError(e: Throwable) = Gdx.app.postRunnable {
        Gdx.app.debug(TAG, "handleRestoreError(e: Throwable) e=$e")
        dialogController.closeWaitDialog()
        dialogController.showErrorDialog()
    }

    private fun handlePurchaseImpl(transaction: Transaction, fromRestore: Boolean) {
        Gdx.app.debug(TAG, "${transaction.identifier} is purchased")
        when (transaction.identifier) {
            IAP_REMOVE_ADS -> {
                if (persistenceGeneral.isAdsRemoved) {
                    dialogController.showInfoDialog("Ads Already Removed !")
                } else {
                    dialogController.showInfoDialog("Ads Removed !")
                    persistenceGeneral.isAdsRemoved = true
                    persistenceGeneral.flushGeneralPreferences()
                    eventBus.post(EventAdsRemoved())
                }
            }
            else -> {
                val lineData: LineData = linesWithMoney[transaction.identifier]
                        ?: error("Invalid IAP ${transaction.identifier}")
                if (fromRestore && (persistenceClassicMaze.classicCandyGlassHandNumberCount == Constants.INFINITY ||
                                persistenceClassicMaze.classicSpinyGlassHandNumberCount == Constants.INFINITY)) {
                    dialogController.showInfoDialog("You have already infinite hands!")
                } else {

                    dialogController.showPurchaseResultDialog(if (fromRestore) "Purchased successfully!" else "Restored successfully!",
                            lineData.spinyCount, lineData.candyCount)

                    if (lineData.candyCount != Constants.INFINITY)
                        persistenceClassicMaze.classicCandyGlassHandNumberCount += lineData.candyCount
                    else
                        persistenceClassicMaze.classicCandyGlassHandNumberCount = Constants.INFINITY

                    if (lineData.spinyCount != Constants.INFINITY)
                        persistenceClassicMaze.classicSpinyGlassHandNumberCount += lineData.spinyCount
                    else
                        persistenceClassicMaze.classicSpinyGlassHandNumberCount = Constants.INFINITY

                    persistenceClassicMaze.saveClassicMaze()
                    eventBus.post(EventSpinyAndCandyHandPurchased())
                }
            }
        }
    }

    private fun initPurchaseManager() {
        val config = PurchaseManagerConfig()
        linesWithMoney.forEach { config.addOffer(Offer().setType(it.value.offerType).setIdentifier(it.key)) }
        config.addOffer(Offer().setType(OfferType.ENTITLEMENT).setIdentifier(IAP_REMOVE_ADS))
        purchaseManager.install(this, config, true)
    }


    private fun addMazeLines(): Array<AbstractStoreLine> = (Pools.get(Array::class.javaObjectType).obtain() as Array<AbstractStoreLine>)
            .apply {
                val linesWithMoneyValues = linesWithMoney.values.toList()
                add(StoreLineWithCandy.create(assets, 0, 1, 150, purchaseWithCandyClassicMaze))
                add(StoreLineWithCandy.create(assets, 1, 0, 150, purchaseWithCandyClassicMaze))
                add(linesWithMoneyValues[0].convertToStoreLine())
                add(linesWithMoneyValues[1].convertToStoreLine())
                add(StoreLineWithCandy.create(assets, 1, 1, 250, purchaseWithCandyClassicMaze))
                add(StoreLineWithCandy.create(assets, 0, 5, 500, purchaseWithCandyClassicMaze))
                add(StoreLineWithCandy.create(assets, 5, 0, 500, purchaseWithCandyClassicMaze))
                add(linesWithMoneyValues[2].convertToStoreLine())

                add(linesWithMoneyValues[3].convertToStoreLine())
                add(linesWithMoneyValues[4].convertToStoreLine())
                add(linesWithMoneyValues[5].convertToStoreLine())

                add(linesWithMoneyValues[6].convertToStoreLine())
                add(linesWithMoneyValues[7].convertToStoreLine())
                add(linesWithMoneyValues[8].convertToStoreLine())

                add(linesWithMoneyValues[9].convertToStoreLine())
                add(linesWithMoneyValues[10].convertToStoreLine())
                add(linesWithMoneyValues[11].convertToStoreLine())

                add(linesWithMoneyValues[12].convertToStoreLine())

                //add(StoreLineCurrencyTest.create(assets))
            }

    private fun LineData.convertToStoreLine() = StoreLineWithMoney.create(
            assets,
            this,
            informationMap[iapID] ?: error("$iapID not found in informationMap"),
            this@StoreController)

    companion object {
        const val TAG = "StoreEventController"
        const val IAP_REMOVE_ADS = "glass_maze_remove_ads"
        val linesWithMoney = mapOf(

                "glass_maze_candy_5" to LineData(0, 5, "glass_maze_candy_5"),
                "glass_maze_spiny_5" to LineData(5, 0, "glass_maze_spiny_5"),
                "glass_maze_spiny_5_candy_5" to LineData(5, 5, "glass_maze_spiny_5_candy_5"),

                "glass_maze_candy_10" to LineData(0, 10, "glass_maze_candy_10"),
                "glass_maze_spiny_10" to LineData(10, 0, "glass_maze_spiny_10"),
                "glass_maze_spiny_10_candy_10" to LineData(10, 10, "glass_maze_spiny_10_candy_10"),

                "glass_maze_candy_50" to LineData(0, 50, "glass_maze_candy_50"),
                "glass_maze_spiny_50" to LineData(50, 0, "glass_maze_spiny_50"),
                "glass_maze_spiny_50_candy_50" to LineData(50, 50, "glass_maze_spiny_50_candy_50"),

                "glass_maze_candy_100" to LineData(0, 100, "glass_maze_candy_100"),
                "glass_maze_spiny_100" to LineData(100, 0, "glass_maze_spiny_100"),
                "glass_maze_spiny_100_candy_100" to LineData(100, 100, "glass_maze_spiny_100_candy_100"),

                "glass_maze_infinite_hands" to LineData(Constants.INFINITY, Constants.INFINITY, "glass_maze_infinite_hands", OfferType.ENTITLEMENT)
        )
    }
}

