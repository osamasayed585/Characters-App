package com.yassir.characters

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.yassir.design.theme.RMCTheme
import com.yassir.navigation.richMortCharacters.MyRMCApp
import com.yassir.navigation.util.networkMonitor.NetworkMonitor
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var networkMonitor: NetworkMonitor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RMCTheme {
                val navController = rememberNavController()
                MyRMCApp(networkMonitor, navController)
            }
        }
    }
}