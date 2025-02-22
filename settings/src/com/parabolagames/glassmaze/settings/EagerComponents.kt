package com.parabolagames.glassmaze.settings

import com.parabolagames.glassmaze.framework.ForApp
import javax.inject.Inject

@ForApp
class EagerComponents @Inject
internal constructor(private val settingsController: SettingsController,
                     private val aboutController: AboutController,
                     private val termsOfServiceAndPrivacyPolicyController: TermsOfServiceAndPrivacyPolicyController) {
}