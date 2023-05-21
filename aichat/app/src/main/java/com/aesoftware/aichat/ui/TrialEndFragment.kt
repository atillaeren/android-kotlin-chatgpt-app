package com.aesoftware.aichat.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.aesoftware.aichat.databinding.FragmentTrialEndBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TrialEndFragment : Fragment() {
    private lateinit var binding: FragmentTrialEndBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTrialEndBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.closeIcon.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.tryBtn.setOnClickListener {
            findNavController().navigate(TrialEndFragmentDirections.actionTrialEndFragmentToInAppFragment())
        }
    }

}