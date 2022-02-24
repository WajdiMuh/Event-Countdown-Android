package com.wajdimuh.eventcountdown.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.wajdimuh.eventcountdown.databinding.FragmentGalleryBinding
import com.wajdimuh.eventcountdown.ui.Event
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
        val swiprerefresh: SwipeRefreshLayout = binding.swiperefresh
        val listevent: RecyclerView = binding.eventlist
        val eventlistAdapter: EventlistAdapter = EventlistAdapter(this.requireContext())
        listevent.layoutManager = LinearLayoutManager(container!!.context)
        listevent.adapter = eventlistAdapter
        listevent.addItemDecoration(DividerItemDecoration(this.context, LinearLayoutManager.VERTICAL))
        eventlistAdapter.updatedata(
            mutableListOf(
                Event(0,"hello", Date()),
                Event(1,"hello",Date()),
                Event(2,"hello",Date()),
                Event(5,"hello",Date())
            )
        )

        swiprerefresh.setOnRefreshListener {
            swiprerefresh.isRefreshing = false
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}