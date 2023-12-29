package com.example.madcamp_week1_rev

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.madcamp_week1_rev.databinding.FragmentMemoBinding

class MemoFragment : Fragment() {
    private lateinit var binding : FragmentMemoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMemoBinding.inflate(inflater, container, false)
        return binding.root
    }

}