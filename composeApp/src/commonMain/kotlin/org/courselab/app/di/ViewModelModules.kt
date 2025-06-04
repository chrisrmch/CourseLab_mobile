package org.courselab.app.di

import androidx.navigation.NavHostController
import org.courselab.app.ui.screens.sign_in.LogInViewModel
import org.courselab.app.ui.screens.sign_up.SignUpViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val viewModelModules = module {
    factoryOf(::LogInViewModel)
    factoryOf(::SignUpViewModel)
}

//val navigationModule = module {
//    single<AppNavigator> { (navController: NavHostController) ->
//        ComposeAppNavigator(navController)
//    }
//}
