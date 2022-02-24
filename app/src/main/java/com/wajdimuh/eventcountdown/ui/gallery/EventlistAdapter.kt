package com.wajdimuh.eventcountdown.ui.gallery

import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wajdimuh.eventcountdown.R
import com.wajdimuh.eventcountdown.ui.Event
import java.text.DateFormat
import java.text.SimpleDateFormat


class EventlistAdapter(context:Context):RecyclerView.Adapter<RecyclerView.ViewHolder>(),SwipeClickListener,addClickListener {
    var context:Context
    var events:MutableList<Event> = mutableListOf()
    val dateFormat: DateFormat = SimpleDateFormat("yyyy/MM/dd, h:mm a")
    init {
        this.context = context
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType == 0){
            val viewholder:EventViewHolder = EventViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.event,parent,false))
            viewholder.listener = this
            return viewholder
        }else{
            val viewholder:addViewHolder = addViewHolder(LayoutInflater.from(parent.context).inflate(
                R.layout.add,parent,false))
            viewholder.listener = this
            return viewholder
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(getItemViewType(position) == 0){
            val event = events[position]
            val data:EventViewHolder = holder as EventViewHolder
            data.eventdata = event
            data.title.text = event.title
            data.date.text = dateFormat.format(event.date)
        }
    }

    override fun getItemCount(): Int {
        return events.size + 1
    }

    fun updatedata(newevents: MutableList<Event>){
        events = newevents
        notifyDataSetChanged()
    }

    override fun onDeleteSwipeClick(event: Event) {
        val eventidx = events.indexOf(event)
        events.removeAt(eventidx)
        notifyItemRemoved(eventidx)
    }

    override fun onEditSwipeClick(event: Event) {
        Log.i("edit",event.toString())
    }

    override fun getItemViewType(position: Int): Int {
        if(position == events.size)
            return 1
        else
            return 0
    }

    override fun onAddClick() {
        val adddialog:AddEventDialog = AddEventDialog(context)
        adddialog.addlistener = {

        }
        adddialog.show()
    }

}