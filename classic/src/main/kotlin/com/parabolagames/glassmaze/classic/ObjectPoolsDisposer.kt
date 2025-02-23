    package com.parabolagames.glassmaze.classic

import com.badlogic.gdx.Gdx
import com.parabolagames.glassmaze.classic.glass.GenericCandyGlassFactory
import com.parabolagames.glassmaze.classic.glass.GenericGlassBallActor
import com.parabolagames.glassmaze.classic.glass.SpinyBallActor
import com.parabolagames.glassmaze.shared.candy.CandyActorBox2D
import com.parabolagames.glassmaze.shared.candy2.CandyActor
import com.parabolagames.glassmaze.shared.crack.GenericGlassCrackedPieceActor

object ObjectPoolsDisposer {
    fun disposeAll() {
        Gdx.app.debug("com.parabolagames.glassmaze.ObjectPoolsDisposeListener", "disposing pools")
        GridActor.disposeObjectPools()
        SpinyBallActor.disposeObjectPools()
        GenericGlassBallActor.disposeObjectPools()
        GenericCandyGlassFactory.disposeObjectPools()
        MazeBallActor.disposeObjectPools()
        CubeActor.disposeObjectPools()
        GenericGlassCrackedPieceActor.disposeObjectPools()
        CandyActorBox2D.disposeObjectPools()
        CandyActor.disposeObjectPools()
        Level.disposeObjectPools()
        GridData.disposeObjectPools()
        BonusCandyHandActor.disposeObjectPools()
        BonusSpinyHandActor.disposeObjectPools()
    }
}