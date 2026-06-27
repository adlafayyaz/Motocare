package com.example.motocare.ui

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
import com.example.motocare.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object FormDialogHelper {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)

    fun showDatePicker(context: Context, currentValue: String, onPicked: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        runCatching {
            dateFormat.parse(currentValue)?.let { calendar.time = it }
        }
        DatePickerDialog(
            context,
            { _, year, month, day ->
                calendar.set(year, month, day)
                onPicked(dateFormat.format(calendar.time))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    fun showOptionPicker(
        context: Context,
        title: String,
        options: List<String>,
        onPicked: (String) -> Unit
    ) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val view = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundResource(R.drawable.bg_bottom_sheet_dark)
            setPadding(dp(context, 24), dp(context, 26), dp(context, 24), dp(context, 24))
        }
        view.addView(TextView(context).apply {
            text = title
            setTextColor(context.getColor(R.color.motocare_text))
            textSize = 20f
            setTypeface(typeface, android.graphics.Typeface.BOLD)
        })
        options.forEach { option ->
            view.addView(TextView(context).apply {
                text = option
                setTextColor(context.getColor(R.color.motocare_text))
                textSize = 16f
                gravity = Gravity.CENTER_VERTICAL
                setPadding(0, dp(context, 18), 0, dp(context, 18))
                setOnClickListener {
                    onPicked(option)
                    dialog.dismiss()
                }
            })
        }
        dialog.setContentView(view)
        dialog.show()
        dialog.window?.apply {
            setBackgroundDrawableResource(android.R.color.transparent)
            setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
            setGravity(Gravity.BOTTOM)
        }
    }

    private fun dp(context: Context, value: Int): Int {
        return (value * context.resources.displayMetrics.density).toInt()
    }
}
