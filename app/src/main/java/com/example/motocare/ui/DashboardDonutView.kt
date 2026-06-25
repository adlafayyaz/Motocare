package com.example.motocare.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

class DashboardDonutView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val ringPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.BUTT
    }
    private val bounds = RectF()
    private var segments: List<Segment> = emptyList()

    fun setData(
        fuel: Int,
        service: Int,
        oil: Int,
        tax: Int
    ) {
        segments = listOf(
            Segment(service, Color.parseColor("#FFCB25")),
            Segment(oil, Color.parseColor("#6045F4")),
            Segment(tax, Color.parseColor("#E936A7")),
            Segment(fuel, Color.parseColor("#FF8125"))
        ).filter { it.value > 0 }
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val stroke = width.coerceAtMost(height) * 0.17f
        ringPaint.strokeWidth = stroke
        val inset = stroke / 2f + 2f
        bounds.set(inset, inset, width - inset, height - inset)

        if (segments.isEmpty()) {
            ringPaint.color = Color.parseColor("#333541")
            canvas.drawArc(bounds, -90f, 360f, false, ringPaint)
            return
        }

        val total = segments.sumOf { it.value }.toFloat()
        var start = -90f
        segments.forEach { segment ->
            val sweep = 360f * segment.value / total
            ringPaint.color = segment.color
            canvas.drawArc(bounds, start, sweep, false, ringPaint)
            start += sweep
        }
    }

    private data class Segment(val value: Int, val color: Int)
}
