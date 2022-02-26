package com.wajdimuh.eventcountdown.ui.home

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.github.kittinunf.fuel.Fuel
import com.wajdimuh.eventcountdown.databinding.FragmentHomeBinding
import com.wajdimuh.eventcountdown.ui.Event
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.joda.time.*
import org.json.JSONObject
import org.json.JSONTokener
import java.text.DateFormat
import java.text.SimpleDateFormat

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var selectedevent:Event? = null
    private var carddur: TextView? = null

    private val ha = Handler()
    private val dateFormat: DateFormat = SimpleDateFormat("dd/MM/yyyy, h:mm a")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        if(arguments != null){
            selectedevent = Json.decodeFromString<Event>(requireArguments().getSerializable("event").toString())
        }

        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val loading: View = binding.loading.root
        val cardtitle: TextView = binding.eventtitle
        val carddate: TextView = binding.date
        carddur = binding.dur
        loading.visibility = View.VISIBLE
        if(arguments == null) {
            Fuel.get("getlatestevent").response { request, response, result ->
                val (bytes) = result
                if (bytes != null) {
                    val jsonObject = JSONTokener(String(bytes)).nextValue() as JSONObject
                    selectedevent = Event(
                        jsonObject.getInt("id"),
                        jsonObject.getString("title"),
                        DateTime.parse(jsonObject.getString("date"))
                    )
                    cardtitle.text = selectedevent!!.title
                    carddate.text = dateFormat.format(selectedevent!!.date.toDate())
                    ha.postDelayed(object : Runnable {
                        override fun run() {
                            recalculate()
                            ha.postDelayed(this, 1000)
                        }
                    }, 0)
                    loading.visibility = View.GONE
                }
            }
        }else{
            loading.visibility = View.GONE
            cardtitle.text = selectedevent!!.title
            carddate.text = dateFormat.format(selectedevent!!.date.toDate())
            ha.postDelayed(object : Runnable {
                override fun run() {
                    recalculate()
                    ha.postDelayed(this, 1000)
                }
            }, 0)
        }

        val swiprerefresh: SwipeRefreshLayout = binding.swiperefresh
        swiprerefresh.setOnRefreshListener {
            if(arguments == null) {
                Fuel.get("getlatestevent").response { request, response, result ->
                    val (bytes) = result
                    if (bytes != null) {
                        val jsonObject = JSONTokener(String(bytes)).nextValue() as JSONObject
                        selectedevent = Event(
                            jsonObject.get("id") as Int,
                            jsonObject.get("title") as String,
                            DateTime.parse(jsonObject.get("date") as String)
                        )
                        cardtitle.text = selectedevent!!.title
                        carddate.text = dateFormat.format(selectedevent!!.date.toDate())
                        recalculate()
                        swiprerefresh.isRefreshing = false
                    }
                }
            }else{
                cardtitle.text = selectedevent!!.title
                carddate.text = dateFormat.format(selectedevent!!.date.toDate())
                swiprerefresh.isRefreshing = false
            }
        }


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onPause() {
        super.onPause()
        ha.removeCallbacksAndMessages(null)
    }

    fun recalculate(){
        val d = Period(DateTime(),selectedevent!!.date, PeriodType.yearMonthDayTime())
        carddur!!.text =  "Years: ${d.years}, Months: ${d.months}, Days: ${d.days}, Hours: ${d.hours}, Minutes: ${d.minutes}, Seconds: ${d.seconds}"
    }
}