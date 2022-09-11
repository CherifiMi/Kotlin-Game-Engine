package util

import java.io.File

fun readTextFromFile(s: String): String {
    return try {
        File("src/main/resources/$s").readText()
    }catch (e: Exception){
        println(e)
        assert(false)
        ""
    }
}