package com.parabolagames.glassmaze.candymode

import com.badlogic.gdx.math.Vector2
import com.parabolagames.glassmaze.shared.ActorType

internal interface ICrackListener {
    fun crackedByMe(pos: Vector2, crackenType: ActorType)
}