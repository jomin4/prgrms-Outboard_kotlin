package com.ll

class WiseSayingController(
    private val wiseSayingService: WiseSayingService
) {
    fun register() {
        print("명언 : ")
        val content = readln()

        print("작가 : ")
        val author = readln()

        val id = wiseSayingService.write(content, author)

        println("${id}번 명언이 등록되었습니다.")
    }

    fun list(rq: Rq) {
        val keywordType = rq.getParam("keywordType", "")
        val keyword = rq.getParam("keyword", "")
        val page = rq.getParamAsInt("page", 1)

        val pageResult = wiseSayingService.getWiseSayings(keywordType, keyword, page)

        if (keyword.isNotBlank()) {
            println("----------------------")
            println("검색타입 : $keywordType")
            println("검색어 : $keyword")
            println("----------------------")
        }

        println("번호 / 작가 / 명언")
        println("----------------------")

        for (wiseSaying in pageResult.items) {
            println("${wiseSaying.id} / ${wiseSaying.author} / ${wiseSaying.content}")
        }

        println("----------------------")
        println("페이지 : ${renderPageLine(pageResult.currentPage, pageResult.totalPages)}")
    }

    private fun renderPageLine(currentPage: Int, totalPages: Int): String {
        return (1..totalPages).joinToString(" / ") { if (it == currentPage) "[$it]" else "$it" }
    }

    fun delete(rq: Rq) {
        val id = rq.getParamAsInt("id", 0)

        if (!wiseSayingService.deleteWiseSaying(id)) {
            println("${id}번 명언은 존재하지 않습니다.")
            return
        }

        println("${id}번 명언이 삭제되었습니다.")
    }

    fun modify(rq: Rq) {
        val id = rq.getParamAsInt("id", 0)

        val wiseSaying = wiseSayingService.getWiseSaying(id)

        if (wiseSaying == null) {
            println("${id}번 명언은 존재하지 않습니다.")
            return
        }

        println("명언(기존) : ${wiseSaying.content}")
        print("명언 : ")
        val content = readln()

        println("작가(기존) : ${wiseSaying.author}")
        print("작가 : ")
        val author = readln()

        wiseSayingService.modifyWiseSaying(id, content, author)
    }

    fun build() {
        wiseSayingService.buildDataJson()
        println("data.json 파일의 내용이 갱신되었습니다.")
    }
}