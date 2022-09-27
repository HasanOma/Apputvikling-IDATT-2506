package com.example.oving3

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class AddFriend : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_friend)
    }

    @SuppressLint("NewApi")
    fun onClickAddFriend(v: View?){
        val name: TextView = findViewById(R.id.editTextName)
        val dob : TextView = findViewById(R.id.editTextBirthday)

        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val date = LocalDate.parse(dob.text.toString(), formatter)

        val friend = Friend(name.text.toString(), date)
        setResult(RESULT_OK, Intent().putExtra("friend",friend))
        finish()
    }

    fun onClickBack(v: View?){
        finish()
    }
}