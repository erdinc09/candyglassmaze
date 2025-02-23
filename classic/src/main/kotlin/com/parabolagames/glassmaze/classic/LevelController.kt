package com.parabolagames.glassmaze.classic

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.utils.Pools
import com.google.common.base.Optional
import com.google.common.base.Preconditions.checkArgument
import com.google.common.base.Preconditions.checkState
import com.google.common.collect.ImmutableList
import com.parabolagames.glassmaze.framework.ForGame
import java.io.BufferedReader
import java.util.*
import javax.inject.Inject
import javax.inject.Named

// y=row x=col
//
// Veri Formatı:
//
// Level no
// Satir sayısı/boşluk/sutun sayısı
// Top koordinatı y/boşluk/x
// Kırmızı dikenli cam koordinatları y1/boşluk/x1,y2/boşluk/x2, …
// Şekerli cam koordinatları y1/boşluk/x1,y2/boşluk/x2, …
// Boş cam koordinatları y1/boşluk/x1,y2/boşluk/x2, …
// <zorunlu kırılması gereken şekerli cam> <zorunlu kırılması gereken kırmızı dikenli cam>
@ForGame
internal class LevelController
@Inject constructor(
        private val dataPersistenceManager: IClassicMazePersistenceManager,
        @param:Named(ClassicModeModule3.CLASSIC_LEVEL_NUMBER) private val levelNumber: Optional<Int>,
        @param:Named(ClassicModeModule2.CLASSIC_RANDOM_PLAY_LEVEL_LIMIT) private val levelLimitForRandomLevel: Int,
        private val subMode: ClassicModeSubMode) {

    private val randomLevelNumber: Int
        get() = if (dataPersistenceManager.levelNumber == 1) {
            1
        } else {
            if (dataPersistenceManager.levelNumber <= levelLimitForRandomLevel) {
                MathUtils.random(1, dataPersistenceManager.levelNumber - 1)//not necessary, since we do not let the random mode start
            } else {
                //always let the levels without crushers
                val randomLevelIndex = LEVELS_WITHOUT_CRUSHERS.binarySearch(dataPersistenceManager.levelNumber, 0, LEVELS_WITHOUT_CRUSHERS.size)
                val randomLevelIndexNormalize = if (randomLevelIndex > 0) randomLevelIndex else -randomLevelIndex - 1 - 1
                LEVELS_WITHOUT_CRUSHERS[MathUtils.random(0, randomLevelIndexNormalize)]
            }
        }

    fun createLevel(): Level? {
        val levelNum: Int = if (levelNumber.isPresent) {
            checkArgument(levelNumber.get() > 0)
            levelNumber.get()
        } else {
            if (subMode === ClassicModeSubMode.CLASSIC)
                getExternalNumberIfItIsNotPlayed()?.toInt()
                        ?: dataPersistenceManager.levelNumber
            else randomLevelNumber//random mode
        }
        var gd: GridData? = generateGrid(levelNum)
        return if (gd != null) Pools.get(Level::class.java).obtain().apply { poolInit(levelNum, gd) } else null
    }

    private fun getExternalNumberIfItIsNotPlayed() = if (!externalLevelPlayed) EXTERNAL_LEVEL_NUMBER.also { externalLevelPlayed = true } else null

    private fun generateGrid(levelNumber: Int): GridData? {
        val levelFileHandle = if (EXTERNAL_LEVEL_FOLDER != null)
            Gdx.files.absolute("$EXTERNAL_LEVEL_FOLDER/$levelNumber.txt")
        else Gdx.files.internal("levels/$levelNumber.txt")

        if (!levelFileHandle.exists()) {
            return null
        }
        val fileContentBuilder = ImmutableList.builder<String>()
        levelFileHandle.reader().use { r ->
            BufferedReader(r).use { br ->
                var line: String?
                while (br.readLine().also { line = it } != null) {
                    fileContentBuilder.add(line!!.replace(",".toRegex(), "   "))
                }
            }
        }
        val lines = fileContentBuilder.build()
        var row: Int
        var col: Int
        Scanner(lines[ROW_COL_INDEX]).use { s ->
            row = s.nextInt()
            col = s.nextInt()
        }
        val gd = Pools.get(GridData::class.java).obtain()
        gd.poolInit(row, col)
        // ALL OBSTACLE
        for (row_ in 0 until row) {
            for (col_ in 0 until col) {
                gd.setGridValue(row_, col_, GridValue.OBSTACLE)
            }
        }
        var ballRow: Int
        var ballCol: Int
        Scanner(lines[BALL_INDEX]).use { s ->
            ballRow = s.nextInt()
            ballCol = s.nextInt()
        }
        gd.setGridValue(ballRow, ballCol, GridValue.BALL)
        Scanner(lines[SPINY_BALL_INDEX]).use { s ->
            while (s.hasNextInt()) {
                val row_ = s.nextInt()
                val col_ = s.nextInt()
                checkState(
                        gd.getGridValue(row_, col_) === GridValue.OBSTACLE,
                        "(row,col,level)=(%s,%s, %s.txt) is already defined as %s before, now new definition is SPINY_BALL",
                        row_,
                        col_,
                        levelNumber,
                        gd.getGridValue(row_, col_).toString())
                gd.setGridValue(row_, col_, GridValue.SPINY_BALL)
                gd.spinyGlassCount++
            }
        }
        gd.candyCount = 0
        Scanner(lines[CANDY_GLASS_INDEX]).use { s ->
            while (s.hasNextInt()) {
                val row_ = s.nextInt()
                val col_ = s.nextInt()
                checkState(
                        gd.getGridValue(row_, col_) === GridValue.OBSTACLE,
                        "(row,col,level)=(%s,%s, %s.txt) is already defined as %s before, now new definition is CANDY_GLASS",
                        row_,
                        col_,
                        levelNumber,
                        gd.getGridValue(row_, col_).toString())
                gd.setGridValue(row_, col_, GridValue.CANDY_GLASS)
                gd.candyCount++
            }
        }
        gd.glassCount = 0
        Scanner(lines[GLASS_INDEX]).use { s ->
            while (s.hasNextInt()) {
                val row_ = s.nextInt()
                val col_ = s.nextInt()
                checkState(
                        gd.getGridValue(row_, col_) === GridValue.OBSTACLE,
                        "(row,col,level)=(%s,%s, %s.txt) is already defined as %s before, now new definition is GLASS",
                        row_,
                        col_,
                        levelNumber,
                        gd.getGridValue(row_, col_).toString())
                gd.setGridValue(row_, col_, GridValue.GLASS)
                gd.glassCount++
            }
        }
        Scanner(lines[MANUEL_BREAKS_INDEX]).use { s ->
            gd.candyGlassCountToManualBreak = s.nextInt()
            gd.spinyGlassCountToManualBreak = s.nextInt()
        }
        return gd
    }

    companion object {
        private val EXTERNAL_LEVEL_FOLDER = System.getProperty("externalLevelFolder")
        private val EXTERNAL_LEVEL_NUMBER = System.getProperty("externalLevelNumber")
        private const val ROW_COL_INDEX = 1
        private const val BALL_INDEX = 2
        private const val SPINY_BALL_INDEX = 3
        private const val CANDY_GLASS_INDEX = 4
        private const val GLASS_INDEX = 5
        const val MANUEL_BREAKS_INDEX = 6
        private var externalLevelPlayed = false

        private val LEVELS_WITHOUT_CRUSHERS = listOf(1, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 23, 26, 27, 28, 29, 33, 34,
                35, 36, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 49, 51, 53, 54, 55, 57, 58, 61, 62, 64, 67, 69, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 83,
                84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 95, 97, 100, 101, 102, 104, 105, 106, 108, 109, 111, 113, 114, 115, 116, 118, 119, 121, 122, 124, 125, 126,
                128, 129, 130, 131, 132, 134, 136, 139, 141, 142, 144, 145, 146, 147, 148, 150, 151, 152, 153, 154, 155, 156, 164, 165, 166, 167, 168, 169, 171, 173,
                174, 176, 177, 178, 179, 180, 181, 182, 187, 190, 191, 193, 194, 195, 196, 197, 198, 199, 201, 202, 204, 205, 206, 207, 208, 209, 210, 211, 212, 213, 215,
                216, 218, 220, 222, 223, 225, 226, 227, 229, 230, 232, 234, 235, 236, 237, 239, 240, 241, 242, 244, 245, 246, 247, 249, 250, 252, 253, 254, 255, 256, 257,
                259, 260, 261, 262, 263, 264, 265, 266, 268, 269, 270, 271, 272, 273, 274, 275, 276, 277, 278, 279, 280, 284, 285, 287, 288, 289, 290, 291, 292, 293, 294,
                295, 296, 297, 298, 301, 302, 303, 306, 308, 309, 311, 312, 313, 314, 315, 317, 318, 319, 320, 324, 325, 326, 327, 328, 331, 332, 334, 336, 337, 339, 340,
                341, 342, 345, 346, 347, 348, 349, 350, 351, 352, 353, 354, 357, 358, 360, 362, 363, 364, 365, 366, 367, 368, 370, 371, 372, 373, 374, 375, 376, 377, 380,
                383, 385, 386, 387, 388, 390, 391, 392, 393, 395, 396, 397, 398, 399, 400, 402, 406, 407, 408, 409, 410, 412, 414, 416, 417, 418, 420, 422, 423, 427, 431,
                432, 433, 436, 437, 439, 441, 443, 446, 447, 448, 449, 450, 451, 452, 453, 455, 456, 457, 458, 459, 461, 463, 465, 467, 468, 469, 474, 475, 482, 483, 485,
                486, 487, 489, 490, 492, 493, 495, 496, 497, 498, 499, 500, 501, 503, 505, 506, 507, 508, 512, 513, 514, 516, 517, 519, 520, 521, 522, 523, 526, 527, 528,
                530, 531, 532, 534, 535, 537, 538, 539, 541, 542, 544, 546, 548, 549, 550, 552, 554, 555, 557, 558, 559, 560, 562, 564, 565, 567, 568, 572, 573, 575, 576,
                577, 579, 581, 583, 584, 585, 586, 587, 588, 589, 591, 592, 593, 595, 596, 597, 598, 599, 600, 601, 602, 603, 604, 606, 609, 610, 611, 612, 615, 619, 621,
                622, 623, 624, 626, 627, 628, 629, 630, 634, 635, 636, 639, 640, 641, 643, 644, 645, 648, 649, 650, 653, 655, 656, 657, 659, 662, 663, 664, 666, 667, 668,
                669, 670, 671, 673, 674, 675, 676, 677, 678, 680, 682, 687, 688, 689, 690, 691, 693, 694, 696, 697, 698, 699, 701, 702, 703, 704, 705, 708, 710, 712, 715,
                716, 717, 718, 720, 724, 725, 726, 727, 730, 731, 733, 734, 735, 736, 737, 739, 740, 741, 742, 744, 746, 747, 748, 750, 751, 753, 754, 755, 756, 757, 760,
                762, 764, 765, 767, 770, 773, 774, 778, 780, 781, 783, 784, 785, 786, 787, 789, 791, 792, 793, 794, 796, 797, 798, 799)
    }
}