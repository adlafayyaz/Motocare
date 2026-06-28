package com.example.motocare.ui

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.text.InputType
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
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
                    dialog.dismiss()
                    if (option.equals("Lainnya", true)) {
                        showTextInput(context, title, onPicked)
                    } else {
                        onPicked(option)
                    }
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

    private fun showTextInput(
        context: Context,
        title: String,
        onPicked: (String) -> Unit
    ) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val input = EditText(context).apply {
            setBackgroundResource(R.drawable.bg_input_dark)
            setTextColor(context.getColor(R.color.motocare_text))
            setHintTextColor(context.getColor(R.color.motocare_muted))
            hint = title
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
            setPadding(dp(context, 18), 0, dp(context, 18), 0)
            minHeight = dp(context, 56)
        }
        val view = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundResource(R.drawable.bg_bottom_sheet_dark)
            setPadding(dp(context, 24), dp(context, 26), dp(context, 24), dp(context, 24))
            addView(TextView(context).apply {
                text = title
                setTextColor(context.getColor(R.color.motocare_text))
                textSize = 20f
                setTypeface(typeface, android.graphics.Typeface.BOLD)
            })
            addView(input, LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp(context, 56)
            ).apply {
                topMargin = dp(context, 18)
            })
            addView(Button(context).apply {
                text = context.getString(R.string.save)
                setTextColor(context.getColor(R.color.motocare_background))
                setBackgroundResource(R.drawable.bg_segment_yellow)
                isAllCaps = false
                setOnClickListener {
                    val value = input.text.toString().trim()
                    if (value.isNotEmpty()) {
                        onPicked(value)
                        dialog.dismiss()
                    } else {
                        input.error = context.getString(R.string.error_field_required)
                    }
                }
            }, LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp(context, 52)
            ).apply {
                topMargin = dp(context, 18)
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
