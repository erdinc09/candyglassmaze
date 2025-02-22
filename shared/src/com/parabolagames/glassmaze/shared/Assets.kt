package com.parabolagames.glassmaze.shared

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetErrorListener
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.Texture.TextureFilter
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.Disposable
import com.parabolagames.glassmaze.framework.ForApp
import java.util.*
import javax.inject.Inject

@ForApp
class Assets @Inject constructor() : Disposable, AssetErrorListener {
    private val breakSounds: MutableList<String> = ArrayList()
    private val assetManager: AssetManager = AssetManager()
    fun update(): Boolean = assetManager.update()

    val progress: Float
        get() = assetManager.progress

    @Inject
    fun init() {
        queueAtlases()
        queueSingleTextures()
        queueSounds()
        queueFonts()
    }

    private fun queueFonts() = with(assetManager) {
        load(FONT_COSMIC_SANS_GREEN, BitmapFont::class.java)
        load(FONT_COSMIC_SANS_ORANGE, BitmapFont::class.java)
        load(FONT_COSMIC_SANS_RED, BitmapFont::class.java)
        load(FONT_COSMIC_SANS_ORANGE_BOLD, BitmapFont::class.java)
        load(FONT_COURIER_ORANGE, BitmapFont::class.java)
        load(FONT_COMIC_SANS_MS, BitmapFont::class.java)
        load(FONT_CURRENCY_MONTSERRAT, BitmapFont::class.java)
    }

    private fun String.toPathFromInternalFile(): String = Gdx.files.internal(this).path()

    private fun queueSounds() {
        loadBreakSound(BREAK_SOUND_1)
        loadBreakSound(BREAK_SOUND_2)
        loadBreakSound(BREAK_SOUND_3)
        with(assetManager) {
            load(BREAK_SOUND_8.toPathFromInternalFile(), Sound::class.java)
            load(GLASS_APPEAR_SOUND.toPathFromInternalFile(), Sound::class.java)
            load(SPINY_BALL_APPEAR_SOUND.toPathFromInternalFile(), Sound::class.java)
            load(NO_HAND_SOUND.toPathFromInternalFile(), Sound::class.java)
            load(GLASS_MISSED_SOUND.toPathFromInternalFile(), Sound::class.java)
            load(GAME_BACK_GROUUND_SOUND_1.toPathFromInternalFile(), Sound::class.java)
            load(MENU_MUSIC.toPathFromInternalFile(), Music::class.java)
        }
    }

