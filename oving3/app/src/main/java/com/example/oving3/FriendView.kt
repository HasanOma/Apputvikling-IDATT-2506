package com.example.oving3

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class FriendView (
    private val context: Context,
    private val dataSource: ArrayList<Friend>) : BaseAdapter() {

        private val inflater: LayoutInflater
                = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        override fun getCount(): Int {
            return dataSource.size
        }

        override fun getItem(position: Int): Any {
            return dataSource[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        @SuppressLint("ViewHolder")
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val rowView = inflater.inflate(R.layout.friend_component, parent, false)
            val titleTextView = rowView.findViewById(R.id.recipe_list_title) as TextView
            val subtitleTextView = rowView.findViewById(R.id.recipe_list_subtitle) as TextView
            val friend = getItem(position) as Friend
            titleTextView.text = friend.name
            subtitleTextView.text = friend.date.toString()
            return rowView
        }
}