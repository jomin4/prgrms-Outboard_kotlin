package com.ll

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

    fun getParam(key: String, defaultValue: String): String {
        return params[key] ?: defaultValue
    }
}