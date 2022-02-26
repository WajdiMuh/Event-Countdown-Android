package com.wajdimuh.eventcountdown.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.github.kittinunf.fuel.Fuel
import com.wajdimuh.eventcountdown.R
import com.wajdimuh.eventcountdown.databinding.FragmentGalleryBinding
import com.wajdimuh.eventcountdown.ui.Event
import org.joda.time.DateTime
import org.json.JSONArray
import org.json.JSONTokener
import java.util.*


class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val galleryViewModel =
            ViewModelProvider(this).get(GalleryViewModel::class.java)

        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val loading: View = binding.loading.root
        val swiprerefresh: SwipeRefreshLayout = binding.swiperefresh
        val listevent: RecyclerView = binding.eventlist
        loading.visibility = View.VISIBLE
        val eventlistAdapter: EventlistAdapter = EventlistAdapter(this.context as FragmentActivity,loading)
        listevent.layoutManager = LinearLayoutManager(container!!.context)
        listevent.adapter = eventlistAdapter
        listevent.addItemDecoration(DividerItemDecoration(this.context, LinearLayoutManager.VERTICAL))

        Fuel.get("getallevents").response { request, response, result ->
            val (bytes) = result
            if (bytes != null) {
                val events: MutableList<Event> = mutableListOf()
                val jsonArray = JSONTokener(String(bytes)).nextValue() as JSONArray
                for (i in 0 until jsonArray.length()) {
                    val event = jsonArray.getJSONObject(i)
                    events.add(Event(event.getInt("id"),event.getString("title"),DateTime.parse(event.getString("date"))))
                }
                eventlistAdapter.updatedata(events)
                loading.visibility = View.GONE
            }
        }


        swiprerefresh.setOnRefreshListener {
            Fuel.get("getallevents").response { request, response, result ->
                val (bytes) = result
                if (bytes != null) {
                    val events: MutableList<Event> = mutableListOf()
                    val jsonArray = JSONTokener(String(bytes)).nextValue() as JSONArray
                    for (i in 0 until jsonArray.length()) {
                        val event = jsonArray.getJSONObject(i)
                        events.add(Event(event.getInt("id"),event.getString("title"),DateTime.parse(event.getString("date"))))
                    }
                    eventlistAdapter.updatedata(events)
                    swiprerefresh.isRefreshing = false
                }
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}