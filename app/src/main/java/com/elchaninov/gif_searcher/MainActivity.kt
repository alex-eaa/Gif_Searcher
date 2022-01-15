package com.elchaninov.gif_searcher

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.elchaninov.gif_searcher.databinding.MainActivityBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}