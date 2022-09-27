package com.example.oving7.managers

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

open class DBManager(context: Context) :
	SQLiteOpenHelper(context, DB_NAME, null, DATABASE_VERSION) {

	companion object {
		const val DB_NAME = "MovieDatabase"
		const val DATABASE_VERSION = 1

		const val ID = "_id"

		const val TABLE_MOVIE = "MOVIE"
		const val TITLE = "TITLE"
		const val DIRECTOR = "DIRECTOR"

		const val TABLE_ACTOR = "ACTOR"
		const val NAME = "name"

		const val TABLE_MOVIE_ACTOR = "MOVIE_ACTOR"
		const val MOVIE_ID = "MOVIE_ID"
		const val ACTOR_ID = "ACTOR_ID"

		val JOIN_MOVIE_ACTOR = arrayOf(
			"$TABLE_MOVIE.$ID=$TABLE_MOVIE_ACTOR.$MOVIE_ID",
			"$TABLE_ACTOR.$ID=$TABLE_MOVIE_ACTOR.$ACTOR_ID"
		)
	}

	override fun onCreate(db: SQLiteDatabase) {
		db.execSQL(
			"""create table $TABLE_MOVIE (
						$ID integer primary key autoincrement, 
						$TITLE text unique not null,
						$DIRECTOR text
						);"""
		)
		db.execSQL(
			"""create table $TABLE_ACTOR (
						$ID integer primary key autoincrement, 
						$NAME text unique not null
						);"""
		)
		db.execSQL(
			"""create table $TABLE_MOVIE_ACTOR (
						$ID integer primary key autoincrement, 
						$ACTOR_ID numeric, 
						$MOVIE_ID numeric,
						FOREIGN KEY($MOVIE_ID) REFERENCES $TABLE_MOVIE($ID), 
						FOREIGN KEY($ACTOR_ID) REFERENCES $TABLE_ACTOR($ID)
						);"""
		)
	}

	override fun onUpgrade(db: SQLiteDatabase, arg1: Int, arg2: Int) {
		db.execSQL("DROP TABLE IF EXISTS $TABLE_MOVIE_ACTOR")
		db.execSQL("DROP TABLE IF EXISTS $TABLE_ACTOR")
		db.execSQL("DROP TABLE IF EXISTS $TABLE_MOVIE")
		onCreate(db)
	}

	fun insert(movie: String, director:String,actor: String,actor2: String) {
		writableDatabase.use { database ->
			val movieId = insertValueIfNotExistsMovie(database,movie, director)
			println(movieId)
			val actorId = insertValueIfNotExists(database, TABLE_ACTOR, NAME, actor)
			linkActorMovie(database, movieId, actorId)
			val actor2Id = insertValueIfNotExists(database, TABLE_ACTOR, NAME, actor2)
			linkActorMovie(database, movieId, actor2Id)
		}
	}

	private fun insertValueIfNotExistsMovie(database: SQLiteDatabase, title: String, director: String): Long {
		query(database, TABLE_MOVIE, arrayOf(ID, TITLE), "$TITLE='$title'").use { cursor ->
			return if (cursor.count != 0) {
				cursor.moveToFirst()
				cursor.getLong(0)
			} else {
				insertValueMovie(database,title, director)
			}
		}
	}

	private fun insertValueMovie(database: SQLiteDatabase, title: String, director:String): Long {
		val values = ContentValues()
		values.put(TITLE, title.trim())
		values.put(DIRECTOR,director)
		return database.insert(TABLE_MOVIE,null,values)
	}

	private fun insertValueIfNotExists(db: SQLiteDatabase, table: String, field: String, value: String): Long {
		query(db, table, arrayOf(ID, field), "$field='$value'").use { cursor ->
			return if (cursor.count != 0) {
				cursor.moveToFirst()
				cursor.getLong(0)
			} else {
				insertValue(db, table, field, value)
			}
		}
	}

	private fun insertValue(database: SQLiteDatabase, table: String, field: String, value: String): Long {
		val values = ContentValues()
		values.put(field, value.trim())
		return database.insert(table,null,values)
	}

	private fun linkActorMovie(database: SQLiteDatabase, movieId: Long, actorId: Long) {
		val values = ContentValues()
		values.put(MOVIE_ID, movieId)
		values.put(ACTOR_ID, actorId)
		database.insert(TABLE_MOVIE_ACTOR, null, values)
	}

	fun doQuery(table: String, columns: Array<String>, selection: String? = null):
			ArrayList<String> {
		assert(columns.isNotEmpty())
		readableDatabase.use { database ->
			query(database, table, columns, selection).use { cursor ->
				return fromCursor(cursor, columns.size)
			}
		}
	}

	fun rawQuery(
		select: Array<String>, from: Array<String>, join: Array<String>, where: String? = null
	): ArrayList<String> {

		val query = StringBuilder("SELECT ")
		for ((i, field) in select.withIndex()) {
			query.append(field)
			if (i != select.lastIndex) query.append(", ")
		}

		query.append(" FROM ")
		for ((i, table) in from.withIndex()) {
			query.append(table)
			if (i != from.lastIndex) query.append(", ")
		}

		query.append(" WHERE ")
		if (join.isNotEmpty()) {
			for ((i, link) in join.withIndex()) {
				query.append(link)
				if (i != join.lastIndex) query.append(" and ")
			}
		}

		if (where != null) {
			if (join.isNotEmpty()) query.append(" and $where")
			else query.append(" $where")
		}

		readableDatabase.use { db ->
			db.rawQuery("$query;", null).use { cursor ->
				return fromCursor(cursor, select.size)
			}
		}
	}

	fun fromCursor(cursor: Cursor, numberOfColumns: Int): ArrayList<String> {
		val result = ArrayList<String>()
		cursor.moveToFirst()
		while (!cursor.isAfterLast) {
			val item = StringBuilder("")
			for (i in 0 until numberOfColumns) {
				item.append("${cursor.getString(i)} ")
			}
			result.add("$item")
			cursor.moveToNext()
		}
		return result
	}

	private fun query(
		database: SQLiteDatabase, table: String, columns: Array<String>, selection: String?
	): Cursor {
		return database.query(table, columns, selection, null, null, null, null, null)
	}
}
