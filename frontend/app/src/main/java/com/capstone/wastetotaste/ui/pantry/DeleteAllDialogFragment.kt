package com.capstone.wastetotaste.ui.pantry

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.capstone.wastetotaste.ViewModelFactory
import com.capstone.wastetotaste.databinding.FragmentDeleteAllDialogBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DeleteAllDialogFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DeleteAllDialogFragment : DialogFragment() {
    private lateinit var binding: FragmentDeleteAllDialogBinding
    private lateinit var deleteAllDialogViewModel: DeleteAllDialogViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDeleteAllDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        deleteAllDialogViewModel = obtainViewModel(this)
        binding.btnCancelDeleteAll.setOnClickListener {
            dialog?.cancel()
        }
        binding.btnDeleteAll.setOnClickListener {
            deleteAllDialogViewModel.deleteAllIngredients()
            dialog?.dismiss()
        }
    }

    private fun obtainViewModel(fragment: Fragment): DeleteAllDialogViewModel {
        val factory = ViewModelFactory.getInstance(fragment.requireActivity().application)
        return ViewModelProvider(fragment.requireActivity(), factory)[DeleteAllDialogViewModel::class.java]
    }
}