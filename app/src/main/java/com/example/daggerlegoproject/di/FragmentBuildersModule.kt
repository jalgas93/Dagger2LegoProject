package com.example.daggerlegoproject.di

import com.example.daggerlegoproject.presentations.FirstFragment.FrontFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract fun contributeFrontFragment():FrontFragment
}