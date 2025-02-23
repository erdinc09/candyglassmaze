package com.parabolagames.glassmaze.candymode

import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Align
import com.parabolagames.glassmaze.framework.IScreenDrawer
import com.parabolagames.glassmaze.framework.labelStyle
import com.parabolagames.glassmaze.shared.Assets

internal class Ball2HolderActor constructor(
        x: Float,
        y: Float,
        assets: Assets,
        world: World,
        screenDrawer: IScreenDrawer,
        private val ironBallGiftListener: IIronBallGiftListener,
        private var availableBallNumber: Int) : BallHolderActor(
        x,
        y,
        assets,
        world,
        screenDrawer,
        Assets.IRON_BALL) {

    private val count: Label = Label("$availableBallNumber", labelStyle(assets.getBitmapFont(Assets.FONT_CURRENCY_MONTSERRAT)))
            .apply {
                setAlignment(Align.left)
                wrap = false
                setSize(1f, 0.2f)
                setFontScale(0.002f)
                setPosition(this@Ball2HolderActor.width - 0.1f, 0.03f)
            }.also {
                addActor(it)
            }

    override fun createActor(x: Float, y: Float, assets: Assets,world:World): ThrowableBallActor {
        if (--availableBallNumber == 0) {
            removeListener(
                    dragVectorDrawer)
            addAction(Actions.alpha(0.4f))
        }
        count.setText("$availableBallNumber")
        return ThrowableIronBallActor.createForGame(x, y, assets, stage, world,ironBallGiftListener)
    }
}