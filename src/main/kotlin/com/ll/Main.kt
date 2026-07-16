package com.ll

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

fun main() {
    println("== 명언 앱 ==")

    var lastId = 0
    val wiseSayings = mutableListOf<WiseSaying>()

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

            wiseSayings.add(WiseSaying(lastId, content, author))

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
                println("${id}번 명언은 존재하지않습니다.")
                continue
            }
            wiseSayings.remove(wiseSaying)

            println("${id}번 명언이 삭제되었습니다.")
        }

        if (rq.action == "수정") {
            val id = rq.getParamAsInt("id",0)

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


        }
    }
}