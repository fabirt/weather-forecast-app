package com.fabirt.weatherforecast.core.transitions

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.transition.TransitionValues
import android.transition.Visibility
import android.view.View
import android.view.ViewGroup

class FadeScale : Visibility() {

    override fun onAppear(
        sceneRoot: ViewGroup?,
        view: View?,
        startValues: TransitionValues?,
        endValues: TransitionValues?
    ): Animator {
        return createScaleAnimator(view)
    }

    override fun onDisappear(
        sceneRoot: ViewGroup?,
        view: View?,
        startValues: TransitionValues?,
        endValues: TransitionValues?
    ): Animator {
        return createFadeAnimator(view)
    }

    private fun createScaleAnimator(
        view: View?,
        startScale: Float = 0.84f,
        endScale: Float = 1f,
        startAlpha: Float = 0f,
        endAlpha: Float = 1f
    ): Animator {
        val holderX = PropertyValuesHolder.ofFloat("scaleX", startScale, endScale)
        val holderY = PropertyValuesHolder.ofFloat("scaleY", startScale, endScale)
        val holderAlpha = PropertyValuesHolder.ofFloat("transitionAlpha", startAlpha, endAlpha)
        return ObjectAnimator.ofPropertyValuesHolder(view, holderX, holderY, holderAlpha)
    }

    private fun createFadeAnimator(
        view: View?,
        startAlpha: Float = 1f,
        endAlpha: Float = 0f
    ): Animator {
        val holderAlpha = PropertyValuesHolder.ofFloat("transitionAlpha", startAlpha, endAlpha)
        return ObjectAnimator.ofPropertyValuesHolder(view, holderAlpha)
    }
}