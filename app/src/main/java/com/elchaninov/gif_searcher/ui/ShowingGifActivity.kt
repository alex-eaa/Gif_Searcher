package com.elchaninov.gif_searcher.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.elchaninov.gif_searcher.R
import com.elchaninov.gif_searcher.model.Gif
import com.elchaninov.gif_searcher.databinding.GifActivityBinding


class ShowingGifActivity : AppCompatActivity() {

    private lateinit var binding: GifActivityBinding

    private val requestListener: RequestListener<GifDrawable> =
        object : RequestListener<GifDrawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<GifDrawable>?,
                isFirstResource: Boolean
            ): Boolean {
                binding.progressContainer.progress.hide()
                binding.errorContainer.error.show()
                return false
            }

            override fun onResourceReady(
                resource: GifDrawable?,
                model: Any?,
                target: Target<GifDrawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                binding.errorContainer.error.hide()
                binding.progressContainer.progress.hide()
                return false
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = GifActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val gif: Gif? = intent.getParcelableExtra(EXTRA_GIF)
        gif?.let {
            renderGif(gif)
            binding.errorContainer.tryAgainBtn.setOnClickListener {
                renderGif(gif)
            }
        }

        binding.errorContainer.tryAgainBtn.setOnClickListener {
            gif?.let { renderGif(gif) }
        }
    }

    private fun renderGif(gif: Gif) {
        binding.errorContainer.error.hide()
        binding.progressContainer.progress.show()

        Glide
            .with(this)
            .asGif()
            .placeholder(R.drawable.ic_baseline_image_24)
            .error(R.drawable.ic_baseline_broken_image_24)
            .load(gif.urlView)
            .addListener(requestListener)
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .into(binding.gifView)
    }

    companion object {
        const val EXTRA_GIF = "EXTRA_GIF"
    }
}