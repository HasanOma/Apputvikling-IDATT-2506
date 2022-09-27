package com.example.server

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.ServerSocket
import java.net.Socket

class MainActivity : AppCompatActivity() {
    private val PORT: Int = 12347
    private lateinit var view: TextView
    private var clients = mutableListOf<Socket>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        view = findViewById(R.id.textView)
        setupServer()
    }

    fun setupServer() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                ServerSocket(PORT).use { serverSocket: ServerSocket ->
                    println("IIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIPPPPPPPPPPPPPPPPPPPPP: " + serverSocket.inetAddress)
                    while (true) {
                        val socket = serverSocket.accept()
                        showMessage = "Client connected: $socket"
                        connect(socket)
                        println("coooonnnnnnneeeeeecccccccttttttteeeeeeddddddd")
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
                println("faileddddddddddddddddddddddddddddddddddddddddddddddddddd")
                showMessage = e.message
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

    private suspend fun connect(socket: Socket) =
        coroutineScope {
            CoroutineScope(Dispatchers.IO).launch {
                clients.add(socket)
                write(socket, "You Are Connected")
                read(socket)
                println("connected")
            }
        }

    private suspend fun read(socket: Socket) = coroutineScope {
        CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                val message = BufferedReader(InputStreamReader(socket.getInputStream())).readLine()

                if (message == null) {
                    showMessage = "Client disconnected"
                    clients.remove(socket)
                    socket.close()
                    break
                } else {
                    clients.filter { it !== socket }.forEach { write(it, message) }
                }
            }
        }
    }

    private suspend fun write(socket: Socket, message: String) = coroutineScope {
        CoroutineScope(Dispatchers.IO).launch {
            val writer = PrintWriter(socket.getOutputStream(), true)
            writer.println(message)
        }
    }
}