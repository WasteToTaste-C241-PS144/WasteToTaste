package com.capstone.wastetotaste.ui.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.capstone.wastetotaste.R
import com.capstone.wastetotaste.databinding.FragmentAccountInfoBinding
import com.google.firebase.auth.FirebaseAuth

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AccountInfoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AccountInfoFragment : Fragment() {

    private var _binding: FragmentAccountInfoBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAccountInfoBinding.inflate(inflater, container, false)
        val view: View = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        // Memeriksa apakah currentUser null (tidak ada pengguna yang login)
        if (currentUser != null) {
            // Mengambil email pengguna dari currentUser
            val userEmail = currentUser.email

            // Menampilkan email pengguna di TextView
            userEmail?.let {
                binding.tvUserEmail.text = it
            }
        }
        binding.btnChangeEmail.setOnClickListener {
            val changeEmailFragment = ChangeEmailFragment()
            val fragmentManager = parentFragmentManager
            fragmentManager.beginTransaction().apply {
                replace(R.id.frame_container, changeEmailFragment, ChangeEmailFragment::class.java.simpleName)
                addToBackStack(null)
                commit()
            }
        }
        binding.btnChangePassword.setOnClickListener {
            val changePasswordFragment = ChangePasswordFragment()
            val fragmentManager = parentFragmentManager
            fragmentManager?.beginTransaction()?.apply {
                replace(R.id.frame_container, changePasswordFragment, ChangePasswordFragment::class.java.simpleName)
                addToBackStack(null)
                commit()
            }
        }
        binding.topAppBar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}