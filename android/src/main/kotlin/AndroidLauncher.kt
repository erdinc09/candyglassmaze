package com.parabolagames.glassmaze

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Window
import android.widget.RelativeLayout
import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import com.parabolagames.glassmaze.core.GlassMazeMainProxy
import com.parabolagames.glassmaze.shared.IAddsController
import com.parabolagames.glassmaze.shared.IPlatformUiFix
import com.parabolagames.glassmaze.store.TestPurchaseManager

class AndroidLauncher @SuppressLint("HandlerLeak") constructor() : AndroidApplication() {
    private lateinit var layout: RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        val glassMazeMainProxy = GlassMazeMainProxy(
            object : IPlatformUiFix {
                override val isUiAlignmentFromTopIsNecessary: Boolean
                    get() = false
                override val isThrowingBallPadInCandyModeNecessary: Boolean
                    get() = false
            },
            object : IAddsController {
                override fun showBanner(show: Boolean) {}
                override fun showInterstitial() {}
                override fun initAds() {}
            },
            TestPurchaseManager()
        )
        val gameView = initializeForView(
            glassMazeMainProxy,
            AndroidApplicationConfiguration()
        )

        layout = RelativeLayout(this)
            .apply {
                addView(gameView)
            }.also { setContentView(it) }
    }
}