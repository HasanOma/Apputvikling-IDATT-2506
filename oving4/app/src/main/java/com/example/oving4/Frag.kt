package com.example.oving4

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.ListFragment
import java.lang.ClassCastException
import android.content.res.TypedArray
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment

class Frag : ListFragment(){
    private var titles: Array<String> = arrayOf()
    private var titleListener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        titles = resources.getStringArray(R.array.movietitles)
        listAdapter = activity?.let {
            ArrayAdapter(it, android.R.layout.simple_list_item_1,
            android.R.id.text1, titles)
        }
    }

    override fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {
        super.onListItemClick(l, v, position, id)
        titleListener!!.onFragmentInteractionListener(position)
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteractionListener(pos: Int?)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        titleListener = try {
            activity as OnFragmentInteractionListener
        } catch (e: ClassCastException) {
            throw ClassCastException(
                "$activity FragmentListener not implemented"
            )
        }
    }

    override fun onDetach() {
        super.onDetach()
        titleListener = null
    }
}