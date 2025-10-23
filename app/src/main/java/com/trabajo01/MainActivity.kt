package com.trabajo01

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.trabajo01.navigation.NavGraph
import com.trabajo01.ui.theme.Trabajo01_2025Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Trabajo01_2025Theme {
                val navController = rememberNavController()
                NavGraph(navController = navController)
            }
        }
    }
}