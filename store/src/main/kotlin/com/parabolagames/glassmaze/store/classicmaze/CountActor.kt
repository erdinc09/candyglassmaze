package com.parabolagames.glassmaze.store.classicmaze

import com.badlogic.gdx.scenes.scene2d.actions.Actions.*
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.google.common.base.Preconditions.checkState
import com.parabolagames.glassmaze.framework.labelStyle
import com.parabolagames.glassmaze.shared.Assets
import com.parabolagames.glassmaze.shared.Constants
import com.parabolagames.glassmaze.shared.ui.GM_Container

internal class CountActor(assets: Assets) : Label("0", labelStyle(assets.getBitmapFont(Assets.FONT_CURRENCY_MONTSERRAT))) {

    init {
        wrap = false
        this.setFontScale(2f)
    }

    fun setCount(handCount: Int) {
        setText(if (handCount == Constants.INFINITY) Constants.INFINITY_SYMBOL else "$handCount")

        checkState(parent is GM_Container<*>)
        parent.clearActions()
        if (handCount == Constants.INFINITY) {
            parent.addAction(
                    forever(sequence(
                            delay(1f),
                            scaleBy(0.4f, 0.4f, 0.25f),
                            scaleBy(-0.4f, -0.4f, 0.25f),
                            scaleBy(0.4f, 0.4f, 0.25f),
                            scaleBy(-0.4f, -0.4f, 0.25f),
                    )))
        }
    }

    override fun sizeChanged() {
        checkState(parent == null || parent is GM_Container<*>)
        parent?.width = width
        parent?.height = height
    }
}