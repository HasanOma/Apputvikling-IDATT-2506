package com.example.oving2

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class part1 : AppCompatActivity() {
    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { res ->
            when (res.resultCode) {
                RESULT_OK -> {
                    if (res.data != null) {
                        val number = res.data!!.getIntExtra("int13", 42)
                        Toast.makeText(this, "Generated number: $number", Toast.LENGTH_SHORT)
                            .show()
                        val textView = findViewById<View>(R.id.gen) as TextView
                        textView.text = "Generated number: $number"
                    }
                }
                else -> {
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_generatenumber)
    }

    fun onClickRandomNumberActivity(v: View?) {
        startForResult.launch(Intent(this, MainActivity::class.java))
    }

    fun toPartTwo(v: View?){
        startActivity(Intent(this,part2::class.java))
    }
}