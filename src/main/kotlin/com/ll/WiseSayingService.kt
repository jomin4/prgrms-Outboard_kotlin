package com.ll

class WiseSayingService(
    private val wiseSayingRepository: WiseSayingRepository
) {
    fun write(content: String, author: String): Int {
        val id = wiseSayingRepository.genNextId()
        wiseSayingRepository.save(WiseSaying(id, content, author))
        return id
    }

    fun getWiseSayings(keywordType: String = "", keyword: String = "", page: Int = 1): Page<WiseSaying> {
        return wiseSayingRepository.findAllPaged(keywordType, keyword, page, 5)
    }

    fun getWiseSaying(id: Int): WiseSaying? {
        return wiseSayingRepository.findById(id)
    }

    fun deleteWiseSaying(id: Int): Boolean {
        wiseSayingRepository.findById(id) ?: return false
        wiseSayingRepository.deleteById(id)
        return true
    }

    fun modifyWiseSaying(id: Int, content: String, author: String): Boolean {
        val wiseSaying = wiseSayingRepository.findById(id) ?: return false
        wiseSaying.content = content
        wiseSaying.author = author
        wiseSayingRepository.save(wiseSaying)
        return true
    }

    fun buildDataJson() {
        wiseSayingRepository.buildDataJson()
    }
}