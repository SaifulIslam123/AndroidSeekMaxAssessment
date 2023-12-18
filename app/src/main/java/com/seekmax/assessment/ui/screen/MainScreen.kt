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
import com.seekmax.assessment.ui.screen.home.HomeScreen
import com.seekmax.assessment.ui.screen.jobdetail.JobDetailScreen
import com.seekmax.assessment.ui.screen.login.LoginScreen
import com.seekmax.assessment.ui.screen.myjobs.MyJobListScreen
import com.seekmax.assessment.ui.screen.profile.ProfileScreen
import com.seekmax.assessment.ui.theme.textPrimary
import com.seekmax.assessment.ui.theme.textSecondary


sealed class BottomNavigationScreens(
    val routePrefix: String = "",
    val route: String,
    val name: String
) {
    object Home :
        BottomNavigationScreens(route = "home", name = "Home")

    object Profile :
        BottomNavigationScreens(route = "profile", name = "Profile")

    object Login : BottomNavigationScreens(route = "login", name = "Login")
    object JobDetail :
        BottomNavigationScreens("jobdetail/", "jobdetail/{jobId}", name = "Job Detail")

    object MyJobList : BottomNavigationScreens(route = "myjoblist", name = "My Job")

}

@Composable
private fun AppBottomNavigation(
    navController: NavHostController,
    items: List<BottomNavigationScreens>
) {
    BottomNavigation(backgroundColor = textPrimary) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { screen ->
            BottomNavigationItem(
                icon = { },
                label = { Text(screen.name) },
                selectedContentColor = Color.White,
                unselectedContentColor = textSecondary,
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
        BottomNavigationScreens.Profile,
        BottomNavigationScreens.MyJobList
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
        composable(BottomNavigationScreens.Profile.route) { ProfileScreen(navController = navController) }
        composable(BottomNavigationScreens.Login.route) { LoginScreen(navController = navController) }
        composable(BottomNavigationScreens.JobDetail.route) { backStackEntry ->
            JobDetailScreen(
                navController = navController,
                jobId = backStackEntry.arguments?.getString(
                    "jobId"/*BottomNavigationScreens.JobDetail.name*/, ""
                ) ?: ""
            )
        }
        composable(BottomNavigationScreens.MyJobList.route) { MyJobListScreen(navController = navController) }
    }
}