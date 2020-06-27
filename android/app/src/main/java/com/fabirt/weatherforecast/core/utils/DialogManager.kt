package com.fabirt.weatherforecast.core.utils

import android.app.Dialog
import android.content.Context
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import com.fabirt.weatherforecast.R

object DialogManager {
    fun show(
        context: Context,
        title: String = "",
        body: String = "",
        positiveText: String = "Confirm",
        negativeText: String = "Cancel",
        onConfirm: () -> Unit = {}
    ) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.popup_dialog)
        val window = dialog.window!!
        window.setElevation(24F) // Material specs dialog elevation
        val layoutParams = WindowManager.LayoutParams().apply {
            copyFrom(window.attributes)
            width = WindowManager.LayoutParams.WRAP_CONTENT
            windowAnimations = R.style.PopupDialog
        }
        window.attributes = layoutParams
        window.setWindowAnimations(R.style.PopupDialogAnimation)
        window.setBackgroundDrawable(context.getDrawable(R.drawable.shape_popup))
        val titleText = dialog.findViewById<TextView>(R.id.dialogTitleText)
        val bodyText = dialog.findViewById<TextView>(R.id.dialogBodyText)
        val positiveButton = dialog.findViewById<Button>(R.id.dialogPositiveButton)
        val negativeButton = dialog.findViewById<Button>(R.id.dialogNegativeButton)
        titleText.text = title
        bodyText.text = body
        positiveButton.text = positiveText
        negativeButton.text = negativeText
        negativeButton.setOnClickListener {
            dialog.dismiss()
        }
        positiveButton.setOnClickListener {
            onConfirm()
            dialog.dismiss()
        }
        dialog.show()
    }
}