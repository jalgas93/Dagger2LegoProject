package com.example.daggerlegoproject.di

import com.example.daggerlegoproject.presentations.FirstFragment.FrontFragment
import dagger.Module
@Suppress("unused")
@Module
abstract class FragmentBuildersModule {
    abstract fun contributeFrontFragment():FrontFragment
}