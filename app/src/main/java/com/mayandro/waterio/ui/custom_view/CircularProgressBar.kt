package com.mayandro.waterio.ui.custom_view

import android.animation.ValueAnimator
import android.graphics.RectF
import android.view.animation.DecelerateInterpolator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import kotlin.math.min


class CircularProgressBar(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
    View(context, attrs, defStyleAttr) {
    private var viewWidth = 0
    private var viewHeight = 0
    private val startAngle = -90f // Always start from top (default is: "3 o'clock on a watch.")
    private var sweepAngle = 0f // How long to sweep from mStartAngle
    private val maxSweepAngle = 360f // Max degrees to sweep = full circle
    private var strokeWidth = 10f // Width of outline
    private val animationDuration = 700 // Animation duration for progress change
    private val maxProgress = 100 // Max progress to use
    private var drawText = true // Set to true if progress text should be drawn
    private var progressColor: Int = Color.BLACK // Outline color
    private var textColor: Int = Color.BLACK // Progress text color
    private val paint : Paint = Paint(Paint.ANTI_ALIAS_FLAG) // Allocate paint outside onDraw to avoid unnecessary object creation
    private val backgroundPaint = Paint().apply {
        color = Color.LTGRAY
        style = Paint.Style.STROKE
        strokeWidth = 10f
        isAntiAlias = true
    }


    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        initMeasurments()
        drawOutlineArc(canvas)
        if (drawText) {
            drawText(canvas)
        }
    }

    private fun initMeasurments() {
        viewWidth = width
        viewHeight = height
    }

    private fun drawOutlineArc(canvas: Canvas) {
        val diameter = min(viewWidth, viewHeight) - strokeWidth * 2
        val outerOval = RectF(strokeWidth, strokeWidth, diameter, diameter)
        paint.color = progressColor
        paint.strokeWidth = strokeWidth
        paint.isAntiAlias = true
        paint.strokeCap = Paint.Cap.ROUND
        paint.style = Paint.Style.STROKE
        canvas?.drawArc(outerOval, 270f, 360f, false, backgroundPaint)
        canvas.drawArc(outerOval, startAngle, sweepAngle, false, paint)
    }

    private fun drawText(canvas: Canvas) {
        paint.textSize = min(viewWidth, viewHeight) / 4f
        paint.textAlign = Paint.Align.CENTER
        paint.strokeWidth = 0f
        paint.color = textColor
        // Center text
        val xPos: Int = canvas.width / 2
        val yPos = (canvas.height / 2 - (paint.descent() + paint.ascent()) / 2)
        canvas.drawText(calcProgressFromSweepAngle(sweepAngle).toString() + "%", xPos.toFloat(), yPos, paint)
    }

    private fun calcSweepAngleFromProgress(progress: Int): Float {
        return maxSweepAngle / maxProgress * progress
    }

    private fun calcProgressFromSweepAngle(sweepAngle: Float): Int {
        return (sweepAngle * maxProgress / maxSweepAngle).toInt()
    }

    /**
     * Set progress of the circular progress bar.
     * @param progress progress between 0 and 100.
     */
    fun setProgress(progress: Int) {
        val animator = ValueAnimator.ofFloat(sweepAngle, calcSweepAngleFromProgress(progress))
        animator.interpolator = DecelerateInterpolator()
        animator.duration = animationDuration.toLong()
        animator.addUpdateListener { valueAnimator ->
            sweepAngle = valueAnimator.animatedValue as Float
            invalidate()
        }
        animator.start()
    }

    fun setProgressColor(color: Int) {
        progressColor = color
        invalidate()
    }

    fun setProgressWidth(width: Int) {
        strokeWidth = width.toFloat()
        invalidate()
    }

    fun setTextColor(color: Int) {
        textColor = color
        invalidate()
    }

    fun showProgressText(show: Boolean) {
        drawText = show
        invalidate()
    }
}