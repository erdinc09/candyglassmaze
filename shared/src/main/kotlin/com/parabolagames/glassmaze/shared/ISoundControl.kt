package com.parabolagames.glassmaze.shared

interface ISoundControl {
    var isSoundMuted: Boolean
    var isMusicMuted: Boolean
    var musicVolume: Float
    var soundVolume: Float
    fun saveVolumes()
}