package com.example.daggerlegoproject.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.daggerlegoproject.presentations.FirstFragment.FrontFragment
import com.example.daggerlegoproject.presentations.FirstFragment.FrontViewModel
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import kotlin.reflect.KClass

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(FrontViewModel::class)
    abstract fun bindFrontFragmentViewModel(frontViewModel: FrontViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}

@MustBeDocumented
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)

@Retention
@MapKey
annotation class ViewModelKey(val value: KClass<out ViewModel>)