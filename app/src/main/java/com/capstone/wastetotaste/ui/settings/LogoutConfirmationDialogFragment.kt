package com.capstone.wastetotaste.ui.settings

import android.os.Bundle
import android.util.DisplayMetrics
import androidx.fragment.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.capstone.wastetotaste.R

class LogoutConfirmationDialogFragment(private val onLogoutConfirmed: () -> Unit) : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_logout_dialog, container, false)

        val btnYes: Button = view.findViewById(R.id.btn_yes)
        val btnNo: Button = view.findViewById(R.id.btn_no)

        btnYes.setOnClickListener {
            onLogoutConfirmed()
            dismiss()
        }

        btnNo.setOnClickListener {
            dismiss()
        }

        return view
    }

    override fun onStart() {
        super.onStart()
        // Set the maximum width of the dialog
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        // Get the screen width and set the desired maximum width
        val displayMetrics = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        val screenWidth = displayMetrics.widthPixels
        val maxWidth = (screenWidth * 0.9).toInt() // 90% of screen width

        dialog?.window?.setLayout(maxWidth, ViewGroup.LayoutParams.WRAP_CONTENT)
    }
}
