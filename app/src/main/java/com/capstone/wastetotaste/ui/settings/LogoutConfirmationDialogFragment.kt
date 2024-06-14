package com.capstone.wastetotaste.ui.settings

import android.os.Bundle
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
}
