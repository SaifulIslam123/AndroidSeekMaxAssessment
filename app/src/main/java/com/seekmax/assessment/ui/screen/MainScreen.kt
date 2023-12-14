package com.seekmax.assessment.ui.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.seekmax.assessment.R


sealed class BottomNavigationScreens(val route: String, val name: String, val icon: Int) {
    object Home :
        BottomNavigationScreens("home", "Home", R.drawable.ic_edit)

    object Profile :
        BottomNavigationScreens("profile", "Profile", R.drawable.ic_edit)
}

@Composable
private fun AppBottomNavigation(
    navController: NavHostController,
    items: List<BottomNavigationScreens>
) {
    BottomNavigation {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { screen ->
            BottomNavigationItem(
                icon = { },
                label = { Text(screen.name) },
                selectedContentColor = Color.White,
                unselectedContentColor = Color.Green,
                alwaysShowLabel = true,
                selected = currentRoute == screen.route,
                onClick = {
                    // This if check gives us a "singleTop" behavior where we do not create a
                    // second instance of the composable if we are already on that destination
                    /*if (currentRoute != screen.route) {
                        navController.navigate(screen.route)
                    }*/
                    navController.navigate(screen.route) {

                        navController.graph.startDestinationRoute?.let { screen_route ->
                            popUpTo(screen_route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

/*@Composable
private fun currentRoute(navController: NavHostController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.arguments?.getString(KEY_ROUTE)
}*/


@Composable
fun MainScreen() {
    val bottomNavigationItems = listOf(
        BottomNavigationScreens.Home,
        BottomNavigationScreens.Profile
    )
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            AppBottomNavigation(navController, bottomNavigationItems)
        },
        content = {
            Surface(modifier = Modifier.padding(it)) {
                MainScreenNavigationConfigurations(navController)
            }
        }
    )
}

@Composable
private fun MainScreenNavigationConfigurations(
    navController: NavHostController
) {
    NavHost(navController, startDestination = BottomNavigationScreens.Home.route) {
        composable(BottomNavigationScreens.Home.route) { HomeScreen(navController = navController) }
        composable(BottomNavigationScreens.Profile.route) { HomeScreen(navController = navController) }
        composable("users/{userId}") { backStackEntry ->
            UserDetailScreen(
                navController,
                (backStackEntry.arguments?.getString("userId", "")
                    ?: "").toLong()
            )
        }

    }
}