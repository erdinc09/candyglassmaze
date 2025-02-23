package com.parabolagames.glassmaze.shared

data class ActorTypeData (val type: ActorType, val data: Any?) {
    constructor(type: ActorType) : this(type, null)
}