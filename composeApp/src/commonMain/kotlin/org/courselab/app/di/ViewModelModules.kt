package org.courselab.app.di

import org.courselab.app.ui.screens.onboarding.UserProfileViewModel
import org.courselab.app.ui.screens.onboarding.UserViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import org.courselab.app.ui.screens.log_in.LogInViewModel
import org.courselab.app.ui.screens.sign_up.SignUpViewModel
import org.courselab.app.org.courselab.app.ui.screens.onboarding.MunicipioSearchViewModel

val viewModelModules = module {
    factoryOf(::SignUpViewModel)
    factoryOf(::MunicipioSearchViewModel)
    single { LogInViewModel( get() ) }
    single { UserProfileViewModel() }


    // single { UserViewModel(get()) } cºrea una única instancia de UserViewModel (singleton)
    // que se reutilizará en toda la aplicación. Esto es apropiado para ViewModels que
    // gestionan datos globales o de sesión del usuario que deben ser consistentes.
    single { UserViewModel(get(), get()) }
}