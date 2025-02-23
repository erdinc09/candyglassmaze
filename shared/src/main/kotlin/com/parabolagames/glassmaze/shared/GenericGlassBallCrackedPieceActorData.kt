package com.parabolagames.glassmaze.shared

data class GenericGlassBallCrackedPieceActorData private constructor(
        val piece1Atlas: String,
        val piece2Atlas: String,
        val piece1_1Atlas: String,
        val piece1_2Atlas: String,
        val piece2_1Atlas: String,
        val piece2_2Atlas: String,
        val crackDelayTime: Float,
        val crackFadeOutTime: Float,
        val crack1Atlas: String,
        val glassAtlas: String) {

    companion object {
        @JvmField
        val GLASS_2 = GenericGlassBallCrackedPieceActorData(
                glassAtlas = Assets.GLASS2,
                piece1_1Atlas = Assets.GLASS2_PIECE_1_1_ATLAS,
                piece1_2Atlas = Assets.GLASS2_PIECE_1_2_ATLAS,
                piece1Atlas = Assets.GLASS2_PIECE_1_ATLAS,
                piece2_1Atlas = Assets.GLASS2_PIECE_2_1_ATLAS,
                piece2_2Atlas = Assets.GLASS2_PIECE_2_2_ATLAS,
                piece2Atlas = Assets.GLASS2_PIECE_2_ATLAS,
                crack1Atlas = Assets.GLASS2_CRACK1_ATLAS,
                crackDelayTime = 0.5f,
                crackFadeOutTime = 0.25f)

        @JvmField
        val GLASS_3 = GenericGlassBallCrackedPieceActorData(
                glassAtlas = Assets.GLASS3,
                piece1_1Atlas = Assets.GLASS3_PIECE_1_1_ATLAS,
                piece1_2Atlas = Assets.GLASS3_PIECE_1_2_ATLAS,
                piece1Atlas = Assets.GLASS3_PIECE_1_ATLAS,
                piece2_1Atlas = Assets.GLASS3_PIECE_2_1_ATLAS,
                piece2_2Atlas = Assets.GLASS3_PIECE_2_2_ATLAS,
                piece2Atlas = Assets.GLASS3_PIECE_2_ATLAS,
                crack1Atlas = Assets.GLASS3_CRACK1_ATLAS,
                crackDelayTime = 0.5f,
                crackFadeOutTime = 0.25f)

        @JvmField
        val GLASS_4 = GenericGlassBallCrackedPieceActorData(
                glassAtlas = Assets.GLASS4,
                piece1_1Atlas = Assets.GLASS4_PIECE_1_1_ATLAS,
                piece1_2Atlas = Assets.GLASS4_PIECE_1_2_ATLAS,
                piece1Atlas = Assets.GLASS4_PIECE_1_ATLAS,
                piece2_1Atlas = Assets.GLASS4_PIECE_2_1_ATLAS,
                piece2_2Atlas = Assets.GLASS4_PIECE_2_2_ATLAS,
                piece2Atlas = Assets.GLASS4_PIECE_2_ATLAS,
                crack1Atlas = Assets.GLASS4_CRACK1_ATLAS,
                crackDelayTime = 0.5f,
                crackFadeOutTime = 0.25f)

        @JvmField
        val GLASS_5 = GenericGlassBallCrackedPieceActorData(
                glassAtlas = Assets.GLASS5,
                piece1_1Atlas = Assets.GLASS5_PIECE_1_1_ATLAS,
                piece1_2Atlas = Assets.GLASS5_PIECE_1_2_ATLAS,
                piece1Atlas = Assets.GLASS5_PIECE_1_ATLAS,
                piece2_1Atlas = Assets.GLASS5_PIECE_2_1_ATLAS,
                piece2_2Atlas = Assets.GLASS5_PIECE_2_2_ATLAS,
                piece2Atlas = Assets.GLASS5_PIECE_2_ATLAS,
                crack1Atlas = Assets.GLASS5_CRACK1_ATLAS,
                crackDelayTime = 0.5f,
                crackFadeOutTime = 0.25f)

        @JvmField
        val SPINY_GLASS = GenericGlassBallCrackedPieceActorData(
                glassAtlas = Assets.SPINY_GLASS,
                piece1_1Atlas = Assets.SPINY_GLASS_PIECE_1_1_ATLAS,
                piece1_2Atlas = Assets.SPINY_GLASS_PIECE_1_2_ATLAS,
                piece1Atlas = Assets.SPINY_GLASS_PIECE_1_ATLAS,
                piece2_1Atlas = Assets.SPINY_GLASS_PIECE_2_1_ATLAS,
                piece2_2Atlas = Assets.SPINY_GLASS_PIECE_2_2_ATLAS,
                piece2Atlas = Assets.SPINY_GLASS_PIECE_2_ATLAS,
                crack1Atlas = Assets.GLASS5_CRACK1_ATLAS,
                crackDelayTime = 0.5f,
                crackFadeOutTime = 0.25f)
    }
}
