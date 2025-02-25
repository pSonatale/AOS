package com.example.sonatale

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.sonatale.databinding.FragmentTaleBinding

class TaleFragment : Fragment() {
    private lateinit var binding: FragmentTaleBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTaleBinding.inflate(layoutInflater)
        return binding.root
    }
}