    private fun queueAtlases() = with(assetManager) {
        load(CANDY1_ATLAS.toPathFromInternalFile(), TextureAtlas::class.java)
        load(CANDY2_ATLAS.toPathFromInternalFile(), TextureAtlas::class.java)
        load(CANDY3_ATLAS.toPathFromInternalFile(), TextureAtlas::class.java)
        load(CANDY4_ATLAS.toPathFromInternalFile(), TextureAtlas::class.java)
        load(CANDY5_ATLAS.toPathFromInternalFile(), TextureAtlas::class.java)
        load(CANDY6_ATLAS.toPathFromInternalFile(), TextureAtlas::class.java)
        load(CANDY7_ATLAS.toPathFromInternalFile(), TextureAtlas::class.java)
        load(CANDY8_ATLAS.toPathFromInternalFile(), TextureAtlas::class.java)
        load(CANDY9_ATLAS.toPathFromInternalFile(), TextureAtlas::class.java)
        load(CANDY10_ATLAS.toPathFromInternalFile(), TextureAtlas::class.java)
        load(CANDY11_ATLAS.toPathFromInternalFile(), TextureAtlas::class.java)
        load(CANDY12_ATLAS.toPathFromInternalFile(), TextureAtlas::class.java)
        load(CANDY13_ATLAS.toPathFromInternalFile(), TextureAtlas::class.java)
        load(CANDY14_ATLAS.toPathFromInternalFile(), TextureAtlas::class.java)
        load(CANDY15_ATLAS.toPathFromInternalFile(), TextureAtlas::class.java)
        load(CANDY16_ATLAS.toPathFromInternalFile(), TextureAtlas::class.java)
        load(CANDY17_ATLAS.toPathFromInternalFile(), TextureAtlas::class.java)
        load(GLASS2_PIECE_1_ATLAS.toPathFromInternalFile(), TextureAtlas::class.java)
        load(GLASS2_PIECE_2_ATLAS.toPathFromInternalFile(), TextureAtlas::class.java)
        load(GLASS2_PIECE_1_1_ATLAS.toPathFromInternalFile(), TextureAtlas::class.java)
        load(GLASS2_PIECE_1_2_ATLAS.toPathFromInternalFile(), TextureAtlas::class.java)
        load(GLASS2_PIECE_2_1_ATLAS.toPathFromInternalFile(), TextureAtlas::class.java)
        load(GLASS2_PIECE_2_2_ATLAS.toPathFromInternalFile(), TextureAtlas::class.java)
        load(GLASS2.toPathFromInternalFile(), Texture::class.java)
        load(GLASS3_PIECE_1_ATLAS.toPathFromInternalFile(), TextureAtlas::class.java)
        load(GLASS3_PIECE_2_ATLAS.toPathFromInternalFile(), TextureAtlas::class.java)
        load(GLASS3_PIECE_1_1_ATLAS.toPathFromInternalFile(), TextureAtlas::class.java)
        load(GLASS3_PIECE_1_2_ATLAS.toPathFromInternalFile(), TextureAtlas::class.java)
        load(GLASS3_PIECE_2_1_ATLAS.toPathFromInternalFile(), TextureAtlas::class.java)
        load(GLASS3_PIECE_2_2_ATLAS.toPathFromInternalFile(), TextureAtlas::class.java)
        load(GLASS3.toPathFromInternalFile(), Texture::class.java)
        load(GLASS4_PIECE_1_ATLAS.toPathFromInternalFile(), TextureAtlas::class.java)
        load(GLASS4_PIECE_2_ATLAS.toPathFromInternalFile(), TextureAtlas::class.java)
        load(GLASS4_PIECE_1_1_ATLAS.toPathFromInternalFile(), TextureAtlas::class.java)
        load(GLASS4_PIECE_1_2_ATLAS.toPathFromInternalFile(), TextureAtlas::class.java)
        load(GLASS4_PIECE_2_1_ATLAS.toPathFromInternalFile(), TextureAtlas::class.java)
        load(GLASS4_PIECE_2_2_ATLAS.toPathFromInternalFile(), TextureAtlas::class.java)
        load(GLASS4.toPathFromInternalFile(), Texture::class.java)
        load(GLASS5_PIECE_1_ATLAS.toPathFromInternalFile(), TextureAtlas::class.java)
        load(GLASS5_PIECE_2_ATLAS.toPathFromInternalFile(), TextureAtlas::class.java)
        load(GLASS5_PIECE_1_1_ATLAS.toPathFromInternalFile(), TextureAtlas::class.java)
        load(GLASS5_PIECE_1_2_ATLAS.toPathFromInternalFile(), TextureAtlas::class.java)
        load(GLASS5_PIECE_2_1_ATLAS.toPathFromInternalFile(), TextureAtlas::class.java)
        load(GLASS5_PIECE_2_2_ATLAS.toPathFromInternalFile(), TextureAtlas::class.java)
        load(GLASS5.toPathFromInternalFile(), Texture::class.java)
        load(BUTTONS_PLAY_PAUSE.toPathFromInternalFile(), TextureAtlas::class.java)
        load(BUTTONS_ACCEPT_DENY.toPathFromInternalFile(), TextureAtlas::class.java)
        load(BUTTONS_SOUND.toPathFromInternalFile(), TextureAtlas::class.java)
        load(BUTTONS_SETTINGS.toPathFromInternalFile(), TextureAtlas::class.java)
        load(BUTTONS_BACK.toPathFromInternalFile(), TextureAtlas::class.java)
        load(BUTTONS_REPLAY.toPathFromInternalFile(), TextureAtlas::class.java)
        load(BUTTONS_ADS_REMOVE.toPathFromInternalFile(), TextureAtlas::class.java)
        load(BUTTONS_STORE.toPathFromInternalFile(), TextureAtlas::class.java)
        load(BUTTONS_RECTANGLE.toPathFromInternalFile(), TextureAtlas::class.java)
        load(SLIDER_UI.toPathFromInternalFile(), TextureAtlas::class.java)
        load(SCROLL_PANE_UI.toPathFromInternalFile(), TextureAtlas::class.java)
        load(SPINY_GLASS_ATLAS.toPathFromInternalFile(), TextureAtlas::class.java)
        load(SPINY_GLASS_PIECE_1_ATLAS.toPathFromInternalFile(), TextureAtlas::class.java)
        load(SPINY_GLASS_PIECE_1_1_ATLAS.toPathFromInternalFile(), TextureAtlas::class.java)
        load(SPINY_GLASS_PIECE_1_2_ATLAS.toPathFromInternalFile(), TextureAtlas::class.java)
        load(SPINY_GLASS_PIECE_2_ATLAS.toPathFromInternalFile(), TextureAtlas::class.java)
        load(SPINY_GLASS_PIECE_2_1_ATLAS.toPathFromInternalFile(), TextureAtlas::class.java)
        load(SPINY_GLASS_PIECE_2_2_ATLAS.toPathFromInternalFile(), TextureAtlas::class.java)
        load(SPINY_GLASS.toPathFromInternalFile(), Texture::class.java)
        load(GLASS2_CRACK1_ATLAS.toPathFromInternalFile(), TextureAtlas::class.java)
        load(GLASS3_CRACK1_ATLAS.toPathFromInternalFile(), TextureAtlas::class.java)
        load(GLASS4_CRACK1_ATLAS.toPathFromInternalFile(), TextureAtlas::class.java)
        load(GLASS5_CRACK1_ATLAS.toPathFromInternalFile(), TextureAtlas::class.java)
        load(MAZE_CUBES.toPathFromInternalFile(), TextureAtlas::class.java)
        load(MAZE_CUBES_2.toPathFromInternalFile(), TextureAtlas::class.java)
        load(MAZE_CUBES_3.toPathFromInternalFile(), TextureAtlas::class.java)
        load(MAZE_CUBES_4.toPathFromInternalFile(), TextureAtlas::class.java)
        load(MAZE_CUBES_5.toPathFromInternalFile(), TextureAtlas::class.java)
        load(HINT_ICON.toPathFromInternalFile(), TextureAtlas::class.java)
        load(FIRE_ATLAS.toPathFromInternalFile(), TextureAtlas::class.java)
    }

