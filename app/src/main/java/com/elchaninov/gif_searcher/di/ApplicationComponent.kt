package com.elchaninov.gif_searcher.di

import android.content.Context
import com.elchaninov.gif_searcher.ui.CategoriesActivity
import com.elchaninov.gif_searcher.ui.MainActivity
import com.elchaninov.gif_searcher.ui.ShowingGifActivity
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
    fun inject(mainActivity: MainActivity)
    fun inject(showingGifActivity: ShowingGifActivity)
}