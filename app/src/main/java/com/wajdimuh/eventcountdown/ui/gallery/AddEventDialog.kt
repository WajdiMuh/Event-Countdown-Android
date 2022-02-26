package com.wajdimuh.eventcountdown.ui.gallery

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.fragment.app.FragmentActivity
import com.mohamedabulgasem.datetimepicker.DateTimePicker
import com.wajdimuh.eventcountdown.R
import org.joda.time.DateTime
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

class AddEventDialog(context: Context) : Dialog(context) {
    lateinit var title:EditText
    lateinit var date:EditText
    lateinit var cancel:Button
    lateinit var add:Button
    lateinit var dateval:DateTime
    var activitycontext:FragmentActivity? = null
    var data: Pair<String,DateTime>? = null
    var listener: (result: Pair<String,DateTime>) -> Unit = { _ -> }
    val dateFormat: DateFormat = SimpleDateFormat("dd/MM/yyyy, h:mm a")
    var dateTimePicker: DateTimePicker? = null

    constructor(context: FragmentActivity,data:Pair<String,DateTime>) : this(context) {
        this.data = data
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.addevent)
        date = findViewById(R.id.date)
        dateTimePicker = DateTimePicker.Builder(activitycontext!!)
            .onDateTimeSetListener { year, month, dayOfMonth, hourOfDay, minute ->
                Log.i("da",DateTime(year, month, dayOfMonth, hourOfDay, minute).toString())
                if(DateTime(year, month + 1, dayOfMonth, hourOfDay, minute).isAfterNow) {
                    dateval = DateTime(year, month + 1, dayOfMonth, hourOfDay, minute)
                    date.setText(dateFormat.format(dateval.toDate()))
                }else{
                    Toast.makeText(activitycontext!!.applicationContext,
                        "Date set in the past",
                        Toast.LENGTH_SHORT).show()
                }
            }
            .is24HourView(false)
            .initialValues(
                initialHour = DateTime.now().hourOfDay,
                initialMinute = DateTime.now().minuteOfHour
            )
            .minDate(System.currentTimeMillis())
            .build()
        title = findViewById(R.id.title)
        cancel = findViewById(R.id.cancel)
        add = findViewById(R.id.add)
        cancel.setOnClickListener{
            cancel()
        }
        add.setOnClickListener{
            listener.invoke(Pair(title.text.toString(), dateval))
            dismiss()
        }
        date.setOnTouchListener { _, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    title.clearFocus()
                    dateTimePicker!!.show()
                }
            }
            true
        }
        if(data != null){
            title.setText(data!!.first)
            dateval = data!!.second
            date.setText(dateFormat.format(dateval.toDate()))
            add.setText("Edit")
        }else{
            dateval = DateTime().plusDays(1)
            date.setText(dateFormat.format(dateval.toDate()))
        }
    }
}