    private fun queueSingleTextures() = with(assetManager) {

        load(LIFE_0.toPathFromInternalFile(), Texture::class.java)
        load(LIFE_1.toPathFromInternalFile(), Texture::class.java)
        load(LIFE_2.toPathFromInternalFile(), Texture::class.java)
        load(LIFE_3.toPathFromInternalFile(), Texture::class.java)
        load(LIFE_SINGLE_MISSED.toPathFromInternalFile(), Texture::class.java)
        load(RING.toPathFromInternalFile(), Texture::class.java)
        load(CANDY_RING.toPathFromInternalFile(), Texture::class.java)
        load(CLASSIC_RING.toPathFromInternalFile(), Texture::class.java)
        load(RANDOM_RING.toPathFromInternalFile(), Texture::class.java)
        load(GAME_BACK_GROUND.toPathFromInternalFile(), Texture::class.java)
        load(GAME_BACK_GROUND2.toPathFromInternalFile(), Texture::class.java)
        load(CANDY_GLASS_HIGH_TEXT.toPathFromInternalFile(), Texture::class.java)
        load(CANDY_GLASS_HIGH.toPathFromInternalFile(), Texture::class.java)
        load(IRON_BALL.toPathFromInternalFile(), Texture::class.java)
        load(HAND_EXPLANATION.toPathFromInternalFile(), Texture::class.java)
        load(HAND_CANDY_GLASS.toPathFromInternalFile(), Texture::class.java)
        load(HAND_SPINY_GLASS.toPathFromInternalFile(), Texture::class.java)
        load(DIALOG_STAGE_BACK_GROUND.toPathFromInternalFile(), Texture::class.java)
        load(DIALOG_BACK_GROUND.toPathFromInternalFile(), Texture::class.java)
        load(STORE_LINE_BACK_GROUND.toPathFromInternalFile(), Texture::class.java)
        load(DIALOG_STAGE_BACK_GROUND_LIGHT.toPathFromInternalFile(), Texture::class.java)
        load(STORE_CANDY_GLASS_HAND.toPathFromInternalFile(), Texture::class.java)
        load(STORE_SPINY_HAND.toPathFromInternalFile(), Texture::class.java)
        load(ARROW_LEFT_RED.toPathFromInternalFile(), Texture::class.java)
        load(ARROW_RIGHT_RED.toPathFromInternalFile(), Texture::class.java)
        load(ARROW_RIGHT_GREEN.toPathFromInternalFile(), Texture::class.java)
        load(GRID_TEXTURE.toPathFromInternalFile(), Texture::class.java)
    }

