package com.example.heimdallgp_livedata

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.heimdallgp_livedata.ui.MainScreen
import com.example.heimdallgp_livedata.ui.theme.HeimdallGPLiveDataTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HeimdallGPLiveDataTheme {
                MainScreen()
            }
        }
    }
}