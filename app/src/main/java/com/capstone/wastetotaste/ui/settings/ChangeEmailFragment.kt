package com.capstone.wastetotaste.ui.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.capstone.wastetotaste.R
import com.capstone.wastetotaste.cv.PassCV
import com.capstone.wastetotaste.databinding.FragmentChangeEmailBinding
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ChangeEmailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChangeEmailFragment : Fragment() {
    private var _binding: FragmentChangeEmailBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChangeEmailBinding.inflate(inflater, container, false)
        val view: View = binding.root
        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Toggle Password Visibility
        val passwordEditText: PassCV = binding.edtPassword
        val togglePasswordButton: ImageButton = binding.btnEyeSignup

        togglePasswordButton.setOnClickListener {
            passwordEditText.togglePasswordVisibility()
            togglePasswordButton.setBackgroundResource(
                if (passwordEditText.isPasswordVisible) {
                    R.drawable.ic_eye_closed // Change to the eye closed icon
                } else {
                    R.drawable.ic_eye // Change to the eye open icon
                }
            )
        }

        // Set onClickListener untuk tombol Simpan
        binding.btnSave.setOnClickListener {
            updateEmail()
        }

        // Set email pengguna yang sedang login ke EditText edtEmail
        setLoggedInUserEmail()

        binding.topAppBar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }


    private fun setLoggedInUserEmail() {
        // Mendapatkan instance FirebaseAuth
        val auth = FirebaseAuth.getInstance()

        // Mendapatkan pengguna yang sedang login
        val currentUser = auth.currentUser

        // Memeriksa apakah currentUser tidak null dan email tidak kosong
        if (currentUser != null && !currentUser.email.isNullOrEmpty()) {
            // Mengisi email pengguna ke EditText edtEmail
            binding.edtEmail.setText(currentUser.email)
        } else {
            // Handle jika tidak ada pengguna yang login atau email kosong
            // Contoh: Tampilkan pesan atau navigasi ke halaman login
        }
    }

    private fun updateEmail() {
        val newEmail = binding.edtEmail.text.toString().trim()
        val password = binding.edtPassword.text.toString().trim()

        if (newEmail.isEmpty()) {
            binding.edtEmail.error = "Email tidak boleh kosong"
            return
        }

        if (password.isEmpty()) {
            binding.edtPassword.error = "Password tidak boleh kosong"
            return
        }

        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        currentUser?.let { user ->
            val credential = EmailAuthProvider.getCredential(user.email!!, password)
            user.reauthenticate(credential).addOnCompleteListener { reAuthTask ->
                if (reAuthTask.isSuccessful) {
                    user.updateEmail(newEmail).addOnCompleteListener { updateTask ->
                        if (updateTask.isSuccessful) {
                            Toast.makeText(requireContext(), "Email berhasil diperbarui", Toast.LENGTH_SHORT).show()
                            requireActivity().onBackPressedDispatcher.onBackPressed()
                        } else {
                            Toast.makeText(requireContext(), "Gagal memperbarui email: ${updateTask.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(requireContext(), "Re-authentication failed: ${reAuthTask.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}