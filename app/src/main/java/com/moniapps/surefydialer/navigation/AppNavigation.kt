package com.moniapps.surefydialer.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.moniapps.surefydialer.screens.CallScreen.CallScreen
import com.moniapps.surefydialer.screens.contacts.ContactScreen
import com.moniapps.surefydialer.screens.recents.RecentScreen

@Composable
fun AppNavigation(modifier: Modifier = Modifier, navHostController: NavHostController) {
    NavHost(
        navController = navHostController,
        startDestination = ScreenGraph.RecentScreen.route
    ) {
        composable(route = ScreenGraph.RecentScreen.route) {
            RecentScreen(modifier = modifier)
        }
        composable(route = ScreenGraph.ContactScreen.route) {
            ContactScreen(modifier = modifier)
        }
        composable(
            route = ScreenGraph.CallScreen.route,
            arguments = listOf(
                navArgument("phoneNumber") {
                    type = NavType.StringType
                    nullable = true
                }
            )
        ) { backStackEntry ->
            val number = backStackEntry.arguments?.getString("phoneNumber")
            if (number != null) {
                CallScreen(
                    modifier = modifier,
                    number = number,
                    navController = navHostController
                )
            }
        }
    }
}