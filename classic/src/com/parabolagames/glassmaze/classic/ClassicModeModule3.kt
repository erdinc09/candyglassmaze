package com.parabolagames.glassmaze.classic

import com.google.common.base.Optional
import com.google.common.base.Preconditions.checkArgument
import com.parabolagames.glassmaze.framework.ForGame
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class ClassicModeModule3 {
    private val levelNumber: Optional<Int>
    private val subMode: ClassicModeSubMode

    /**
     * Creates a module for given non null and >0 level number.
     *
     * @param levelNumber the level number
     * @param subMode the sub mode
     */
    internal constructor(levelNumber: Int, subMode: ClassicModeSubMode) {
        checkArgument(levelNumber > 0)
        this.levelNumber = Optional.of(levelNumber)
        this.subMode = subMode
    }

    /**
     * Creates a module for absent level number. This module enable the game such that: <br></br>
     *
     *
     *  * starts next random level in RANDOM mode
     *  * starts level from the DataPersistenceManager in CLASSIC mode
     *
     * @param subMode the sub mode
     */
    internal constructor(subMode: ClassicModeSubMode) {
        levelNumber = Optional.absent()
        this.subMode = subMode
    }

    @ForGame
    @Named(CLASSIC_LEVEL_NUMBER)
    @Provides
    internal fun provideLevelNumber(): Optional<Int> = levelNumber

    @ForGame
    @Provides
    internal fun provideSubMode(): ClassicModeSubMode = subMode


    companion object {
        /**
         * This is not real level number, for level number use [ILevelNumberProvider] instead. This
         * may return null which means : <br></br>
         *
         *
         *  * start next random level in RANDOM mode
         *  * start level from the DataPersistenceManager in CLASSIC mode
         */
        const val CLASSIC_LEVEL_NUMBER = "classicModeLevelNumber"
    }
}