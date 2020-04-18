package com.fabirt.weatherforecast.core

import android.view.View
import androidx.databinding.BindingAdapter

/**
 * Binding adapter used to hide the view
 */
@BindingAdapter("goneIfNull")
fun goneIfNull(view: View, it: Any?) {
    view.visibility = if (it == null) View.GONE else View.VISIBLE
}

@BindingAdapter("goneIfNotNull")
fun goneIfNotNull(view: View, it: Any?) {
    view.visibility = if (it != null) View.GONE else View.VISIBLE
}