package com.parabolagames.glassmaze.store

import com.badlogic.gdx.pay.Information
import com.badlogic.gdx.pay.PurchaseManager
import com.badlogic.gdx.pay.PurchaseManagerConfig
import com.badlogic.gdx.pay.Transaction


class TestPurchaseManager : PurchaseManager {

    private lateinit var observer: com.badlogic.gdx.pay.PurchaseObserver

    override fun storeName(): String {
        return "TestPurchaseManager"
    }

    override fun install(observer: com.badlogic.gdx.pay.PurchaseObserver, config: PurchaseManagerConfig, autoFetchInformation: Boolean) {
        this.observer = observer
        observer.handleInstall()
    }

    override fun installed(): Boolean {
        return ::observer.isInitialized
    }

    override fun dispose() {}


    override fun purchase(identifier: String) {
        val transaction = Transaction()
        transaction.identifier = identifier
        observer.handlePurchase(transaction)
    }

    override fun purchaseRestore() {}

    override fun getInformation(identifier: String): Information {
        return Information.UNAVAILABLE
    }
}