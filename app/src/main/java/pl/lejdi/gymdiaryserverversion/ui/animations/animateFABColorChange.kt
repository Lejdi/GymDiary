package pl.lejdi.gymdiaryserverversion.ui.animations

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.ColorStateList
import androidx.core.content.ContextCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton

//motionlayout doesn't support animating FAB background - need to use ValueAnimator
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