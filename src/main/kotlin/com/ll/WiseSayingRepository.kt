package com.ll

import java.io.File

class WiseSayingRepository {
    private val dbDir = File("db/wiseSaying")
    private var lastId: Int = loadLastId()
    private val wiseSayings: MutableList<WiseSaying> = loadWiseSayings()

    private fun loadLastId(): Int {
        val file = File(dbDir, "lastId.txt")
        if (!file.exists()) return 0
        return file.readText().trim().toInt()
    }

    private fun saveLastId() {
        dbDir.mkdirs()
        File(dbDir, "lastId.txt").writeText(lastId.toString())
    }

    private fun loadWiseSayings(): MutableList<WiseSaying> {
        if (!dbDir.exists()) return mutableListOf()

        return dbDir.listFiles { file -> file.name.endsWith(".json") }
            ?.map { loadWiseSayingFromFile(it) }
            ?.sortedBy { it.id }
            ?.toMutableList()
            ?: mutableListOf()
    }

    private fun loadWiseSayingFromFile(file: File): WiseSaying {
        val lines = file.readText().lines()
        val id = lines[1].substringAfter(":").trim().removeSuffix(",").toInt()
        val content = lines[2].substringAfter(":").trim().removeSuffix(",").removeSurrounding("\"")
        val author = lines[3].substringAfter(":").trim().removeSurrounding("\"")
        return WiseSaying(id, content, author)
    }

    private fun saveWiseSayingToFile(wiseSaying: WiseSaying) {
        dbDir.mkdirs()
        File(dbDir, "${wiseSaying.id}.json").writeText(wiseSaying.toJsonStr())
    }

    private fun deleteWiseSayingFile(id: Int) {
        File(dbDir, "$id.json").delete()
    }

    fun genNextId(): Int {
        lastId++
        saveLastId()
        return lastId
    }

    fun save(wiseSaying: WiseSaying) {
        if (!wiseSayings.contains(wiseSaying)) {
            wiseSayings.add(wiseSaying)
        }
        saveWiseSayingToFile(wiseSaying)
    }

    fun findAll(): List<WiseSaying> {
        return wiseSayings
    }

    fun findById(id: Int): WiseSaying? {
        return wiseSayings.firstOrNull { it.id == id }
    }

    fun deleteById(id: Int) {
        val wiseSaying = findById(id) ?: return
        wiseSayings.remove(wiseSaying)
        deleteWiseSayingFile(id)
    }

    fun buildDataJson() {
        val itemsJson = wiseSayings
            .sortedBy { it.id }
            .joinToString(",\n") { it.toJsonStr().prependIndent("  ") }

        File("data.json").writeText("[\n$itemsJson\n]")
    }
}