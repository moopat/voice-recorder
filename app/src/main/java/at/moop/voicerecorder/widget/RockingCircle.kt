package at.moop.voicerecorder.widget

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.util.AttributeSet
import android.view.View
import android.view.animation.BounceInterpolator
import android.view.animation.LinearInterpolator
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import at.moop.voicerecorder.R
import kotlin.math.max
import kotlin.math.min

/**
 * @author Markus Deutsch <markus@moop.at>
 */
class RockingCircle(context: Context, attrs: AttributeSet) : View(context, attrs) {

    companion object {
        private const val GROW_ANIMATION_DURATION: Long = 800
        private const val SHRINK_ANIMATION_DURATION: Long = 1500
        private const val MAX_ALPHA = 255
    }

    private var fillColor = ContextCompat.getColor(context, R.color.colorBounceFill)

    private var availableW = 0
    private var availableH = 0

    private var targetRadiusRatio: Float = 0f

    private var targetRadius: Float = 0f
    private var previousRadius: Float = 0f
    private var currentRadius: Float = 0f
        set(value) {
            field = value
            postInvalidateOnAnimation()
        }

    private val circlePaint = Paint(ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = fillColor
    }

    private val growCircleAnimator = ValueAnimator.ofFloat(0f, 1f).apply {
        addUpdateListener {
            val totalDiff = targetRadius - previousRadius
            val nextValue = totalDiff * (it.animatedValue as Float)
            currentRadius = previousRadius + nextValue
        }
        addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                shrinkCircleAnimator.start()
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {
            }


        })
        duration = GROW_ANIMATION_DURATION
        interpolator = BounceInterpolator()
    }

    private val shrinkCircleAnimator = ValueAnimator.ofFloat(1f, 0f).apply {
        addUpdateListener {
            currentRadius = targetRadius * (it.animatedValue as Float)
        }
        duration = SHRINK_ANIMATION_DURATION
        interpolator = LinearInterpolator()
    }

    private fun recalculateTargetRadius() {
        previousRadius = currentRadius
        targetRadius = getMaxRadius() * targetRadiusRatio
    }

    private fun getMaxRadius() = max(min(availableH, availableW) / 2, 1)

    private fun getCurrentRatio() = currentRadius / getMaxRadius()

    fun setRadiusRatio(ratio: Float) {
        if (ratio < getCurrentRatio()) {
            // Avoid too quick shrinking.
            return
        }
        growCircleAnimator.cancel()
        shrinkCircleAnimator.cancel()
        targetRadiusRatio = min(1f, ratio)
        recalculateTargetRadius()
        growCircleAnimator.start()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        availableW = w
        availableH = h
        recalculateTargetRadius()
        super.onSizeChanged(w, h, oldw, oldh)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.apply {
            drawCircle(
                availableW.toFloat().div(2),
                availableH.toFloat().div(2),
                currentRadius,
                circlePaint.apply {
                    color = ColorUtils.setAlphaComponent(
                        fillColor,
                        getCurrentRatio().times(MAX_ALPHA).toInt()
                    )
                }
            )
        }
    }

}
