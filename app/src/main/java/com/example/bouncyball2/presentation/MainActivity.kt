package com.example.bouncyball2.presentation

import com.example.bouncyball2.R

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.ImageView

class MainActivity : Activity() {

    private var ballView: ImageView? = null
    private var animation: Animation? = null
    private var isBouncing = false
    private var originalX = 0f
    private var originalY = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ballView = findViewById(R.id.ball_view) as? ImageView

        ballView?.setOnClickListener {
            if (!isBouncing) {
                val (targetX, targetY) = calculateTargetPosition()
                startJumpAnimation(targetX, targetY)
            }
        }

        originalX = ballView?.x ?: 0f
        originalY = ballView?.y ?: 0f
    }

    private fun calculateTargetPosition(): Pair<Float, Float> {
        val screenWidth = resources.displayMetrics.widthPixels
        val screenHeight = resources.displayMetrics.heightPixels

        val ballWidth = ballView?.width ?: 0
        val ballHeight = ballView?.height ?: 0

        // Calculate the maximum allowed X and Y positions to stay within the bounds of the screen
        val maxX = (screenWidth - ballWidth) / resources.displayMetrics.density
        val maxY = (screenHeight - ballHeight) / resources.displayMetrics.density

        // Calculate random target positions with more randomness in both X and Y directions
        val randomOffsetX = (-maxX.toInt()..maxX.toInt()).random()
        val randomOffsetY = (-maxY.toInt()..maxY.toInt()).random()

        // Calculate the final target positions within the adjusted bounds
        val targetX = (originalX / resources.displayMetrics.density + randomOffsetX).toFloat()
        val targetY = (originalY / resources.displayMetrics.density + randomOffsetY).toFloat()

        return Pair(targetX, targetY)
    }

    private fun startJumpAnimation(targetX: Float, targetY: Float) {
        animation?.let {
            it.cancel()
        }

        val startX = ballView?.x ?: originalX
        val startY = ballView?.y ?: originalY

        val animatorX = ValueAnimator.ofFloat(startX, targetX)
        animatorX.duration = 500
        animatorX.addUpdateListener { valueAnimator ->
            val x = valueAnimator.animatedValue as Float
            ballView?.translationX = x
        }

        val animatorY = ValueAnimator.ofFloat(startY, targetY)
        animatorY.duration = 500
        animatorY.addUpdateListener { valueAnimator ->
            val y = valueAnimator.animatedValue as Float
            ballView?.translationY = y
        }

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(animatorX, animatorY)
        animatorSet.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                // Animation started
            }

            override fun onAnimationEnd(animation: Animator) {
                // Animation ended
                isBouncing = false
            }

            override fun onAnimationCancel(animation: Animator) {
                // Animation canceled (not used in this case)
            }

            override fun onAnimationRepeat(animation: Animator) {
                // Animation repeated (not used in this case)
            }
        })

        animatorSet.start()
        isBouncing = true
    }
}
