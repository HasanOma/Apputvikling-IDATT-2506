package com.example.oving7.service

import android.content.Context
import com.example.oving7.managers.DBManager
import org.json.JSONArray

class Database(context: Context) : DBManager(context) {

	fun insertFromJson(movies: JSONArray?){
		if (movies != null) {
			for (i in 0 until movies.length()) {
				val userDetail = movies.getJSONObject(i)
				this.insert(
					userDetail.getString("title"),
					userDetail.getString("director"),
					userDetail.getString("actor").split(",")[0],
					userDetail.getString("actor").split(",")[1]
				)
			}
		}
	}

	val allMovies: ArrayList<String>
		get() = doQuery(TABLE_MOVIE, arrayOf(ID,TITLE))

	val allActors: ArrayList<String>
		get() = doQuery(TABLE_ACTOR, arrayOf(ID, NAME), null)

	val moviesAndActors: ArrayList<String>
		get() {
			val select = arrayOf("$TABLE_ACTOR.$NAME", "$TABLE_MOVIE.$TITLE")
			val from = arrayOf(TABLE_MOVIE, TABLE_ACTOR, TABLE_MOVIE_ACTOR)
			val join = JOIN_MOVIE_ACTOR
			return rawQuery(select, from, join)
		}

	val moviesByDirector: ArrayList<String>
		get() = doQuery(TABLE_MOVIE,arrayOf(TITLE) ,"DIRECTOR = 'Quentin Tarantino'")

}
