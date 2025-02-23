package com.parabolagames.glassmaze.classic

import com.google.common.collect.ImmutableList
import java.io.BufferedReader
import java.io.File
import java.util.*


fun main(args: Array<String>) {

    val filteredFileNames = mutableListOf<Int>()

    File(("./android/assets/levels")).walk().filter { it.isFile }.forEach {

        val fileContentBuilder = ImmutableList.builder<String>()
        it.reader().use { r ->
            BufferedReader(r).use { br ->
                var line: String?
                while (br.readLine().also { line = it } != null) {
                    fileContentBuilder.add(line!!.replace(",".toRegex(), "   "))
                }
            }
        }
        val lines = fileContentBuilder.build()

        Scanner(lines[LevelController.MANUEL_BREAKS_INDEX]).use { s ->
            val candyGlassCountToManualBreak = s.nextInt()
            val spinyGlassCountToManualBreak = s.nextInt()
            if (candyGlassCountToManualBreak == 0 && spinyGlassCountToManualBreak == 0) {
                filteredFileNames.add(it.nameWithoutExtension.toInt())
            }
        }
    }
    filteredFileNames.sort()
    println(filteredFileNames)
}
