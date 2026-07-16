package com.ll

class WiseSaying(
    val id: Int,
    val content: String,
    val author: String
)

fun main() {
    println("명언앱")

    var lastId = 0
    val wiseSayings = mutableListOf<WiseSaying>()


    while (true) {
        print("명령)")
        val command = readln()

        if(command == "종료") {
            break
        }

        if (command == "등록") {
            print("명언 : ")
            val content = readln()

            print("작가 : ")
            val author = readln()

            lastId++

            wiseSayings.add(WiseSaying(lastId,content,author))

            println("${lastId}번 명언이 등록되었습니다.")
        }

        if (command == "목록") {
            println("번호/작가/명언")
            println("------------")

            for (wiseSaying in wiseSayings.reversed()) {
                println("${wiseSaying.id} / ${wiseSaying.author} / ${wiseSaying.content}")
            }
        }
    }
}