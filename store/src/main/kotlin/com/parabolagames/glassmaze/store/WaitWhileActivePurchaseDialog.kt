package com.parabolagames.glassmaze.store

import com.badlogic.gdx.scenes.scene2d.Action
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Dialog
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.Pools
import com.parabolagames.glassmaze.shared.Assets
import com.parabolagames.glassmaze.shared.Constants
import com.parabolagames.glassmaze.shared.candy2.CandyActor
import com.parabolagames.glassmaze.shared.ui.DialogStyle
import com.parabolagames.glassmaze.shared.ui.GM_Label
import kotlin.math.roundToInt

internal class WaitWhileActivePurchaseDialog
@JvmOverloads constructor(private val assets: Assets)
    : Dialog("Store", DialogStyle(assets)) {

    private val candyArray: Array<CandyActor> = Pools.get(Array::class.java).obtain() as Array<CandyActor>

    init {
        isModal = false
        initialize()
    }

    override fun show(stage: Stage): Dialog {
        super.show(stage, null)
        setPosition(
                ((stage.width - width) / 2).roundToInt().toFloat(),
                ((stage.height - height) / 2).roundToInt().toFloat())
        return this
    }

    override fun hide(action: Action?) {
        candyArray.forEach { it.remove() }
        candyArray.clear()
        Pools.get(Array::class.java).free(candyArray)
        super.hide(action)
    }

    private fun initialize() {
        titleLabel.setFontScale(4f)
        titleTable.getCell(titleLabel).padTop(-0.1f * Constants.DIALOG_SIZE_RATIO)

        GM_Label("Please wait ...", assets, Assets.FONT_COSMIC_SANS_ORANGE, 2.7f)
                .also {
                    contentTable.add(it).pad(0.15f * Constants.DIALOG_SIZE_RATIO)
                }

        CandyActor.getCandy(assets, Assets.CANDY17_ATLAS,
                0.25f * Constants.DIALOG_SIZE_RATIO,
                true) { 0.025f }
                .apply {
                    isAnimationPaused = false
                }
                .also {
                    contentTable.add(it).padRight(0.1f * Constants.DIALOG_SIZE_RATIO)
                    candyArray.add(it)
                }
    }
}