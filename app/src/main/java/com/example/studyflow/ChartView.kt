package com.example.studyflow

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.res.use

class ChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs) {

    private var PADDING = 50f
    private val NUMBERS_SINGS_X_AXIS = 5
    private val NUMBERS_SINGS_Y_AXIS = 5


    private var lineColor: Int = ContextCompat.getColor(context, R.color.black)
        set(value) {
            field = value
            strokePaint.color = value
            invalidate()
        }

    private val strokePaint = Paint().apply {
        this.style = Paint.Style.STROKE
        this.strokeWidth = 1f
        this.isAntiAlias = true
        this.color = lineColor
    }

    private val textPaint: TextPaint = TextPaint().apply {
        this.color = ContextCompat.getColor(context, R.color.black)
        this.typeface = Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL)
        this.textSize = 24f
        isAntiAlias = true
    }

    var markers: MutableList<Marker> =
        mutableListOf(
        )

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.ChartView,
            0,
            0
        ).use {
            lineColor = it.getColor(R.styleable.ChartView_lineColor, 0)
        }
    }

    fun addMarker(marker: Marker) {
        if (markers.isNotEmpty())
            if (marker.x < markers.last().x) throw IllegalStateException() else
                markers.add(marker)
        else
            markers.add(marker)
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        PADDING = width * 0.07f
        canvas?.let {
            drawLines(it)
            drawSignatures(it)
            drawAxes(it)
        }
    }

    private fun drawAxes(canvas: Canvas) {
        canvas.drawLine(
            PADDING,
            height - (height - PADDING),
            PADDING,
            height - PADDING,
            strokePaint
        )
        canvas.drawLine(
            width - (width - PADDING),
            height - PADDING,
            width - PADDING,
            height - PADDING,
            strokePaint
        )
    }

    private fun drawSignatures(canvas: Canvas) {
        val maxX = getMaxValueX()
        val minX = getMinValueX()

        val maxY = getMaxValueY()
        val minY = getMinValueY()

        val stepX = (maxX) / NUMBERS_SINGS_X_AXIS

        val stepY = (maxY) / NUMBERS_SINGS_Y_AXIS

        for (i in 0..maxX) {
            canvas.drawText(
                "${i * stepX}",
                getPositionX(Marker(i * stepX, 0)),
                height - PADDING / 2,
                textPaint
            )
        }

        for (i in 0..maxY) {
            canvas.drawText(
                "${i * stepY}",
                PADDING / 2,
                getPositionY(Marker(0, i * stepY)),
                textPaint
            )
        }
    }

    private fun drawLines(canvas: Canvas) {
        markers.forEachIndexed { index, marker ->
            val newX = getPositionX(markers[index])
            val newY = getPositionY(markers[index])
            if (index > 0) {
                canvas.drawLine(
                    getPositionX(markers[index - 1]),
                    getPositionY(markers[index - 1]),
                    newX,
                    newY,
                    strokePaint
                )
            }
            canvas.drawText(marker.y.toString(), newX, newY + PADDING / 2, textPaint)
        }
    }

    private fun getPositionX(marker: Marker): Float {
        return ((marker.x - getMinValueX()) * getChartSizeSide(width)) / (getMaxValueX() - getMinValueX()) + PADDING
    }

    private fun getPositionY(marker: Marker): Float {
        val pos =
            ((marker.y - getMinValueY()) * getChartSizeSide(height)) / (getMaxValueY() - getMinValueY())
        return height - pos - PADDING
    }

    private fun getMaxValueX(): Int {
        return (markers.maxWithOrNull { o1, o2 -> o1.x - o2.x }?.x
            ?: 0)
    }

    private fun getMaxValueY(): Int {
        return (markers.maxWithOrNull { o1, o2 -> o1.y - o2.y }?.y
            ?: 0)
    }

    private fun getMinValueX(): Int {
        return (markers.minWithOrNull { o1, o2 -> o1.x - o2.x }?.x
            ?: 0)
    }

    private fun getMinValueY(): Int {
        return (markers.minWithOrNull { o1, o2 -> o1.y - o2.y }?.y
            ?: 0)
    }

    private fun getChartSizeSide(sizeView: Int): Float {
        return sizeView - PADDING * 2
    }
}