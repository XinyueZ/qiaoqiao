package com.qiaoqiao.licenses

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

internal fun readTextFile(inputStream: InputStream): String? = try {
    val inputreader = InputStreamReader(inputStream)
    val buffreader = BufferedReader(inputreader)
    val text = StringBuilder()

    var line: String?
    while (true) {
        line = buffreader.readLine()
        if (line == null) break
        text.append(line)
        text.append('\n')
    }
    text.toString()
} catch (ex: IOException) {
    null
}

