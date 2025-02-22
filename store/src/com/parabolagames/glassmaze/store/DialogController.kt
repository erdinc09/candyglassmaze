package com.parabolagames.glassmaze.store

import com.google.common.base.Preconditions
import com.google.common.base.Supplier
import com.google.common.eventbus.EventBus
import com.parabolagames.glassmaze.framework.CurrentScreen
import com.parabolagames.glassmaze.framework.ICurrentScreenProvider
import com.parabolagames.glassmaze.framework.IPauseResumeFromGame
import com.parabolagames.glassmaze.shared.Assets
import com.parabolagames.glassmaze.shared.InformationDialogsWithTitle
import com.parabolagames.glassmaze.store.classicmaze.CandyHandAndSpinyHandExchangeApprovalDialog
import com.parabolagames.glassmaze.store.classicmaze.CandyHandAndSpinyHandPurchaseResultDialog
import javax.inject.Inject

internal class DialogController @Inject constructor(private val assets: Assets,
                                                    private val pauseResumeFromGameProvider: Supplier<IPauseResumeFromGame>,
                                                    private val currentScreenProvider: ICurrentScreenProvider,
                                                    private val eventBus: EventBus) {

    private var waitDialog: WaitWhileActivePurchaseDialog? = null

    fun showInfoDialog(content: String) {
        InformationDialogsWithTitle(assets, "Store", content) {
        }.show(pauseResumeFromGameProvider.get().dialogUiStageForInformation)
    }

    fun showErrorDialog() {
        InformationDialogsWithTitle(assets,
                "Store Error",
                "Please try again later...") {
        }.show(pauseResumeFromGameProvider.get().dialogUiStageForInformation)
    }

    fun showWaitDialog() {
        Preconditions.checkState(waitDialog == null)
        waitDialog = WaitWhileActivePurchaseDialog(assets)
                .also {
                    it.show(pauseResumeFromGameProvider.get().dialogUiStageForInformation)
                }
    }

    fun closeWaitDialog() {
        waitDialog!!.hide()
        waitDialog = null
    }

    fun closeWaitDialogIfOpen() {
        waitDialog?.hide()
        waitDialog = null
    }

    fun showPurchaseResultDialog(content: String, spinyCount: Int, candyCount: Int) {
        CandyHandAndSpinyHandPurchaseResultDialog(assets,
                "Store",
                content,
                spinyCount,
                candyCount
        ) { if (currentScreenProvider.currentScreen != CurrentScreen.MENU) eventBus.post(EventCloseStoreDialog()) }
                .show(pauseResumeFromGameProvider.get().dialogUiStageForInformation)

    }

    fun showCandyExchangeConfirmationDialog(spinyHandCount: Int, candyHandCount: Int, candyCountForMoney: Int, acceptFunction: () -> Unit) {
        CandyHandAndSpinyHandExchangeApprovalDialog(assets,
                spinyHandCount,
                candyHandCount,
                candyCountForMoney,
                acceptFunction).show(pauseResumeFromGameProvider.get().dialogUiStageForInformation)
    }
}