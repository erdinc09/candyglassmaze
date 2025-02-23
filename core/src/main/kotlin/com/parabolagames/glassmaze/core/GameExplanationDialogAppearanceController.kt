package com.parabolagames.glassmaze.core

import com.parabolagames.glassmaze.framework.ForApp
import com.parabolagames.glassmaze.shared.IGameExplanationDialogAppearanceController
import javax.inject.Inject

@ForApp
internal class GameExplanationDialogAppearanceController @Inject constructor() : IGameExplanationDialogAppearanceController {
    override var isItTimeToShowGameExplanationDialog: Boolean = true
}