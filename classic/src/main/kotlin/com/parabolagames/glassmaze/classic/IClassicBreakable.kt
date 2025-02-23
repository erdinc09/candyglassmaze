package com.parabolagames.glassmaze.classic

import com.parabolagames.glassmaze.shared.IBreakable

internal interface IClassicBreakable : IBreakable {
    fun markAndEnableForBreakable()
    fun unMarkAndDisableForBreakable()
}