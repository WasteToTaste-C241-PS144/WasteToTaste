package com.capstone.wastetotaste.ui.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import com.capstone.wastetotaste.R
import com.capstone.wastetotaste.cv.ConfirmPassCV
import com.capstone.wastetotaste.cv.PassCV
import com.capstone.wastetotaste.databinding.FragmentChangePasswordBinding
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth

class ChangePasswordFragment : Fragment() {
    private var _binding: FragmentChangePasswordBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChangePasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val passwordOldEditText: PassCV = binding.edtOldPassword
        val passwordNewEditText: PassCV = binding.edtNewPassword
        val passwordConfEditText: ConfirmPassCV = binding.edtConfirmPassword
        val togglePasswordButtonOld: ImageButton = binding.btnEyeOld
        val togglePasswordButtonNew: ImageButton = binding.btnEyeNew
        val togglePasswordButtonConf: ImageButton = binding.btnEyeConf
        passwordConfEditText.setPasswordFieldId(R.id.edt_new_password) // Replace with your actual new password field ID


        togglePasswordButtonOld.setOnClickListener {
            passwordOldEditText.togglePasswordVisibility()
            togglePasswordButtonOld.setBackgroundResource(
                if (passwordOldEditText.isPasswordVisible) {
                    R.drawable.ic_eye_closed // Change to the eye closed icon
                } else {
                    R.drawable.ic_eye // Change to the eye open icon
                }
            )
        }
        togglePasswordButtonNew.setOnClickListener {
            passwordNewEditText.togglePasswordVisibility()
            togglePasswordButtonNew.setBackgroundResource(
                if (passwordNewEditText.isPasswordVisible) {
                    R.drawable.ic_eye_closed // Change to the eye closed icon
                } else {
                    R.drawable.ic_eye // Change to the eye open icon
                }
            )
        }
        togglePasswordButtonConf.setOnClickListener {
            passwordConfEditText.togglePasswordVisibility()
            togglePasswordButtonConf.setBackgroundResource(
                if (passwordConfEditText.isPasswordVisible) {
                    R.drawable.ic_eye_closed // Change to the eye closed icon
                } else {
                    R.drawable.ic_eye // Change to the eye open icon
                }
            )
        }

        binding.btnSave.setOnClickListener {
            updatePassword()
        }

        binding.topAppBar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }
    private fun updatePassword() {
        val currentPassword = binding.edtOldPassword.text.toString().trim()
        val newPassword = binding.edtNewPassword.text.toString().trim()
        val confirmPassword = binding.edtConfirmPassword.text.toString().trim()

        if (currentPassword.isEmpty()) {
            binding.edtOldPassword.error = "Password saat ini tidak boleh kosong"
            return
        }

        if (newPassword.isEmpty()) {
            binding.edtNewPassword.error = "Password baru tidak boleh kosong"
            return
        }

        if (confirmPassword.isEmpty()) {
            binding.edtConfirmPassword.error = "Konfirmasi password tidak boleh kosong"
            return
        }

        if (newPassword != confirmPassword) {
            binding.edtConfirmPassword.error = "Password tidak sesuai"
            return
        }

        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        currentUser?.let { user ->
            val credential = EmailAuthProvider.getCredential(user.email!!, currentPassword)
            user.reauthenticate(credential).addOnCompleteListener { reAuthTask ->
                if (reAuthTask.isSuccessful) {
                    user.updatePassword(newPassword).addOnCompleteListener { updateTask ->
                        if (updateTask.isSuccessful) {
                            Toast.makeText(requireContext(),"Password berhasil diperbarui", Toast.LENGTH_SHORT).show()
                            requireActivity().onBackPressedDispatcher.onBackPressed()
                        } else {
                            Toast.makeText(requireContext(), "Gagal memperbarui password: ${updateTask.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(requireContext(), "Re-authentication gagal: ${reAuthTask.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}