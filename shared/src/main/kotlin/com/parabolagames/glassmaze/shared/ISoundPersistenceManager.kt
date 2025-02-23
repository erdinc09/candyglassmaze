package com.parabolagames.glassmaze.shared

interface ISoundPersistenceManager {
    var musicVolume: Float
    var soundVolume: Float
    var isSoundMuted: Boolean
    var isMusicMuted: Boolean
    fun flushGeneralPreferences()
}