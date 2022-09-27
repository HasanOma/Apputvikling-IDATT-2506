package com.example.client

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

class MainActivity : AppCompatActivity() {
    private val IP: String = "10.0.2.2"
    private val PORT: Int = 12347
    private val name: String = "hasan"

    var server: Socket? = null
        private set
    var messages = mutableListOf<String>()
    var messagesSent = mutableListOf<String>()

    private lateinit var view: TextView
    private lateinit var text: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        view = findViewById(R.id.view)
        text = findViewById(R.id.message)
        setupServer()
    }

    fun setupServer() {
        CoroutineScope(Dispatchers.IO).launch {
            showMessage =  "Connecting..."
            try {
                server = Socket(IP, PORT)
                println("Connected1")
                listenToServer()
            } catch (e: IOException) {
                e.printStackTrace()
                showMessage = e.message
            }
        }
    }

    private fun listenToServer() {
        CoroutineScope(Dispatchers.IO).launch {
            server?.let {
                while (true) {
                    println("Connected2")
                    val reader = BufferedReader(InputStreamReader(it.getInputStream()))
                    val message = reader.readLine()
                    if (message !== null) {
                        showMessage = message
                        messages.add(message)
                    } else {
                        it.close()
                        break
                    }
                }
            }
        }
    }

    private var showMessage: String? = ""
        set(str) {
            MainScope().launch {
                val message: String = view.getText().toString()
                view.text = message + "\n" + str
            }
            field = str
        }

    fun onClickSend(view: View) {
        CoroutineScope(Dispatchers.IO).launch {
            var message = text.text.toString()
            sendMessage(message)
        }
    }

    private suspend fun sendMessage(message: String) = coroutineScope {
        CoroutineScope(Dispatchers.IO).launch {
            server?.let {
                val writer = PrintWriter(it.getOutputStream(), true)
                val msg = "$name: $message";
                writer.println(msg)
                messagesSent.add(msg)
                showMessage = msg
            }
        }
    }
}