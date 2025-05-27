package org.courselab.app.di

import org.courselab.app.ui.screens.sign_in.LogInViewModel
import org.courselab.app.ui.screens.sign_up.SignUpViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val viewModelModules = module {
    factoryOf(::LogInViewModel)
    factoryOf(::SignUpViewModel)
}