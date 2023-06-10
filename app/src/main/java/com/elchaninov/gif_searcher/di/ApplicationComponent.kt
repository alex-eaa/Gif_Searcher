package com.elchaninov.gif_searcher.di

import android.content.Context
import com.elchaninov.gif_searcher.ui.FullGifActivity
import com.elchaninov.gif_searcher.ui.categories.CategoriesActivity
import com.elchaninov.gif_searcher.ui.favorites.FavoritesActivity
import com.elchaninov.gif_searcher.ui.gifs.GifsActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ViewModelModule::class,
        PagingModule::class,
        RepositoryModule::class,
        RetrofitModule::class,
        RoomModule::class,
    ]
)
interface ApplicationComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun setContext(context: Context): Builder
        fun build(): ApplicationComponent
    }

    fun inject(categoryActivity: CategoriesActivity)
    fun inject(gifsActivity: GifsActivity)
    fun inject(fullGifActivity: FullGifActivity)
    fun inject(favoritesActivity: FavoritesActivity)
}