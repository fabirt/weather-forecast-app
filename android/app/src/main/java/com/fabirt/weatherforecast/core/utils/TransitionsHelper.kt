package com.fabirt.weatherforecast.core.utils

import androidx.fragment.app.Fragment
import com.fabirt.weatherforecast.core.transitions.FadeScale

object TransitionsHelper {
    fun setupTransitions(fragment: Fragment) {
        val transitionDuration = 300L
        val animation = FadeScale().apply {
            duration = transitionDuration
        }
        fragment.enterTransition = animation
        fragment.exitTransition = animation
        fragment.returnTransition = animation
    }
}