    val breakSoundNames: List<String>
        get() = Collections.unmodifiableList(breakSounds)

    private fun loadBreakSound(fileName: String) {
        assetManager.load(fileName.toPathFromInternalFile(), Sound::class.java)
        breakSounds.add(fileName)
    }

    fun getTexture(fileName: String): Texture = assetManager.get(fileName, Texture::class.java)

    fun getTexturesFromTextureAtlas(fileName: String): Array<AtlasRegion> = assetManager.get(fileName, TextureAtlas::class.java)
            .let {
                makeAtlasTextureFilterBothLinear(it)
                it.regions
            }

    fun getTextureAtlas(fileName: String): TextureAtlas = assetManager.get(fileName, TextureAtlas::class.java).apply { makeAtlasTextureFilterBothLinear(this) }

    fun getSound(fileName: String): Sound = assetManager.get(fileName, Sound::class.java)

    fun getMusic(fileName: String): Music = assetManager.get(fileName, Music::class.java)

    fun getBitmapFont(fileName: String): BitmapFont = assetManager.get(fileName, BitmapFont::class.java).apply {
        region.texture.setFilter(TextureFilter.Linear, TextureFilter.Linear)
        setUseIntegerPositions(false)
        data.setScale(0.003f)
    }

    private fun makeAtlasTextureFilterBothLinear(ta: TextureAtlas) = ta.textures.forEach { it.setFilter(TextureFilter.Linear, TextureFilter.Linear) }


    override fun dispose() = assetManager.dispose()

    override fun error(asset: AssetDescriptor<*>, throwable: Throwable) = Gdx.app.error(TAG, "Couldn't load asset '" + asset.fileName + "'", throwable)

