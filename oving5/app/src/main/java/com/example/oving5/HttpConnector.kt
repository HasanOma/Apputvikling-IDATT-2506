package com.example.oving5

import android.util.Log
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.io.UnsupportedEncodingException
import java.net.*

class HttpConnector(private val URL: String) {
    private val utf = "UTF-8"

    init {
        CookieHandler.setDefault(CookieManager(null, CookiePolicy.ACCEPT_ALL))
    }

    private fun connect(url: String): URLConnection {
        val connection = URL(url).openConnection() as HttpURLConnection
        connection.setRequestProperty("Accept-Charset", utf)
        return connection
    }

    fun post(params: Map<String, String>): String {
        val connection = connect(URL)
        connection.doOutput = true
        val type = "application/x-www-form-urlencoded; charset=$utf"
        connection.setRequestProperty("Content-Type", type)
        connection.outputStream.use { outputStream ->
            val postString = encodeParams(params)
            outputStream.write(postString.toByteArray(charset(utf)))
        }
        connection.inputStream.use { inputStream ->
            return responseBody(inputStream, charSet(connection))
        }
    }

    private fun encodeParams(params: Map<String, String>): String {
        var paramString = "?"
        for ((key, value) in params) {
            try {
                paramString += URLEncoder.encode(key, utf)
                paramString += "="
                paramString += URLEncoder.encode(value, utf)
                paramString += "&"
            } catch (e: UnsupportedEncodingException) {
                Log.e("Param encoding failed", e.toString())
            }
        }
        return paramString
    }

    fun getWithHeader(parameterList: Map<String, String>): String {
        val connection = connect(URL + encodeParams(parameterList))
        var response = ""
        for ((key, value) in connection.headerFields) response += "$key=$value\n"

        connection.inputStream.use { inputStream ->
            response += responseBody(inputStream, charSet(connection))
        }
        return response
    }

    fun get(parameterList: Map<String, String>): String {
        val connection = connect(URL + encodeParams(parameterList))
        connection.inputStream.use { response ->
            return responseBody(response, charSet(connection))
        }
    }

    private fun responseBody(inputStream: InputStream, charset: String?): String {
        var body = ""
        try {
            BufferedReader(InputStreamReader(inputStream, charset)).use { bufferedReader ->
                var line: String?
                do {
                    line = bufferedReader.readLine()
                    body += "$line\n"
                } while (line != null)
            }
        } catch (e: Exception) {
            body += "******* Problem reading from server *******\n$e"
        }
        return body
    }

    private fun charSet(connection: URLConnection): String? {
        var charset: String? = utf
        val type = connection.contentType
        val info = type.replace(" ", "").split(";").toTypedArray()
        for (param in info) {
            if (param.startsWith("charset=")) charset = param.split("=").toTypedArray()[1]
        }
        return charset
    }
}