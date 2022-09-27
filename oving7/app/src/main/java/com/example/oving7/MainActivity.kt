package com.example.oving7

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.oving7.data.Movie
import com.example.oving7.databinding.MyLayoutBinding
import com.example.oving7.managers.FileManager
import com.example.oving7.service.Database
import org.json.JSONArray
import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    private lateinit var db: Database
    private lateinit var fileManager: FileManager
    private lateinit var layout: MyLayoutBinding
    private lateinit var movies: MutableList<Movie>
    private lateinit var title: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layout = MyLayoutBinding.inflate(layoutInflater)
        setContentView(layout.root)

        db = Database(this)
        fileManager = FileManager(this)

        val moviesArray = fileManager.readMovies()
        db.insertFromJson(moviesArray)
        saveMovies(moviesArray)
        fileManager.write(movies)
        results(db.allMovies, 0)
        title = findViewById<View>(R.id.info) as TextView
    }

    private fun saveMovies(moviesJSON: JSONArray?){
        movies =  ArrayList()
        if (moviesJSON != null) {
            for (i in 0 until moviesJSON.length()) {
                val userDetail = moviesJSON.getJSONObject(i)
                movies.add(Movie(userDetail.getString("title"),userDetail.getString("director"),userDetail.getString("actor").split(",").toTypedArray()))
            }
        }
    }

    private fun results(list: ArrayList<String>, i: Int) {
        val res = StringBuffer("")
        var newList = list
        if(list.size > 1){
            newList = list.distinct() as ArrayList<String>
        }
        for (s in newList) res.append("$s\n\n")
        when(i){
            1             -> title.text = "All Movies"
            2             -> title.text = "All Actors"
            3             -> title.text = "All Actors In Movies"
            4             -> title.text = "All Movies By Quentin Tarantino"
            else          -> layout.result.text = res
        }
        layout.result.text = res
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.settings, menu)
        menu.add(0, 1, 0, "All Movies")
        menu.add(0, 2, 0, "All Actors")
        menu.add(0, 3, 0, "All Movies And Actors")
        menu.add(0, 4, 0, "Movies By Quentin Tarantino")
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings -> resultActivity()
            1             -> results(db.allMovies,1)
            2             -> results(db.allActors,2)
            3             -> results(db.moviesAndActors,3)
            4             -> results(db.moviesByDirector,4)
            else          -> return false
        }
        return super.onOptionsItemSelected(item)
    }

    private fun resultActivity(){
        val intent = Intent("com.example.oving7.Settings")
        startResultActivity.launch(intent)
    }
    private val startResultActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intent = result.data
            val c = intent?.getStringExtra("colors")
            var lay = findViewById<ConstraintLayout>(R.id.constrainLayout)
            lay.setBackgroundColor(Color.parseColor(c))
        }
    }
}