package com.example.oving4

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem

class MainActivity : AppCompatActivity(), Frag.OnFragmentInteractionListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_portrait)
        setOrientation(resources.configuration)
    }

    private fun setOrientation(config: Configuration) {
        val isPortrait = config.orientation == Configuration.ORIENTATION_PORTRAIT

        if (isPortrait)
            setContentView(R.layout.activity_main_portrait); // it will use .xml from /res/layout
        else
            setContentView(R.layout.activity_main_landscape); // it will use xml from /res/layout-land
    }

    override fun onFragmentInteractionListener(pos: Int?) {
        val frag2 = supportFragmentManager.findFragmentById(R.id.frag2) as Frag2
        frag2.setText(pos)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_back -> onClickBackButton()
            R.id.menu_next -> onClickNextButton()
            R.id.menu_exit -> finish()
            else -> return false
        }
        return true
    }

    private fun onClickBackButton(){
        val fragment2 = supportFragmentManager.findFragmentById(R.id.frag2) as Frag2
        fragment2.onClickBack()
    }

    private fun onClickNextButton(){
        val fragment2 = supportFragmentManager.findFragmentById(R.id.frag2) as Frag2
        fragment2.onClickNext()
    }
}