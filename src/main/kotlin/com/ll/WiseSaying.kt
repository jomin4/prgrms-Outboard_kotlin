package com.ll

class WiseSaying(
    val id: Int,
    var content: String,
    var author: String
)

fun WiseSaying.toJsonStr(): String {
    return """
        {
          "id": $id,
          "content": "$content",
          "author": "$author"
        }
    """.trimIndent()
}