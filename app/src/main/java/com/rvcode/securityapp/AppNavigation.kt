package com.rvcode.securityapp

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rvcode.securityapp.screens.PermissionRequestScreen
import com.rvcode.securityapp.screens.PermissionScannerScreen

import com.rvcode.securityapp.screens.SplashScreen
import com.rvcode.securityapp.util.Destination


@Composable
fun AppNavigation(){
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Destination.Splash
    ){

        composable <Destination.Splash>{
            SplashScreen(
                navigateForRequestPermission = {
                    navController.navigate(Destination.RequestPermission){
                        popUpTo<Destination.Splash>{
                            inclusive=true
                        }
                        launchSingleTop=true
                    }
                },
                navigateForPermissionScanner = {
                    navController.navigate(Destination.Permission){
                        popUpTo<Destination.Splash>{
                            inclusive=true
                        }
                        launchSingleTop=true
                    }
                }
            )
        }


        composable<Destination.RequestPermission> {
            PermissionRequestScreen(
                onAllPermissionGranted = {
                        navController.navigate(Destination.Permission){
                            popUpTo<Destination.RequestPermission>{
                                inclusive=true
                            }
                            launchSingleTop=true
                        }
                },
                notAllPermissionGranted = {
                    navController.navigate(Destination.Splash){
                        popUpTo<Destination.RequestPermission>{
                            inclusive =true
                        }
                        launchSingleTop=true
                    }
            }
            )
        }
        composable <Destination.Permission>{
            PermissionScannerScreen()
        }
    }
}