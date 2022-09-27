package com.example.oving2

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class part2 : AppCompatActivity(){

    private var lim: Int = 10
    private var num2: Int = 5
    private var num: Int = 3

    private var answerView: EditText? = null
    private var limitView: EditText? = null
    private var textView: TextView? = null
    private var textView2: TextView? = null

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            when (result.resultCode) {
                RESULT_OK -> {
                    if (result.data != null) {

                        val generatedNumber1 = result.data!!.getIntExtra("int11", num)
                        val generatedNumber2 = result.data!!.getIntExtra("int12", num2)

                        num = generatedNumber1
                        num2 = generatedNumber2

                        textView?.text = "${getString(R.string.number)} 1: $num"
                        textView2?.text = "${getString(R.string.number)} 2: $num2"

                    }
                }
                else -> {
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_secondary)

        textView = findViewById<View>(R.id.textView2) as TextView
        textView2 = findViewById<View>(R.id.textView4) as TextView
        answerView = findViewById<View>(R.id.editTextNumber) as EditText
        limitView = findViewById<View>(R.id.editTextNumber3) as EditText

        textView?.text = "${getString(R.string.number)} 1: $num"
        textView2?.text = "${getString(R.string.number2)} 2: $num2"
        limitView?.setText("$lim")
    }

    fun toPartOne(v: View?){
        startActivity(Intent(this,part1::class.java))
    }

    fun addOnClick(v: View?) {
        val correctAnswer = num + num2
        val answer = Integer.parseInt(answerView?.text.toString())
        showResult(
            if (correctAnswer == answer) "${getString(R.string.correct)} $num + $num2 = $correctAnswer" else "${
                getString(
                    R.string.wrong_the_correct_answer_is
                )
            }: $num * $num2 = $correctAnswer"
        )
    }

    fun multiplyOnClick(v: View?) {
        val correctAnswer = num * num2
        val answer = Integer.parseInt(answerView?.text.toString())
        showResult(
            if (correctAnswer == answer) "${getString(R.string.correct)} $num * $num2 = $correctAnswer" else "${
                getString(
                    R.string.wrong_the_correct_answer_is
                )
            }: $num * $num2 = $correctAnswer"
        )
    }

    private fun showResult(message: String) {
        AlertDialog.Builder(this)
            .setTitle("Oving2, part 2")
            .setMessage(message)
            .setPositiveButton(getString(R.string.generate_random_number)) { _, _ ->
                run {
                    lim = Integer.parseInt(limitView?.text.toString())
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("limit", lim)
                    startForResult.launch(intent)
                }
            }
            .setNegativeButton(getString(R.string.try_again), null)
            .show()
    }
}