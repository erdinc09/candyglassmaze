package com.parabolagames.glassmaze.core

import com.badlogic.gdx.ApplicationListener
import com.badlogic.gdx.pay.PurchaseManager
import com.parabolagames.glassmaze.shared.IAddsController
import com.parabolagames.glassmaze.shared.IPlatformUiFix

class GlassMazeMainProxy(platformFix: IPlatformUiFix,
                         private val addsController: IAddsController,
                         purchaseManager: PurchaseManager)
    : ApplicationListener, IAddsController {

    private val main: GlassMazeMain = GlassMazeMain(platformFix, this, purchaseManager)

    override fun create() = main.create();

    override fun resize(width: Int, height: Int) = main.resize(width, height)

    override fun render() = main.render()

    override fun pause() = main.pause()

    override fun resume() = main.resume()

    override fun dispose() = main.dispose()

    override fun showBanner(show: Boolean) {
        if (!main.isAdsRemoved()) {
            addsController.showBanner(show)
        }
    }

    override fun showInterstitial() {
        if (!main.isAdsRemoved()) {
            addsController.showInterstitial()
        }
    }

    override fun initAds() {
        if (!main.isAdsRemoved()) {
            addsController.initAds()
        }
    }
}