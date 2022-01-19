package com.elchaninov.gif_searcher.ui

import android.content.ContextWrapper
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider.getUriForFile
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.elchaninov.gif_searcher.R
import com.elchaninov.gif_searcher.databinding.GifActivityBinding
import com.elchaninov.gif_searcher.model.Gif
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.File
import java.io.FileOutputStream
import java.net.URL


class ShowingGifActivity : AppCompatActivity() {

    private lateinit var binding: GifActivityBinding
    private val mDisposable = CompositeDisposable()
    private var gif: Gif? = null

    private val requestListener: RequestListener<GifDrawable> =
        object : RequestListener<GifDrawable> {
            override fun onLoadFailed(
                e: GlideException?, model: Any?,
                target: Target<GifDrawable>?,
                isFirstResource: Boolean,
            ): Boolean {
                binding.progressContainer.progress.hide()
                showError()
                return false
            }

            override fun onResourceReady(
                resource: GifDrawable?,
                model: Any?,
                target: Target<GifDrawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean,
            ): Boolean {
                binding.progressContainer.progress.hide()
                binding.fab.show()
                return false
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = GifActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        gif = intent.getParcelableExtra(EXTRA_GIF)

        binding.fab.setOnClickListener { shareGif() }
        binding.fab.hide()

        renderGif()
    }

    private fun renderGif() {
        gif?.let { gif ->
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
    }

    private fun shareGif() {
        startSavingGifToCache()

        val contentUri: Uri =
            getUriForFile(this, PROVIDER_AUTHORITIES, getSharedFile())

        val shareIntent: Intent = Intent().apply {
            flags = Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, contentUri)
            type = INTENT_TYPE
        }

        val chooser = Intent.createChooser(shareIntent, null)
        chooser.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
        startActivity(chooser)
    }

    private fun startSavingGifToCache() {
        gif?.let { gif ->
            mDisposable.add(createRxSavingGif(gif)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({},
                    {
                        showError()
                    }
                )
            )
        }
    }

    private fun createRxSavingGif(gif: Gif): Single<File> {
        return Single.create { emitter ->
            try {
                val file = getSharedFile()
                URL(gif.urlView).openStream().use { input ->
                    FileOutputStream(file).use { output ->
                        input.copyTo(output)
                    }
                }
                emitter.onSuccess(file)
            } catch (e: Exception) {
                emitter.onError(e)
            }
        }
    }

    private fun getSharedFile(): File {
        val imagePath: File = ContextWrapper(this).cacheDir
        return File(imagePath, NAME_SHARING_FILE)
    }

    private fun showError() {
        gif?.let {
            binding.root.showSnackbar(
                text = "Не удалось загрузить изображение",
                action = { renderGif() }
            )
        }
    }

    override fun onDestroy() {
        mDisposable.dispose()
        super.onDestroy()
    }

    companion object {
        const val EXTRA_GIF = "EXTRA_GIF"
        const val NAME_SHARING_FILE = "sharedImage.gif"
        const val PROVIDER_AUTHORITIES = "com.elchaninov.gif_searcher.fileProvider"
        const val INTENT_TYPE = "image/gif"
    }
}