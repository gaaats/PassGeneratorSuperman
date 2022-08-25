package com.passpackfour.passgeneratorsuperman

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.passpackfour.passgeneratorsuperman.databinding.FragmentStartBinding


class StartFragment : Fragment() {

    private var _binding: FragmentStartBinding? = null
    private val binding get() = _binding ?: throw RuntimeException("FragmentStartBinding = null")


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        try {
            binding.btnConfirm.setOnClickListener {
                try {
                    findNavController().navigate(R.id.action_startFragment_to_resultFragment)
                } catch (e: Exception) {
                    Snackbar.make(binding.root, "There is some error, try again", Snackbar.LENGTH_LONG)
                        .show()
                }
            }
            binding.btnSettings.setOnClickListener {
                findNavController().navigate(R.id.action_startFragment_to_settingsFragment)
            }

        } catch (e: Exception) {
            Snackbar.make(binding.root, "There is some error, try again", Snackbar.LENGTH_LONG)
                .show()
        }
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}