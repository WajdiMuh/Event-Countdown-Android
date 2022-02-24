package com.wajdimuh.eventcountdown.ui.gallery

import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wajdimuh.eventcountdown.R
import com.wajdimuh.eventcountdown.ui.Event

class EventViewHolder(event: View):RecyclerView.ViewHolder(event) {
    val title: TextView
    val date: TextView
    val deletebtn: TextView
    val editbtn: TextView
    lateinit var eventdata:Event
    var listener: SwipeClickListener? = null
    init {
        title = event.findViewById(R.id.eventtitle)
        date = event.findViewById(R.id.eventdate)
        deletebtn = event.findViewById(R.id.delete)
        editbtn = event.findViewById(R.id.edit)
        deletebtn.setOnClickListener{
            listener!!.onDeleteSwipeClick(eventdata)
        }
        editbtn.setOnClickListener{
            listener!!.onEditSwipeClick(eventdata)
        }
    }
}