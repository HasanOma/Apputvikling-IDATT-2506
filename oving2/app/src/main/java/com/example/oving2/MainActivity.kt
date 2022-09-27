package com.example.oving2

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(){
    private var lim = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        lim = intent.getIntExtra("limit", lim)

    }

    fun generateRandomInt(v: View) {
        val intent = Intent()
        for (i in 0..10) {
            intent.putExtra("int1$i", (0..lim).random())
        }
        setResult(RESULT_OK, intent)
        finish()
    }
}