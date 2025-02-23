package com.parabolagames.glassmaze.store

interface IStorePersistenceManager {
    var isAdsRemoved: Boolean
    fun flushGeneralPreferences()
}