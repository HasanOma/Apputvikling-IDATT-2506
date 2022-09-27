package com.example.oving5

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlin.system.exitProcess


class MainActivity : AppCompatActivity() {
    val URL = "https://bigdata.idi.ntnu.no/mobil/tallspill.jsp"
    val get = "GET"
    val post = "POST"
    val header = "GET_HEADER"
    private val network: HttpConnector = HttpConnector(URL)

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnListener()
    }

    private fun btnListener(){
        val btn = findViewById<Button>(R.id.sendBtn)
        btn.setOnClickListener{
            serverRequest(get, requestParams())
        }
        val bb = findViewById<Button>(R.id.confBtn)
        bb.setOnClickListener{
            serverRequest(get, paramNumber())
        }
    }

    private fun serverRequest(http: String, params: Map<String, String>) {
        CoroutineScope(Dispatchers.IO).launch {
            val response: String = try {
                when (http) {
                    get -> network.get(params)
                    post -> network.post(params)
                    header -> network.getWithHeader(params)
                    else -> {
                        exitProcess(0)}
                }
            } catch (e: Exception) {
                e.toString()
            }
            MainScope().launch {
                serverResponse(response)
            }
        }
    }

    private fun requestParams(): Map<String, String> {
        val name = findViewById<TextView>(R.id.textName).text.toString()
        val cardNr = findViewById<TextView>(R.id.textNumber).text.toString()
        return mapOf(
            "navn" to name,
            "kortnummer" to cardNr,
        )
    }

    private fun paramNumber():Map<String, String> {
        val number = findViewById<TextView>(R.id.textNumber2).text.toString()
        return mapOf(
            "tall" to number
        )
    }

    private fun serverResponse(response: String?) {
        findViewById<TextView>(R.id.resServer).text = response
    }
}
