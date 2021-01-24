package io.driverdoc.testapp.ui.customview

import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation

/**
 * @author sonvh
 * create animations
 */
class AimeeAnimations {
    companion object {
        /**
         * pulse in then pulse out animation
         */
        fun createPulseAnim(view: View) {
            val shrinkAnim = ScaleAnimation(1.0f, 0.5f, 1.0f, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
            val growAnim = ScaleAnimation(0.5f, 1.0f, 0.5f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
            shrinkAnim.duration = 2000
            growAnim.duration = 2000
            view.animation = shrinkAnim
            shrinkAnim.start()
            shrinkAnim.setAnimationListener(object : Animation.AnimationListener{
                override fun onAnimationRepeat(animation: Animation?) {
                }

                override fun onAnimationEnd(animation: Animation?) {
                    view.animation = growAnim
                    growAnim.start()
                }

                override fun onAnimationStart(animation: Animation?) {
                }
            })
            growAnim.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationRepeat(animation: Animation?) {
                }

                override fun onAnimationEnd(animation: Animation?) {
                    view.animation = shrinkAnim
                    shrinkAnim.start()
                }

                override fun onAnimationStart(animation: Animation?) {
                }
            })
        }
    }
}