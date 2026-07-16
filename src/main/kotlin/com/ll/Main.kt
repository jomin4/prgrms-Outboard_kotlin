package com.ll

import java.io.File

class WiseSaying(
    val id: Int,
    var content: String,
    var author: String
)

class Rq(command: String) {
    val action: String
    private val params: Map<String, String>

    init {
        val actionAndQuery = command.split("?", limit = 2)
        action = actionAndQuery[0]

        params = if (actionAndQuery.size == 2) {
            actionAndQuery[1].split("&").associate {
                val keyValue = it.split("=")
                keyValue[0] to keyValue[1]
            }
        } else {
            emptyMap()
        }
    }

    fun getParamAsInt(key: String, defaultValue: Int): Int {
        return params[key]?.toIntOrNull() ?: defaultValue
    }
}

val wiseSayingDbDir = File("db/wiseSaying")

fun saveWiseSayingToFile(wiseSaying: WiseSaying) {
    wiseSayingDbDir.mkdirs()

    val json = """
        {
          "id": ${wiseSaying.id},
          "content": "${wiseSaying.content}",
          "author": "${wiseSaying.author}"
        }
    """.trimIndent()

    File(wiseSayingDbDir, "${wiseSaying.id}.json").writeText(json)
}

fun deleteWiseSayingFile(id: Int) {
    File(wiseSayingDbDir, "$id.json").delete()
}

fun loadWiseSayingFromFile(file: File): WiseSaying {
    val lines = file.readText().lines()

    val id = lines[1].substringAfter(":").trim().removeSuffix(",").toInt()
    val content = lines[2].substringAfter(":").trim().removeSuffix(",").removeSurrounding("\"")
    val author = lines[3].substringAfter(":").trim().removeSurrounding("\"")

    return WiseSaying(id, content, author)
}

fun loadWiseSayings(): MutableList<WiseSaying> {
    if (!wiseSayingDbDir.exists()) {
        return mutableListOf()
    }

    return wiseSayingDbDir.listFiles { file -> file.name.endsWith(".json") }
        ?.map { loadWiseSayingFromFile(it) }
        ?.sortedBy { it.id }
        ?.toMutableList()
        ?: mutableListOf()
}

fun saveLastId(lastId: Int) {
    wiseSayingDbDir.mkdirs()
    File(wiseSayingDbDir, "lastId.txt").writeText(lastId.toString())
}

fun loadLastId(): Int {
    val file = File(wiseSayingDbDir, "lastId.txt")

    if (!file.exists()) {
        return 0
    }

    return file.readText().trim().toInt()
}

fun main() {
    println("== 명언 앱 ==")

    var lastId = loadLastId()
    val wiseSayings = loadWiseSayings()

    while (true) {
        print("명령) ")
        val command = readln()
        val rq = Rq(command)

        if (rq.action == "종료") {
            break
        }

        if (rq.action == "등록") {
            print("명언 : ")
            val content = readln()

            print("작가 : ")
            val author = readln()

            lastId++

            val wiseSaying = WiseSaying(lastId, content, author)
            wiseSayings.add(wiseSaying)

            saveWiseSayingToFile(wiseSaying)
            saveLastId(lastId)

            println("${lastId}번 명언이 등록되었습니다.")
        }

        if (rq.action == "목록") {
            println("번호 / 작가 / 명언")
            println("----------------------")

            for (wiseSaying in wiseSayings.reversed()) {
                println("${wiseSaying.id} / ${wiseSaying.author} / ${wiseSaying.content}")
            }
        }

        if (rq.action == "삭제") {
            val id = rq.getParamAsInt("id", 0)

            val wiseSaying = wiseSayings.firstOrNull { it.id == id }

            if (wiseSaying == null) {
                println("${id}번 명언은 존재하지 않습니다.")
                continue
            }

            wiseSayings.remove(wiseSaying)
            deleteWiseSayingFile(id)

            println("${id}번 명언이 삭제되었습니다.")
        }

        if (rq.action == "수정") {
            val id = rq.getParamAsInt("id", 0)

            val wiseSaying = wiseSayings.firstOrNull { it.id == id }

            if (wiseSaying == null) {
                println("${id}번 명언은 존재하지 않습니다.")
                continue
            }

            println("명언(기존) : ${wiseSaying.content}")
            print("명언 : ")
            wiseSaying.content = readln()

            println("작가(기존) : ${wiseSaying.author}")
            print("작가 : ")
            wiseSaying.author = readln()

            saveWiseSayingToFile(wiseSaying)
        }
    }
}