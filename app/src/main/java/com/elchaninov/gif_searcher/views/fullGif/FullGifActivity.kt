package com.elchaninov.gif_searcher.views.fullGif

import android.content.ClipData
import android.content.ContextWrapper
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider.getUriForFile
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.elchaninov.gif_searcher.BuildConfig
import com.elchaninov.gif_searcher.R
import com.elchaninov.gif_searcher.databinding.GifActivityBinding
import com.elchaninov.gif_searcher.model.data.LoadingState
import com.elchaninov.gif_searcher.model.data.userdata.Gif
import com.elchaninov.gif_searcher.utils.hide
import com.elchaninov.gif_searcher.utils.parcelable
import com.elchaninov.gif_searcher.utils.show
import com.elchaninov.gif_searcher.utils.showSnackbar
import com.elchaninov.gif_searcher.utils.slideIn
import com.elchaninov.gif_searcher.viewModel.FullGifViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileInputStream
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FullGifActivity : AppCompatActivity() {

    private val viewModel: FullGifViewModel by viewModels()

    private lateinit var binding: GifActivityBinding
    private var gif: Gif? = null
    private var gifFile: File? = null
    private var adView: AdView? = null

    private val saveLauncher =
        registerForActivityResult(ActivityResultContracts.CreateDocument()) { uri ->
            try {
                if (uri != null) {
                    gifFile?.let { gifFile ->
                        saveFile(uri, gifFile)
                        this.gifFile = null
                        showMessage(R.string.success_save_file)
                    }
                }
            } catch (e: Exception) {
                showMessage(R.string.cant_save_file)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = GifActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (BuildConfig.ALLOW_AD) bannerAdsInit()
        gif = intent.parcelable(EXTRA_GIF)
        if (savedInstanceState == null) fetchGif()

        viewModel.fileLiveData.observe(this) { cachingState ->
            when (cachingState) {
                is LoadingState.Success -> {
                    binding.progress.hide()
                    renderGif(cachingState.data)
                    with(binding.fabSearch) {
                        setOnClickListener { shareGif(cachingState.data) }
                        slideIn(resources.getDimensionPixelSize(R.dimen.margin_fab))
                    }
                    with(binding.fabDownload) {
                        setOnClickListener { saveGifToDownloads(cachingState.data) }
                        slideIn(resources.getDimensionPixelSize(R.dimen.margin_bottom_download_fab))
                    }
                    with(binding.fabFavorites) {
                        setOnClickListener {
                            gif?.let { gif -> viewModel.toggleGifFavorite(gif) }
                        }
                        slideIn(resources.getDimensionPixelSize(R.dimen.margin_bottom_favorite_fab))
                    }
                }
                is LoadingState.Failure -> {
                    binding.progress.hide()
                    showError()
                }
                is LoadingState.Progress -> {
                    binding.progress.show()
                    binding.progressText.text =
                        getString(R.string.progress_percent, cachingState.percent)
                    binding.progressIndicator.progress = cachingState.percent
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isFavoriteGif.collect {
                    if (it) binding.fabFavorites.setImageResource(R.drawable.ic_baseline_favorite_24)
                    else binding.fabFavorites.setImageResource(R.drawable.ic_baseline_favorite_border_24)
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
        binding.layoutBannerHolder.layoutParams.height =
            resources.getDimensionPixelSize(R.dimen.banner_height)
        binding.layoutBannerHolder.addView(adView)
    }

    private fun saveGifToDownloads(gifFile: File) {
        this.gifFile = gifFile
        saveLauncher.launch(gifFile.name)
    }

    private fun saveFile(destUri: Uri, gifFile: File) {
        FileInputStream(gifFile).use { inputStream ->
            contentResolver.openOutputStream(destUri)?.use { outputStream ->
                val buffer = ByteArray(1024)
                var bytesRead: Int
                while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                    outputStream.write(buffer, 0, bytesRead)
                }
            } ?: throw IllegalStateException("Can't open output stream")
        }
    }

    private fun showMessage(@StringRes res: Int) {
        Toast.makeText(this, res, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val EXTRA_GIF = "EXTRA_GIF"
        const val PROVIDER_AUTHORITIES = "com.elchaninov.gif_searcher.fileProvider"
        const val INTENT_TYPE = "video/gif"
    }
}