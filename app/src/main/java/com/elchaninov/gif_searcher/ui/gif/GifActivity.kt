package com.elchaninov.gif_searcher.ui.gif

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.elchaninov.gif_searcher.databinding.GifActivityBinding


class GifActivity : AppCompatActivity() {

    private lateinit var binding: GifActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = GifActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

}