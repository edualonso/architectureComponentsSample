package com.example.edu.myapplication.util

import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

/**
 * Created by edu on 10/01/2018.
 */
object JsonParsingUtil {

    fun readFile(inputStream: InputStream): String {
        val buffer = StringBuilder()
        val bufferedReader = BufferedReader(InputStreamReader(inputStream, "UTF-8"))
        var jsonString: String

        while (true) {
            jsonString = bufferedReader.readLine() ?: break
            buffer.append(jsonString)
        }
        inputStream.close()

        return buffer.toString()
    }

}