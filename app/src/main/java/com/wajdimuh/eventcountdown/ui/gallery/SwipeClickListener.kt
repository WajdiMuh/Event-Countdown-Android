package com.wajdimuh.eventcountdown.ui.gallery

import com.wajdimuh.eventcountdown.ui.Event

interface SwipeClickListener {
    fun onDeleteSwipeClick(event: Event)
    fun onEditSwipeClick(event: Event)
    fun onItemSelect(event: Event)
}