    companion object {
        private val TAG: String = Assets::class.java.name
        const val FONT_CURRENCY_MONTSERRAT = "fonts/Montserrat_65_ORANGE.fnt"
        const val FONT_COMIC_SANS_MS = "fonts/GosmickSansMS_65.fnt"
        const val FONT_COSMIC_SANS_GREEN = "fonts/GosmickSans_Green_65.fnt"
        const val FONT_COSMIC_SANS_ORANGE = "fonts/GosmickSans_Orange_65.fnt"
        const val FONT_COSMIC_SANS_RED = "fonts/GosmickSans_Red_65.fnt"
        const val FONT_COSMIC_SANS_ORANGE_BOLD = "fonts/GosmickSans_Orange_65_BOLD.fnt"
        const val FONT_COURIER_ORANGE = "fonts/Courier_10_Pitch_Orange_BOLD.fnt"
        const val BREAK_SOUND_1 = "sounds/glassbreak/322603__natty23__glass-break.wav"
        const val BREAK_SOUND_2 = "sounds/glassbreak/349240__natty23__glass-break-2.wav"
        const val BREAK_SOUND_3 = "sounds/glassbreak/446137__justinvoke__glass-shatter.wav"
        const val MENU_MUSIC = "sounds/music/peritune-noway3.mp3"
        const val GLASS_APPEAR_SOUND = "sounds/420668__sypherzent__basic-melee-swing-miss-whoosh.wav"
        const val SPINY_BALL_APPEAR_SOUND = "sounds/420670__sypherzent__strong-melee-swing.wav"
        const val NO_HAND_SOUND = "sounds/249300__suntemple__access-denied.wav"
        const val GLASS_MISSED_SOUND = "sounds/443346__tec-studio__alarm-sound-001.wav"
        const val GAME_BACK_GROUUND_SOUND_1 = "sounds/music/396061__edo333__slow-dropping-water-2.mp3"
        const val BREAK_SOUND_8 = "sounds/glassbreak/412054__funwithsound__major-destruction-crash-shatter-from-pillow-fight-disaster-4_modified.mp3"
        const val CANDY_GLASS_HIGH_TEXT = "candyGlassHighTEXT.png"
        const val CANDY_GLASS_HIGH = "candyGlassHigh.png"
        const val RING = "ring.png"
        const val CANDY_RING = "candyRing.png"
        const val CLASSIC_RING = "classicRing.png"
        const val RANDOM_RING = "randomRing.png"
        const val GAME_BACK_GROUND = "backgrounds/gameBackground.png"
        const val GAME_BACK_GROUND2 = "backgrounds/gameBackground2.png"

        const val MAZE_CUBES = "mazecubes/mazecubes.atlas"
        const val MAZE_CUBES_2 = "mazecubes/mazecubes2.atlas"
        const val MAZE_CUBES_3 = "mazecubes/mazecubes3.atlas"
        const val MAZE_CUBES_4 = "mazecubes/mazecubes4.atlas"
        const val MAZE_CUBES_5 = "mazecubes/mazecubes5.atlas"

        internal const val DIALOG_STAGE_BACK_GROUND = "backgrounds/dialogStageBackGround.png"
        internal const val DIALOG_STAGE_BACK_GROUND_LIGHT = "backgrounds/dialogStageBackGround_light.png"
        internal const val DIALOG_BACK_GROUND = "backgrounds/dialogBackGround_T_Wood2.png"
        const val STORE_LINE_BACK_GROUND = "backgrounds/dialogBackGround_T_Wood4.png"

        const val LIFE_0 = "life/life_0.png"
        const val LIFE_1 = "life/life_1.png"
        const val LIFE_2 = "life/life_2.png"
        const val LIFE_3 = "life/life_3.png"

        const val LIFE_SINGLE_MISSED = "life/life_single.png"
        const val HAND_CANDY_GLASS = "hands/handCandy.png"
        const val HAND_SPINY_GLASS = "hands/handSpinyGlass.png"
        const val HAND_EXPLANATION = "hands/hand2.png"
        const val ARROW_LEFT_RED = "arrows/left-red.png"
        const val ARROW_RIGHT_RED = "arrows/right-red.png"
        const val ARROW_RIGHT_GREEN = "arrows/right-green.png"
        const val BUTTONS_PLAY_PAUSE = "ui/buttons.atlas"
        const val BUTTONS_ACCEPT_DENY = "ui/buttons_accept_released.atlas"
        const val BUTTONS_SOUND = "ui/buttons_sound.atlas"
        const val BUTTONS_SETTINGS = "ui/buttons_settings.atlas"
        const val BUTTONS_BACK = "ui/buttons_back.atlas"
        const val BUTTONS_REPLAY = "ui/buttons_replay.atlas"
        const val BUTTONS_ADS_REMOVE = "ui/buttons_adsremove.atlas"
        const val BUTTONS_STORE = "ui/buttons_store.atlas"
        const val BUTTONS_RECTANGLE = "ui/buttons_rectangle.atlas"
        const val SLIDER_UI = "ui/slider_ui.atlas"
        const val SCROLL_PANE_UI = "ui/scrollPane_ui.atlas"
        const val CANDY1_ATLAS = "candy1/candy1.atlas"
        const val CANDY2_ATLAS = "candy2/candy.atlas"
        const val CANDY3_ATLAS = "candy3/candy.atlas"
        const val CANDY4_ATLAS = "candy4/candy.atlas"
        const val CANDY5_ATLAS = "candy5/candy.atlas"
        const val CANDY6_ATLAS = "candy6/candy.atlas"
        const val CANDY7_ATLAS = "candy7/candy.atlas"
        const val CANDY8_ATLAS = "candy8/candy.atlas"
        const val CANDY9_ATLAS = "candy9/candy.atlas"
        const val CANDY10_ATLAS = "candy10/candy.atlas"
        const val CANDY11_ATLAS = "candy11/candy.atlas"
        const val CANDY12_ATLAS = "candy12/candy.atlas"
        const val CANDY13_ATLAS = "candy13/candy.atlas"
        const val CANDY14_ATLAS = "candy14/candy.atlas"
        const val CANDY15_ATLAS = "candy15/candy.atlas"
        const val CANDY16_ATLAS = "candy16/candy.atlas"
        const val CANDY17_ATLAS = "candy17/candy.atlas"
        const val GLASS2_PIECE_1_ATLAS = "glasses/glass2/piece1/glass.atlas"
        const val GLASS2_PIECE_2_ATLAS = "glasses/glass2/piece2/glass.atlas"
        const val GLASS2_PIECE_1_1_ATLAS = "glasses/glass2/piece1_1/glass.atlas"
        const val GLASS2_PIECE_1_2_ATLAS = "glasses/glass2/piece1_2/glass.atlas"
        const val GLASS2_PIECE_2_1_ATLAS = "glasses/glass2/piece2_1/glass.atlas"
        const val GLASS2_PIECE_2_2_ATLAS = "glasses/glass2/piece2_2/glass.atlas"
        const val GLASS2 = "glasses/glass2/glass.png"
        const val GLASS3_PIECE_1_ATLAS = "glasses/glass3/piece1/glass.atlas"
        const val GLASS3_PIECE_2_ATLAS = "glasses/glass3/piece2/glass.atlas"
        const val GLASS3_PIECE_1_1_ATLAS = "glasses/glass3/piece1_1/glass.atlas"
        const val GLASS3_PIECE_1_2_ATLAS = "glasses/glass3/piece1_2/glass.atlas"
        const val GLASS3_PIECE_2_1_ATLAS = "glasses/glass3/piece2_1/glass.atlas"
        const val GLASS3_PIECE_2_2_ATLAS = "glasses/glass3/piece2_2/glass.atlas"
        const val GLASS3 = "glasses/glass3/glass.png"
        const val GLASS4_PIECE_1_ATLAS = "glasses/glass4/piece1/glass.atlas"
        const val GLASS4_PIECE_2_ATLAS = "glasses/glass4/piece2/glass.atlas"
        const val GLASS4_PIECE_1_1_ATLAS = "glasses/glass4/piece1_1/glass.atlas"
        const val GLASS4_PIECE_1_2_ATLAS = "glasses/glass4/piece1_2/glass.atlas"
        const val GLASS4_PIECE_2_1_ATLAS = "glasses/glass4/piece2_1/glass.atlas"
        const val GLASS4_PIECE_2_2_ATLAS = "glasses/glass4/piece2_2/glass.atlas"
        const val GLASS4 = "glasses/glass4/glass.png"
        const val GLASS5_PIECE_1_ATLAS = "glasses/glass5/piece1/glass.atlas"
        const val GLASS5_PIECE_2_ATLAS = "glasses/glass5/piece2/glass.atlas"
        const val GLASS5_PIECE_1_1_ATLAS = "glasses/glass5/piece1_1/glass.atlas"
        const val GLASS5_PIECE_1_2_ATLAS = "glasses/glass5/piece1_2/glass.atlas"
        const val GLASS5_PIECE_2_1_ATLAS = "glasses/glass5/piece2_1/glass.atlas"
        const val GLASS5_PIECE_2_2_ATLAS = "glasses/glass5/piece2_2/glass.atlas"
        const val GLASS5 = "glasses/glass5/glass.png"
        const val IRON_BALL = "balls/ironball/ball.png"
        const val SPINY_GLASS_ATLAS = "glasses/spinyball/piece_all/glass.atlas"
        const val SPINY_GLASS_PIECE_1_ATLAS = "glasses/spinyball/piece1/glass.atlas"
        const val SPINY_GLASS_PIECE_2_ATLAS = "glasses/spinyball/piece2/glass.atlas"
        const val SPINY_GLASS_PIECE_1_1_ATLAS = "glasses/spinyball/piece1_1/glass.atlas"
        const val SPINY_GLASS_PIECE_1_2_ATLAS = "glasses/spinyball/piece1_2/glass.atlas"
        const val SPINY_GLASS_PIECE_2_1_ATLAS = "glasses/spinyball/piece2_1/glass.atlas"
        const val SPINY_GLASS_PIECE_2_2_ATLAS = "glasses/spinyball/piece2_2/glass.atlas"
        const val SPINY_GLASS = "glasses/spinyball/glass.png"
        const val GLASS2_CRACK1_ATLAS = "glasses/cracks/piece1/glass2/glass.atlas"
        const val GLASS3_CRACK1_ATLAS = "glasses/cracks/piece1/glass3/glass.atlas"
        const val GLASS4_CRACK1_ATLAS = "glasses/cracks/piece1/glass4/glass.atlas"
        const val GLASS5_CRACK1_ATLAS = "glasses/cracks/piece1/glass5/glass.atlas"

        const val FIRE_ATLAS = "effects/fire.atlas"

        const val STORE_SPINY_HAND = "store/spiny_hand.png"
        const val STORE_CANDY_GLASS_HAND = "store/candy_glass_hand.png"
        const val HINT_ICON = "hint/hint.atlas"

        const val GRID_TEXTURE = "grid.png"
    }
}