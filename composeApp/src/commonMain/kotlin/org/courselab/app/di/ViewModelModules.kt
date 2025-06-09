package org.courselab.app.di

import org.courselab.app.org.courselab.app.ui.screens.onboarding.MunicipioSearchViewModel
import org.courselab.app.ui.screens.onboarding.UserViewModel
import org.courselab.app.ui.screens.sign_in.LogInViewModel
import org.courselab.app.ui.screens.sign_up.SignUpViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val viewModelModules = module {
    // factoryOf(::LogInViewModel) crea una nueva instancia de LogInViewModel cada vez que se solicita.
    // Esto es útil para ViewModels que están ligados al ciclo de vida de una pantalla específica
    // y no necesitan mantener su estado más allá de esa pantalla.
    factoryOf(::LogInViewModel)
    factoryOf(::SignUpViewModel)
    factoryOf(::MunicipioSearchViewModel)

    // single { UserViewModel(get()) } cºrea una única instancia de UserViewModel (singleton)
    // que se reutilizará en toda la aplicación. Esto es apropiado para ViewModels que
    // gestionan datos globales o de sesión del usuario que deben ser consistentes.
    single { UserViewModel(get(), get())}
}