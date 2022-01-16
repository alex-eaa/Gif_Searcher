package com.elchaninov.gif_searcher.di

import android.content.Context
import com.elchaninov.gif_searcher.viewModel.MainViewModel
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        MapperModule::class,
        RetrofitModule::class,
        RepositoryModule::class
    ]
)
interface ApplicationComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun setContext(context: Context): Builder
        fun build(): ApplicationComponent
    }

    fun inject(mainViewModel: MainViewModel)
}