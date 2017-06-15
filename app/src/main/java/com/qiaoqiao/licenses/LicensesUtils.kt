package com.qiaoqiao.licenses

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

internal fun readTextFile(inputStream: InputStream): String? = try {
    val inputreader = InputStreamReader(inputStream)
    val buffreader = BufferedReader(inputreader)
    val text = StringBuilder()

    var line: String = buffreader.readLine()
    while (line != null) {
        text.append(line)
        text.append('\n')
        line = buffreader.readLine()
    }
    text.toString()
} catch (ex: IOException) {
    null
}

