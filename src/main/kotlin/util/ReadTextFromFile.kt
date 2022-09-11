package util

import java.io.File

fun readTextFromFile(s: String): String {
    return File("src/main/resources/$s").readText()
}