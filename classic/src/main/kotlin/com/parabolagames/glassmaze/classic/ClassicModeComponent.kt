package com.parabolagames.glassmaze.classic

import com.parabolagames.glassmaze.framework.BaseScreen
import com.parabolagames.glassmaze.framework.ForGame
import dagger.Subcomponent

@ForGame
//@Subcomponent(modules = [ClassicModeModule::class])
@Subcomponent(modules = [ClassicModeModule2::class, ClassicModeModule3::class])
interface ClassicModeComponent {
    val screen: BaseScreen

    @Subcomponent.Builder
    interface Builder {
        //        fun classicModeModule(module: ClassicModeModule): Builder
        fun classicModeModule(module: ClassicModeModule3): Builder
        fun build(): ClassicModeComponent
    }
}