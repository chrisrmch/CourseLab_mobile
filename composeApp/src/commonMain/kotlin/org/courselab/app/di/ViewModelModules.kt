package org.courselab.app.di

import org.courselab.app.ui.screens.home.ActivityViewModelCommon
import org.courselab.app.ui.screens.onboarding.viewModel.MunicipioSearchViewModel
import org.courselab.app.ui.screens.onboarding.viewModel.UserProfileViewModel
import org.courselab.app.ui.screens.onboarding.viewModel.UserViewModel
import org.courselab.app.ui.screens.profile.UserPageStateViewModel
import org.courselab.app.ui.screens.sign_in.LogInViewModel
import org.courselab.app.ui.screens.sign_up.SignUpViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val viewModelModules = module {
    factoryOf(::SignUpViewModel)
    factoryOf(::MunicipioSearchViewModel)
    factoryOf(::LogInViewModel)
    single { UserProfileViewModel(get(), get()) }
    single { UserViewModel(get(), get()) }
    single { ActivityViewModelCommon(get(), get()) }
    single { UserPageStateViewModel(get(), get()) }
}