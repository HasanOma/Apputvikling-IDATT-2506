package com.example.oving4

import android.content.res.TypedArray
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment

class Frag2 : Fragment() {

    private var pictures: TypedArray? = null
    private var descriptions: Array<String> = arrayOf()
    private var imageIDs: IntArray = intArrayOf()
    private var posDisplayed: Int = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        descriptions  = resources.getStringArray(R.array.moviedescriptions)
        pictures = resources.obtainTypedArray(R.array.pictures)
        imageIDs = IntArray(pictures!!.length())
        for (i in 0 until pictures!!.length()) imageIDs[i] = pictures!!.peekValue(i).resourceId
        return inflater.inflate(R.layout.frag2, container, false);
    }

    fun setText(pos: Int?) {
        requireView().findViewById<TextView>(R.id.textView).text = descriptions[pos!!]
        requireView().findViewById<ImageView>(R.id.imageView).setImageResource(imageIDs[pos])
        posDisplayed =  pos
    }

    fun onClickBack(){
        if(posDisplayed == 0) posDisplayed = imageIDs.size-1 else posDisplayed -=1
        setText(posDisplayed)
    }
    fun onClickNext(){
        if(posDisplayed == imageIDs.size-1) posDisplayed = 0 else posDisplayed +=1
        setText(posDisplayed)
    }
}
