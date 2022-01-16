package com.elchaninov.gif_searcher.ui.gif

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.elchaninov.gif_searcher.R
import com.elchaninov.gif_searcher.data.Gif
import com.elchaninov.gif_searcher.databinding.GifActivityBinding


class GifActivity : AppCompatActivity() {

    private lateinit var binding: GifActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = GifActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val gif: Gif? = intent.getParcelableExtra(EXTRA_GIF)

        gif?.let {
            Glide
                .with(this)
                .asGif()
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .load(it.urlView)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(binding.gifView)
        }
    }

    companion object {
        const val EXTRA_GIF = "EXTRA_GIF"
    }
}