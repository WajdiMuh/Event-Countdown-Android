package com.wajdimuh.eventcountdown.ui.gallery

import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wajdimuh.eventcountdown.R
import com.wajdimuh.eventcountdown.ui.Event

class addViewHolder(add: View):RecyclerView.ViewHolder(add) {
    var listener: addClickListener? = null
    init {
        add.setOnClickListener{
            listener!!.onAddClick()
        }
    }
}