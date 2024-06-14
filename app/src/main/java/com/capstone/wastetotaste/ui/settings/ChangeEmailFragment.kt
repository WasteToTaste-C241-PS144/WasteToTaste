package com.capstone.wastetotaste.ui.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.capstone.wastetotaste.R
import com.capstone.wastetotaste.databinding.FragmentChangeEmailBinding
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

        // Validasi email baru
        if (newEmail.isEmpty()) {
            binding.edtEmail.error = "Email tidak boleh kosong"
            return
        }

        // Mendapatkan instance FirebaseAuth
        val auth = FirebaseAuth.getInstance()

        // Mendapatkan pengguna yang sedang login
        val currentUser = auth.currentUser

        // Memeriksa apakah currentUser tidak null
        currentUser?.let { user ->
            // Update email di Firebase Authentication
            user.updateEmail(newEmail)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Update email di database aplikasi Anda (jika diperlukan)
                        // Contoh: Simpan ke database lokal atau server aplikasi

                        // Tampilkan pesan sukses
                        Toast.makeText(requireContext(), "Email berhasil diperbarui", Toast.LENGTH_SHORT).show()

                        // Kembali ke halaman sebelumnya
                        requireActivity().onBackPressedDispatcher.onBackPressed()
                    } else {
                        // Handle error saat update email
                        Toast.makeText(requireContext(), "Gagal memperbarui email: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}