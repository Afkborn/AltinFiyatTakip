package com.bilgehankalay.altinfiyattakip.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bilgehankalay.altinfiyattakip.R
import com.bilgehankalay.altinfiyattakip.databinding.FragmentVarliklarimBinding


class Varliklarim : Fragment() {
    private lateinit var binding : FragmentVarliklarimBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVarliklarimBinding.inflate(inflater,container,false)
        return binding.root
    }

}