package com.wajdimuh.eventcountdown.ui.gallery

import android.app.Dialog
import android.app.DialogFragment
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TimePicker
import androidx.annotation.RequiresApi
import com.wajdimuh.eventcountdown.R
import java.util.*

class AddEventDialog(context: Context) : Dialog(context) {
    lateinit var title:EditText
    lateinit var cancel:Button
    lateinit var add:Button
    lateinit var date:DatePicker
    lateinit var time:TimePicker
    var addlistener: (result: Pair<String,Date>) -> Unit = { _ -> }
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.addevent)
        title = findViewById(R.id.title)
        cancel = findViewById(R.id.cancel)
        add = findViewById(R.id.add)
        date = findViewById(R.id.datepicker)
        time = findViewById(R.id.timepicker)
        cancel.setOnClickListener{
            cancel()
        }
        add.setOnClickListener{
            addlistener.invoke(Pair(title.text.toString(),getDate(date,time)))
            dismiss()
        }
    }
    @RequiresApi(Build.VERSION_CODES.M)
    fun getDate(date:DatePicker, time:TimePicker): Date {
        val calendar = Calendar.getInstance()
        calendar.set(date.year, date.month, date.dayOfMonth,time.hour,time.minute)
        return calendar.time
    }
}