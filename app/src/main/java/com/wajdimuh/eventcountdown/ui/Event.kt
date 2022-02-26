package com.wajdimuh.eventcountdown.ui
import com.wajdimuh.eventcountdown.DateTimeSerializer
import kotlinx.serialization.Serializable
import org.joda.time.DateTime

@Serializable
data class Event(
        val id:Int,
        val title:String,
        @Serializable(with = DateTimeSerializer::class)
        val date: DateTime
    )
