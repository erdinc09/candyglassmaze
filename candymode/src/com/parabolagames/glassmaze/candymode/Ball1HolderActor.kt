package com.parabolagames.glassmaze.candymode

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Align
import com.parabolagames.glassmaze.framework.IScreenDrawer
import com.parabolagames.glassmaze.framework.labelStyle
import com.parabolagames.glassmaze.shared.Assets
import com.parabolagames.glassmaze.shared.Constants
import com.parabolagames.glassmaze.shared.GenericGlassBallCrackedPieceActorData
import com.parabolagames.glassmaze.shared.SoundPlayer

internal class Ball1HolderActor(
        x: Float,
        y: Float,
        assets: Assets,
        world: World,
        screenDrawer: IScreenDrawer,
        private val soundPlayer: SoundPlayer) : BallHolderActor(
        x,
        y,
        assets,
        world,
        screenDrawer,
        GenericGlassBallCrackedPieceActorData.GLASS_3.glassAtlas) {

    private var data: GenericGlassBallCrackedPieceActorData = GenericGlassBallCrackedPieceActorData.GLASS_3

    init {
        Label(Constants.INFINITY_SYMBOL, labelStyle(assets.getBitmapFont(Assets.FONT_CURRENCY_MONTSERRAT)))
                .apply {
                    setAlignment(Align.left)
                    wrap = false
                    setSize(1f, 0.2f)
                    setFontScale(0.002f)
                    setPosition(this@Ball1HolderActor.width - 0.1f, +0.03f)
                }.also {
                    addActor(it)
                }
    }

    override fun createActor(x: Float, y: Float, assets: Assets, world: World): ThrowableBallActor {
        val newData: GenericGlassBallCrackedPieceActorData
        val dataProb = MathUtils.random()
        newData = when {
            dataProb < 0.25f -> GenericGlassBallCrackedPieceActorData.GLASS_2
            dataProb < 0.5f -> GenericGlassBallCrackedPieceActorData.GLASS_3
            dataProb < 0.75f -> GenericGlassBallCrackedPieceActorData.GLASS_4
            else -> GenericGlassBallCrackedPieceActorData.GLASS_5

        }
        loadTexture(assets.getTexture(newData.glassAtlas)) //TODO:CACHE
        setSize(WIDTH_HEIGHT, WIDTH_HEIGHT)
        val ball = ThrowableGlassBallActor.createForGame(x, y, assets, stage, soundPlayer, data, world)
        data = newData
        return ball
    }
}