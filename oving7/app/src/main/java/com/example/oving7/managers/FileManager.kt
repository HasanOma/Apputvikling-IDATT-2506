package com.example.oving7.managers

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.example.oving7.R
import com.example.oving7.data.Movie
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.nio.charset.Charset

class FileManager(private val activity: AppCompatActivity) {

	private val filename: String = "movies.txt"

	private var dir: File = activity.filesDir
	private var file: File = File(dir, filename)
	private var externalDir: File? = activity.getExternalFilesDir(null)
	private var externalFile = File(externalDir, filename)


	fun readMovies(): JSONArray? {
		try {
			val obj = JSONObject(load())
			return obj.getJSONArray("movies")
		} catch (e: JSONException) {
			e.printStackTrace()
		}
		return null
	}

	private fun load(): String {
		val json: String?
		try {
			val input = activity.resources.openRawResource(R.raw.list_of_movies);
			val size = input.available()
			val buffer = ByteArray(size)
			val charset: Charset = Charsets.UTF_8
			input.read(buffer)
			input.close()
			json = String(buffer, charset)
		}
		catch (ex: IOException) {
			ex.printStackTrace()
			return ""
		}
		return json
	}

	fun write(movies:MutableList<Movie>){
		try {
			val fileOutputStream: FileOutputStream = activity.openFileOutput("moviefile.txt", Context.MODE_PRIVATE)
			val outputWriter = OutputStreamWriter(fileOutputStream)
			outputWriter.write(movies.toString())
			outputWriter.close()
		}
		catch (e: Exception) {
			e.printStackTrace()
		}
	}

	private fun readFileFromResFolder(fileId: Int): String {
		val content = StringBuffer("")
		try {
			val inputStream: InputStream = activity.resources.openRawResource(fileId)
			val reader = BufferedReader(InputStreamReader(inputStream)).use { reader ->
				var line = reader.readLine()
				while (line != null) {
					content.append(line)
					content.append("\n")
					line = reader.readLine()
				}
			}
		} catch (e: IOException) {
			e.printStackTrace()
		}
		return content.toString()
	}
}
