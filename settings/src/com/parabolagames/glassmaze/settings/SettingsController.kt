package com.parabolagames.glassmaze.settings

import com.google.common.base.Supplier
import com.google.common.eventbus.EventBus
import com.google.common.eventbus.Subscribe
import com.parabolagames.glassmaze.framework.ForApp
import com.parabolagames.glassmaze.framework.IPauseResumeFromGame
import com.parabolagames.glassmaze.framework.proguard.Keep
import com.parabolagames.glassmaze.shared.Assets
import com.parabolagames.glassmaze.shared.IAddsController
import com.parabolagames.glassmaze.shared.ISoundControl
import com.parabolagames.glassmaze.shared.event.EventOpenSettingsDialog
import javax.inject.Inject

@ForApp
internal class SettingsController @Inject internal constructor(
        private val assets: Assets,
        private val soundControl: ISoundControl,
        private val addsController: IAddsController,
        private val eventBus: EventBus,
        private val pauseResumeFromGameProvider: Supplier<IPauseResumeFromGame>) {
    init {
        eventBus.register(this)
    }

    @Subscribe
    @Keep
    private fun eosdl(event: EventOpenSettingsDialog) {
        SettingsDialog(assets, soundControl, addsController, eventBus).show(pauseResumeFromGameProvider.get().dialogUiStage)
    }
}