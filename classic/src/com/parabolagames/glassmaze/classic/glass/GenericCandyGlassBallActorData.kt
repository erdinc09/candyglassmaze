package com.parabolagames.glassmaze.classic.glass

import com.google.common.base.Supplier
import com.parabolagames.glassmaze.classic.GridActor

internal class GenericCandyGlassBallActorData {
    @JvmField
    val candyAtlas: String

    @JvmField
    val size: Float

    @JvmField
    val candyInGlassSizeRatio: Float

    @JvmField
    val frameDurationSupplier: Supplier<Float>

    @JvmField
    val isAnimatedInGlass: Boolean

    /**
     * @param candyAtlas the atlas file name of the candy (must come from the [Assets]
     * CANDYX_ATLAS constants)
     * @param size the candy size in meters
     * @param candyInGlassSizeRatio the ratio of the candy size in glass, the higher ratio the smaller
     * the candy in glass
     * @param force [RandomForceInterval] that applied to the candy, when released from the
     * glass
     * @param frameDuration the duration which each frame is drawn, the higher duration, the slower
     * the animation
     */
    constructor(
            candyAtlas: String,
            size: Float,
            candyInGlassSizeRatio: Float,
            frameDurationSupplier: Supplier<Float>,
            isAnimatedInGlass: Boolean) : super() {
        this.candyAtlas = candyAtlas
        this.size = size
        this.candyInGlassSizeRatio = candyInGlassSizeRatio
        this.frameDurationSupplier = frameDurationSupplier
        this.isAnimatedInGlass = isAnimatedInGlass
    }

    /**
     * @param candyAtlas the atlas file name of the candy (must come from the [Assets]
     * CANDYX_ATLAS constants)
     * @param frameDuration the duration which each frame is drawn, the higher duration, the slower
     * the animation
     */
    constructor(
            candyAtlas: String, frameDurationSupplier: Supplier<Float>, isAnimatedInGlass: Boolean) : super() {
        this.candyAtlas = candyAtlas
        size = GridActor.CELL_SIZE / 1.75f
        candyInGlassSizeRatio = 2f
        this.frameDurationSupplier = frameDurationSupplier
        this.isAnimatedInGlass = isAnimatedInGlass
    }
}