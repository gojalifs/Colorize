package com.ngapak.dev.colorize

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ngapak.dev.colorize.ui.ColorizeNavGraph

@Composable
fun ColorizeApp(
    navController: NavHostController = rememberNavController()
) {
    ColorizeNavGraph(navController = navController)
}