package com.ll

fun main() {
    println("명언앱")


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

            println("1번 명언이 등록되었습니다.")
        }
    }
}