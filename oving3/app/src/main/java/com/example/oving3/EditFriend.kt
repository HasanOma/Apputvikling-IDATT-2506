package com.example.oving3

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class EditFriend : AppCompatActivity() {

    private lateinit var friend : Friend
    private var pos: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_friend)
        friend = intent?.getSerializableExtra("friend") as Friend
        pos = intent?.getIntExtra("pos",pos)!!
        displayInformation()
    }

    private fun displayInformation(){
        val name: TextView = findViewById(R.id.editTextName2)
        val date : TextView = findViewById(R.id.editTextBirthday2)
        name.text = friend.name
        date.text = friend.date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
    }

    @SuppressLint("NewApi")
    fun onClickEditFriend(v: View?){
        val name: TextView = findViewById(R.id.editTextName2)

        val dob : TextView = findViewById(R.id.editTextBirthday2)

        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val date = LocalDate.parse(dob.text.toString(), formatter)
        friend.name = name.text.toString()
        friend.date = date;
        setResult(RESULT_OK, Intent().putExtra("friend",friend).putExtra("pos",pos))
        finish()
    }

    fun onClickBack(v: View?){
        setResult(RESULT_CANCELED)
        finish()
    }
}