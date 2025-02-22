package com.parabolagames.glassmaze.core.persistence

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.parabolagames.glassmaze.framework.ForApp
import com.parabolagames.glassmaze.menu.IMenuScreenPersistenceManager
import com.parabolagames.glassmaze.settings.ITermsOfServiceAndPrivacyPolicyPersistence
import com.parabolagames.glassmaze.shared.ISoundPersistenceManager
import com.parabolagames.glassmaze.store.IStorePersistenceManager
import javax.inject.Inject


@ForApp
internal class GeneralPersistence @Inject constructor()
    : ISoundPersistenceManager,
        ITermsOfServiceAndPrivacyPolicyPersistence,
        IMenuScreenPersistenceManager,
        IStorePersistenceManager {

    override var termsOfServiceAndPrivacyPolicyVersion: String
        set(value) = set(value, TERM_OF_SERVICE_PRIVACY_VERSION, generalPreferences)
        get() = get(ITermsOfServiceAndPrivacyPolicyPersistence.CURRENT_VERSION,
                TERM_OF_SERVICE_PRIVACY_VERSION, generalPreferences, String::toString)

    override var termsOfServiceAndPrivacyPolicyIsShown: Boolean
        set(value) = set(value, TERM_OF_SERVICE_PRIVACY_IS_SHOWN, generalPreferences)
        get() = get(false, TERM_OF_SERVICE_PRIVACY_IS_SHOWN, generalPreferences, String::toBoolean)

    override fun flushTermsOfServiceAndPrivacyPolicyPersistence() = generalPreferences.flush()

    override var isAdsRemoved: Boolean
        set(value) = set(value, ADS_REMOVED, generalPreferences)
        get() = get(false, ADS_REMOVED, generalPreferences, String::toBoolean)

    override var musicVolume: Float
        set(value) {
            set(value, MUSIC_VOLUME, generalPreferences)
        }
        get() = get(DEFAULT_MUSIC_VOLUME, MUSIC_VOLUME, generalPreferences, String::toFloat)

    override var soundVolume: Float
        set(value) = set(value, SOUND_VOLUME, generalPreferences)
        get() = get(DEFAULT_SOUND_VOLUME, SOUND_VOLUME, generalPreferences, String::toFloat)

    override var isSoundMuted: Boolean
        set(value) = set(value, SOUND_MUTED, generalPreferences)
        get() = get(false, SOUND_MUTED, generalPreferences, String::toBoolean)

    override var isMusicMuted: Boolean
        set(value) = set(value, MUSIC_MUTED, generalPreferences)
        get() = get(false, MUSIC_MUTED, generalPreferences, String::toBoolean)

    private val generalPreferences: Preferences = Gdx.app.getPreferences(PREFERENCE_FILE_PREFIX + "ab")

    override fun flushGeneralPreferences() = generalPreferences.flush()

    companion object {
        private const val DEFAULT_MUSIC_VOLUME = 0.02f
        private const val DEFAULT_SOUND_VOLUME = 0.01f
        private const val TERM_OF_SERVICE_PRIVACY_IS_SHOWN = "VVRFW6Iwf2"
        private const val TERM_OF_SERVICE_PRIVACY_VERSION = "FXJUSsvYJ2"
        private const val ADS_REMOVED = "gl17fxRwbe"
        private const val MUSIC_VOLUME = "ETmStJwgZw"
        private const val SOUND_VOLUME = "th35uMqHWQ"
        private const val SOUND_MUTED = "ignYgRru45"
        private const val MUSIC_MUTED = "1RPYuMkhOw"
    }
}