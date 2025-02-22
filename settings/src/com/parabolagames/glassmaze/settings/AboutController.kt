package com.parabolagames.glassmaze.settings

import com.google.common.base.Supplier
import com.google.common.eventbus.EventBus
import com.google.common.eventbus.Subscribe
import com.parabolagames.glassmaze.framework.ForApp
import com.parabolagames.glassmaze.framework.IPauseResumeFromGame
import com.parabolagames.glassmaze.framework.proguard.Keep
import com.parabolagames.glassmaze.shared.Assets
import javax.inject.Inject

@ForApp
internal class AboutController @Inject internal constructor(
        private val assets: Assets,
        eventBus: EventBus,
        private val pauseResumeFromGameProvider: Supplier<IPauseResumeFromGame>) {
    init {
        eventBus.register(this)
    }

    @Subscribe
    @Keep
    private fun eoadl(eventOpen: EventOpenAboutDialog) {
        AboutDialog(assets).show(pauseResumeFromGameProvider.get().dialogUiStage)
    }
}