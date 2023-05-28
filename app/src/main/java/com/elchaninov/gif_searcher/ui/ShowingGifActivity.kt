package com.elchaninov.gif_searcher.ui

import android.content.ClipData
import android.content.ContextWrapper
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider.getUriForFile
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.elchaninov.gif_searcher.App
import com.elchaninov.gif_searcher.BuildConfig
import com.elchaninov.gif_searcher.R
import com.elchaninov.gif_searcher.databinding.GifActivityBinding
import com.elchaninov.gif_searcher.model.Gif
import com.elchaninov.gif_searcher.parcelable
import com.elchaninov.gif_searcher.viewModel.CachingState
import com.elchaninov.gif_searcher.viewModel.ShowingGifViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import java.io.File
import javax.inject.Inject


class ShowingGifActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: ShowingGifViewModel

    private lateinit var binding: GifActivityBinding
    private var gif: Gif? = null
    private var adView: AdView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        App.instance.component.inject(this)
        super.onCreate(savedInstanceState)
        binding = GifActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (BuildConfig.ALLOW_AD) bannerAdsInit()
        viewModel = ViewModelProvider(this, viewModelFactory)[ShowingGifViewModel::class.java]
        gif = intent.parcelable(EXTRA_GIF)

        if (savedInstanceState == null) fetchGif()

        viewModel.fileLiveData.observe(this) { cachingState ->
            when (cachingState) {
                is CachingState.Success -> {
                    binding.progress.hide()
                    renderGif(cachingState.file)
                    binding.fab.show()
                    binding.fab.setOnClickListener { shareGif(cachingState.file) }
                }
                is CachingState.Failure -> {
                    binding.progress.hide()
                    showError()
                }
                is CachingState.Progress -> {
                    binding.progress.show()
                    binding.progressText.text =
                        getString(R.string.progress_percent, cachingState.percent)
                    binding.progressIndicator.progress = cachingState.percent
                }
            }
        }

        val adRequest = AdRequest.Builder().build()
        adView?.loadAd(adRequest)
    }

    private fun fetchGif() {
        gif?.let {
            viewModel.fileCaching(it, getSharedFileInstance(it))
        }
    }

    override fun onResume() {
        super.onResume()
        adView?.resume()
    }

    override fun onPause() {
        super.onPause()
        adView?.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        adView?.destroy()
    }

    private fun renderGif(file: File) {
        Glide
            .with(this)
            .asGif()
            .load(file)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(binding.gifView)
    }

    private fun shareGif(file: File) {
        val contentUri: Uri = getUriForFile(this, PROVIDER_AUTHORITIES, file)

        val shareIntent: Intent = Intent().apply {
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, contentUri)
            type = INTENT_TYPE
            clipData = ClipData.newRawUri("", contentUri)
        }

        val chooser = Intent.createChooser(shareIntent, null)
        startActivity(chooser)
    }

    private fun getSharedFileInstance(gif: Gif): File {
        val imagePath: File = ContextWrapper(this).cacheDir
        return File(imagePath, gif.id + ".gif")
    }

    private fun showError() {
        binding.root.showSnackbar(
            text = getString(R.string.error_message_2),
            actionText = getString(R.string.button_try_again),
            action = { fetchGif() }
        )
    }

    private fun bannerAdsInit() {
        adView = AdView(this)
        adView?.adUnitId = BuildConfig.BANNER_AD_UNIT_ID
        adView?.setAdSize(AdSize.BANNER)
        binding.layoutBannerHolder.layoutParams.height = resources.getDimensionPixelSize(R.dimen.banner_height)
        binding.layoutBannerHolder.addView(adView)
    }

    companion object {
        const val EXTRA_GIF = "EXTRA_GIF"
        const val PROVIDER_AUTHORITIES = "com.elchaninov.gif_searcher.fileProvider"
        const val INTENT_TYPE = "image/gif"
    }
}