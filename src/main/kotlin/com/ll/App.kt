package com.ll

class App {
    private val wiseSayingRepository = WiseSayingRepository()
    private val wiseSayingService = WiseSayingService(wiseSayingRepository)
    private val wiseSayingController = WiseSayingController(wiseSayingService)

    fun run() {
        println("== 명언 앱 ==")

        while (true) {
            print("명령) ")
            val command = readln()
            val rq = Rq(command)

            if (rq.action == "종료") {
                break
            }

            if (rq.action == "등록") {
                wiseSayingController.register()
            }

            if (rq.action == "목록") {
                wiseSayingController.list()
            }

            if (rq.action == "삭제") {
                wiseSayingController.delete(rq)
            }

            if (rq.action == "수정") {
                wiseSayingController.modify(rq)
            }

            if (rq.action == "빌드") {
                wiseSayingController.build()
            }
        }
    }
}