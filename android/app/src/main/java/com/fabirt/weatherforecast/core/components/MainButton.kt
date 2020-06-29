package com.fabirt.weatherforecast.core.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.fabirt.weatherforecast.R
import kotlinx.android.synthetic.main.view_main_button.view.*

class MainButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
    defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyle, defStyleRes) {

    private var _text: String? = null
    var text: String?
        get() = _text
        set(value) {
            _text = value
            buttonMain.text = value
        }

    init {
        inflateLayout()
        setAttributes(attrs)
    }

    private fun inflateLayout() {
        LayoutInflater.from(context)
            .inflate(R.layout.view_main_button, this, true)
    }

    private fun setAttributes(attrs: AttributeSet?) {
        attrs?.let {
            context.obtainStyledAttributes(it, R.styleable.MainButton).apply {
                try {
                    _text = getString(R.styleable.MainButton_text)
                    buttonMain.text = _text
                    buttonMain.setTextColor(getColor(R.styleable.MainButton_textColor, 0))
                    buttonMain.rippleColor = getColorStateList(R.styleable.MainButton_splashColor)
                    buttonMain.backgroundTintList =
                        getColorStateList(R.styleable.MainButton_tintColor)
                } finally {
                    recycle()
                }
            }
        }
    }

    fun setOnClickListener(l: (View) -> Unit) {
        buttonMain.setOnClickListener(l)
    }
}