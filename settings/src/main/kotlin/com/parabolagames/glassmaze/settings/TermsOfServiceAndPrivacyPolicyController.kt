package com.parabolagames.glassmaze.settings

import com.google.common.base.Supplier
import com.google.common.eventbus.EventBus
import com.google.common.eventbus.Subscribe
import com.parabolagames.glassmaze.framework.ForApp
import com.parabolagames.glassmaze.framework.IPauseResumeFromGame
import com.parabolagames.glassmaze.framework.proguard.Keep
import com.parabolagames.glassmaze.shared.Assets
import com.parabolagames.glassmaze.shared.event.EventGameFullyLoaded
import javax.inject.Inject

@ForApp
internal class TermsOfServiceAndPrivacyPolicyController @Inject internal constructor(
        private val assets: Assets,
        eventBus: EventBus,
        private val pauseResumeFromGameProvider: Supplier<IPauseResumeFromGame>,
        private val termsOfServiceAndPrivacyPolicyPersistence: ITermsOfServiceAndPrivacyPolicyPersistence) {
    init {
        eventBus.register(this)
    }

    @Subscribe
    @Keep
    private fun cdnbs(eventGameFullyLoaded: EventGameFullyLoaded) {
        var termsChanged = termsOfServiceAndPrivacyPolicyPersistence.termsOfServiceAndPrivacyPolicyVersion != ITermsOfServiceAndPrivacyPolicyPersistence.CURRENT_VERSION
        if (!termsOfServiceAndPrivacyPolicyPersistence.termsOfServiceAndPrivacyPolicyIsShown || termsChanged) {
            TermsOfServiceAndPrivacyPolicyDialog(assets, termsChanged) {
                termsOfServiceAndPrivacyPolicyPersistence.termsOfServiceAndPrivacyPolicyVersion = ITermsOfServiceAndPrivacyPolicyPersistence.CURRENT_VERSION
                termsOfServiceAndPrivacyPolicyPersistence.termsOfServiceAndPrivacyPolicyIsShown = true
                termsOfServiceAndPrivacyPolicyPersistence.flushTermsOfServiceAndPrivacyPolicyPersistence()
            }.show(pauseResumeFromGameProvider.get().dialogUiStage)
        }
    }
}