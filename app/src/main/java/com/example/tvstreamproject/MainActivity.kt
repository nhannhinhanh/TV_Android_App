package com.example.tvstreamproject

import androidx.activity.compose.setContent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tvstreamproject.ui.theme.TVApplicationTheme
import com.example.tvstreamproject.ui.DetailScreen
import com.example.tvstreamproject.ui.HomeScreen
import com.example.tvstreamproject.ui.SearchScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TVApplicationTheme(darkTheme = true) {
                var screenState by remember { mutableStateOf<ScreenState>(ScreenState.Home) }

                when (val screen = screenState) {
                    is ScreenState.Home -> {
                        Column(modifier = Modifier.fillMaxSize()) {
                            Row(modifier = Modifier.padding(16.dp)) {
                                Button(onClick = { screenState = ScreenState.Search }) {
                                    Text("Search")
                                }
                            }
                            HomeScreen(
                                onSelectSeries = { selectedId ->
                                    screenState = ScreenState.Detail(selectedId)
                                }
                            )
                        }
                    }
                    is ScreenState.Search -> {
                        SearchScreen(
                            onSelectSeries = { selectedId ->
                                screenState = ScreenState.Detail(selectedId)
                            }
                        )
                    }
                    is ScreenState.Detail -> {
                        DetailScreen(
                            seriesId = screen.seriesId,
                            onBack = {
                                screenState = ScreenState.Home
                            }
                        )
                    }
                }
            }
        }
    }
}
