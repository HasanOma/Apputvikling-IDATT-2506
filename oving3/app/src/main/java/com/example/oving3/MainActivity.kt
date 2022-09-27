package com.example.oving3

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {

    private var friends: ArrayList<Friend> = ArrayList<Friend>()
    private var spinOpt: Array<String> = arrayOf()
    private lateinit var listView: ListView
    private lateinit var adapterSpinner: Adapter
    private lateinit var adapterList: FriendView
    private  var pos: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        friends.add(Friend("Per",LocalDate.parse("12-01-1998", DateTimeFormatter.ofPattern("dd-MM-yyyy"))))
        friends.add(Friend("Bendik",LocalDate.parse("29-07-1998", DateTimeFormatter.ofPattern("dd-MM-yyyy"))))
        spinOpt = resources.getStringArray(R.array.spinnerOptions)
        spinner()
        satrtList()
    }

    private fun spinner() {
        adapterSpinner = ArrayAdapter(this, android.R.layout.simple_spinner_item, spinOpt)
        val spinner = findViewById<Spinner>(R.id.spinner)
        spinner.adapter = adapterSpinner as ArrayAdapter<String>
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                chosen: View,
                position: Int,
                id: Long
            ) {
                if(position == 0){
                    Log.e("tag","Options")
                }
                else if(position == 1){
                    openActivityForResult()
                }
                else{
                    exitProcess(-1)
                }
            }
            override fun onNothingSelected(arg0: AdapterView<*>?) {}
        }
    }

    private fun satrtList() {
        listView = findViewById<ListView>(R.id.listView)
        val listItems = arrayOfNulls<String>(friends.size)
        for ((i, item) in friends.withIndex()) {
            listItems[i] = item.name
        }
        adapterList = FriendView(this, friends)
        listView.adapter = adapterList
        listView.setOnItemClickListener { parent, view, position, id ->
            val element = adapterList.getItem(position)
            openActivityForResultEdit(position)
        }
    }

    private fun openActivityForResult(){
        val intent = Intent("AddFriend")
        startForResult.launch(intent)
    }

    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intent = result.data
            val friend = intent?.getSerializableExtra("friend") as Friend
            friends.add(friend)
            adapterList.notifyDataSetChanged()
        }
        else if (result.resultCode == Activity.RESULT_CANCELED){
            adapterList.notifyDataSetChanged()
            spinner()
        }
    }

    private fun openActivityForResultEdit(position:Int){
        val intent = Intent("EditFriend")
        intent.putExtra("friend", friends.get(position))
        intent.putExtra("pos",position)
        startForResultEdit.launch(intent)
    }

    private val startForResultEdit = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intent = result.data
            val friend = intent?.getSerializableExtra("friend") as Friend
            val pos = intent.getIntExtra("pos",pos)
            friends[pos].name = friend.name
            friends[pos].date = friend.date
            adapterList.notifyDataSetChanged()
        }
        else if (result.resultCode == Activity.RESULT_CANCELED){
            adapterList.notifyDataSetChanged()
            spinner()
        }
    }
}
