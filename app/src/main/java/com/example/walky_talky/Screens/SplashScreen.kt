package com.example.walky_talky.Screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.R
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay

@SuppressLint("InvalidColorHexValue")
@Composable
fun SplashScreen(navHostController: NavHostController,modifier: Modifier = Modifier){

        Scaffold { innerPadding->
            Box (
                modifier = Modifier.padding(innerPadding)
                    .fillMaxSize()
                    .background
                        (color = Color(0xff737b4c))
            ){
                Image(modifier = Modifier.align(Alignment.Center),
                    painter = painterResource(id = com.example.walky_talky.R.drawable.logo),
                    contentDescription = "Logo"
                )
            }
            LaunchedEffect(Unit) {
                delay(2500)
                navHostController.navigate("homeScreen"){
                    popUpTo("Splash"){
                        inclusive = true
                    }

                }
            }
        }
    }
