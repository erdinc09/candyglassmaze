package com.parabolagames.glassmaze.settings

interface ITermsOfServiceAndPrivacyPolicyPersistence {

    var termsOfServiceAndPrivacyPolicyVersion: String
    var termsOfServiceAndPrivacyPolicyIsShown: Boolean
    fun flushTermsOfServiceAndPrivacyPolicyPersistence()
   
    companion object {
        const val CURRENT_VERSION = "v1"
    }
}