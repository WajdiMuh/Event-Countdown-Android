package com.wajdimuh.eventcountdown.ui.gallery

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.wajdimuh.eventcountdown.R
import com.wajdimuh.eventcountdown.ui.Event
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.joda.time.DateTime
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener
import java.text.DateFormat
import java.text.SimpleDateFormat


class EventlistAdapter(context: FragmentActivity,loading: View):RecyclerView.Adapter<RecyclerView.ViewHolder>(),SwipeClickListener,addClickListener {
    var context:FragmentActivity
    var loading:View
    var events:MutableList<Event> = mutableListOf()
    val dateFormat: DateFormat = SimpleDateFormat("dd/MM/yyyy, h:mm a")
    val dateFormatjson: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
    init {
        this.context = context
        this.loading = loading
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
            data.date.text = dateFormat.format(event.date.toDate())
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
        loading.visibility = View.VISIBLE
        Fuel.delete("deleteevent/${event.id}").response { request, response, result ->
            val (bytes) = result
            if (bytes != null) {
                val events: MutableList<Event> = mutableListOf()
                val jsonArray = JSONTokener(String(bytes)).nextValue() as JSONArray
                for (i in 0 until jsonArray.length()) {
                    val event = jsonArray.getJSONObject(i)
                    events.add(Event(event.getInt("id"),event.getString("title"),DateTime.parse(event.getString("date"))))
                }
                updatedata(events)
                loading.visibility = View.GONE
            }
        }
    }

    override fun onEditSwipeClick(event: Event) {
        val editdialog:AddEventDialog = AddEventDialog(context, Pair(event.title, event.date))
        editdialog.activitycontext = context
        editdialog.listener = {
            loading.visibility = View.VISIBLE
            val editedevent = JSONObject()
            editedevent.put("title", it.first)
            editedevent.put("date", dateFormatjson.format(it.second.toDate()))
            Fuel.put("editevent/${event.id}").jsonBody(editedevent.toString()).response { request, response, result ->
                val (bytes) = result
                if (bytes != null) {
                    val events: MutableList<Event> = mutableListOf()
                    val jsonArray = JSONTokener(String(bytes)).nextValue() as JSONArray
                    for (i in 0 until jsonArray.length()) {
                        val event = jsonArray.getJSONObject(i)
                        events.add(Event(event.getInt("id"),event.getString("title"),DateTime.parse(event.getString("date"))))
                    }
                    updatedata(events)
                    loading.visibility = View.GONE
                }
            }
        }
        editdialog.show()
    }

    override fun onItemSelect(event: Event) {
        val bundle = Bundle()
        bundle.putSerializable("event", Json.encodeToString(event))
        Navigation.findNavController(loading).navigate(R.id.action_nav_gallery_to_nav_home,bundle)
    }

    override fun getItemViewType(position: Int): Int {
        if(position == events.size)
            return 1
        else
            return 0
    }

    override fun onAddClick() {
        val adddialog:AddEventDialog = AddEventDialog(context)
        adddialog.activitycontext = context
        adddialog.listener = {
            loading.visibility = View.VISIBLE
            val addedevent = JSONObject()
            addedevent.put("title", it.first)
            addedevent.put("date", dateFormatjson.format(it.second.toDate()))
            Fuel.post("addevent").jsonBody(addedevent.toString()).response { request, response, result ->
                val (bytes) = result
                if (bytes != null) {
                    val events: MutableList<Event> = mutableListOf()
                    val jsonArray = JSONTokener(String(bytes)).nextValue() as JSONArray
                    for (i in 0 until jsonArray.length()) {
                        val event = jsonArray.getJSONObject(i)
                        events.add(Event(event.getInt("id"),event.getString("title"),DateTime.parse(event.getString("date"))))
                    }
                    updatedata(events)
                    loading.visibility = View.GONE
                }
            }
        }
        adddialog.show()
    }

}