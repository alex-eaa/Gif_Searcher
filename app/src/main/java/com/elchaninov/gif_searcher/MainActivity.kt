package com.elchaninov.gif_searcher

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.elchaninov.gif_searcher.databinding.MainActivityBinding
import com.elchaninov.gif_searcher.ui.main.GifAdapter
import com.elchaninov.gif_searcher.ui.main.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java).also {
            App.instance.component.inject(it)
        }
    }

    private var gifAdapter: GifAdapter = GifAdapter(R.layout.item_line_gif)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.gifs.observe(this, { gifs ->
            gifAdapter.data = gifs
        })
        viewModel.searchGifs()

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = gifAdapter
    }
}