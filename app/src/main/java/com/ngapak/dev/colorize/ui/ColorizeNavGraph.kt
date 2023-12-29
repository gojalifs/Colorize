package com.ngapak.dev.colorize.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ngapak.dev.colorize.injection.Injection
import com.ngapak.dev.colorize.ui.factory.IshiharaViewModelFactory
import com.ngapak.dev.colorize.ui.screen.home.HomeScreen
import com.ngapak.dev.colorize.ui.screen.ishihara_test.IshiharaTestHomeScreen
import com.ngapak.dev.colorize.ui.screen.ishihara_test.IshiharaTestScreen
import com.ngapak.dev.colorize.ui.screen.ishihara_test.IshiharaViewModel
import com.ngapak.dev.colorize.ui.screen.ishihara_test.ResultScreen

@Composable
fun ColorizeNavGraph(
    navController: NavHostController
) {
    val ishiharaViewModel: IshiharaViewModel = viewModel(
        factory = IshiharaViewModelFactory(Injection.provideIshiharaRepository(LocalContext.current))
    )
    NavHost(
        navController = navController,
        startDestination = ColorizeNavigation.HOME_ROUTE
    ) {
        composable(ColorizeNavigation.HOME_ROUTE) {
            HomeScreen(onclick = { navController.navigate(ColorizeNavigation.ISHIHARA_HOME_ROUTE) })
        }
        /*
        * Ishihara Test Screen
        * */
        composable(ColorizeNavigation.ISHIHARA_HOME_ROUTE) {
            IshiharaTestHomeScreen(
                { navController.navigate(ColorizeNavigation.ISHIHARA_TEST_ROUTE) },
                viewModel = ishiharaViewModel
            )
        }
        composable(ColorizeNavigation.ISHIHARA_TEST_ROUTE) {
            IshiharaTestScreen(
                {
                    navController.navigate(
                        ColorizeNavigation.RESULT_ROUTE,
                    ) {
                        popUpTo(ColorizeNavigation.HOME_ROUTE)
                    }
                },
                viewModel = ishiharaViewModel
            )
        }

        /*
        * Result Page
        * */
        composable(ColorizeNavigation.RESULT_ROUTE) {
            ResultScreen(viewModel = ishiharaViewModel,
                onClick = { navController.navigateUp() }
            )
        }
    }
}
