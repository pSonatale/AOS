package com.example.sonatale

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sonatale.databinding.FragmentTaleBinding

class TaleFragment : Fragment() {
    private lateinit var binding: FragmentTaleBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTaleBinding.inflate(layoutInflater)

        setTaleSavedRecyclerView()
        return binding.root
    }

    private fun setTaleSavedRecyclerView() {
        val taleTitle = listOf(
            "잠자는 숲속의 공주", "헨젤과 그레텔", "빨간 망토"
        )

        val adapter = TaleSavedRVAdapter(taleTitle)
        binding.rvBook.adapter = adapter
        binding.rvBook.layoutManager = LinearLayoutManager(requireContext())
    }
}