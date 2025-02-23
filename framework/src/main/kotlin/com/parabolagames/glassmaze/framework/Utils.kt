@file:JvmName("Utils")

package com.parabolagames.glassmaze.framework

import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction
import com.badlogic.gdx.scenes.scene2d.ui.Label


fun run2(runnable: Runnable): RunnableAction = Actions.run(runnable)

fun labelStyle(font: BitmapFont): Label.LabelStyle = Label.LabelStyle(font, null)

fun Double.format(digits: Int) = "%.${digits}f".format(this)