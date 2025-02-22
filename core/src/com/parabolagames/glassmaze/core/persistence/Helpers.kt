package com.parabolagames.glassmaze.core.persistence

import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences

internal const val PREFERENCE_FILE_PREFIX = "com.parabolagames.glassmaze."

internal fun <T> get(defaultValue: T, key: String, preferences: Preferences, block: String.() -> T): T {
    return try {
        val encrypted = preferences.getString(key, defaultValue.constructEncryptionWord())
        val decrypted = Encryption.decrypt(encrypted)
        if (!decrypted.startsWith(Encryption.SECRET_WORD) || !decrypted.endsWith(Encryption.SECRET_WORD)) {
            preferences.putString(key, defaultValue.constructEncryptionWord())
            return defaultValue
        }
        decrypted.removeSurrounding(Encryption.SECRET_WORD).block()
    } catch (e: Exception) {
        preferences.putString(key, defaultValue.constructEncryptionWord())
        defaultValue
    }
}

internal fun <T> set(value: T, key: String, preferences: Preferences) {
    val preferences = preferences.putString(key, Encryption.encrypt(Encryption.SECRET_WORD + value + Encryption.SECRET_WORD))
    if (Gdx.app.type == Application.ApplicationType.Android) {
        preferences.flush()
    }
}

private fun <T> T.constructEncryptionWord(): String = Encryption.encrypt(Encryption.SECRET_WORD + this + Encryption.SECRET_WORD)

