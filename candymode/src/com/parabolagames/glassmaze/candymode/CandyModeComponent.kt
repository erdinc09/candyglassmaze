package com.parabolagames.glassmaze.candymode

import com.parabolagames.glassmaze.framework.BaseScreen
import com.parabolagames.glassmaze.framework.ForGame
import dagger.Subcomponent

@ForGame
@Subcomponent(modules = [CandyModeModule::class])
interface CandyModeComponent {
    val screen: BaseScreen

    @Subcomponent.Builder
    interface Builder {
        // Builder module(CandyModeModule module);
        fun build(): CandyModeComponent
    }
}