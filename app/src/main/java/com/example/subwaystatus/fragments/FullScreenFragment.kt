package com.example.subwaystatus.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.subwaystatus.R
import com.example.subwaystatus.databinding.FragmentFullScreenBinding


class FullScreenFragment : Fragment() {

    private lateinit var binding: FragmentFullScreenBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFullScreenBinding.inflate(inflater, container, false)

        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.actionbar_title)


        return binding.root
    }


}