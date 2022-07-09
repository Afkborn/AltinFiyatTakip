package com.bilgehankalay.altinfiyattakip.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bilgehankalay.altinfiyattakip.databinding.ActivityServerErrorBinding

class ServerErrorActivity : AppCompatActivity() {

    private lateinit var binding : ActivityServerErrorBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityServerErrorBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}