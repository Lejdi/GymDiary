package pl.lejdi.gymdiary.ui.animations

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.ColorStateList
import androidx.core.content.ContextCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton

fun animateFABColorChange(
    context : Context,
    fab : FloatingActionButton,
    startColor : Int,
    endColor : Int,
    duration : Long
){
    val colorFrom = ContextCompat.getColor(
        context,
        startColor
    )
    val colorTo = ContextCompat.getColor(
        context,
        endColor
    )
    val colorAnimation =
        ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
    colorAnimation.duration = duration
    colorAnimation.addUpdateListener {
            animator ->
        fab.backgroundTintList= ColorStateList.valueOf(
            animator.animatedValue as Int)
    }
    colorAnimation.start()
}