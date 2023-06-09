package com.dashkovskiy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.dashkovskiy.ui.events.Event
import com.dashkovskiy.ui.events.EventsContainer
import com.dashkovskiy.ui.navigation.AppNavigation
import com.dashkovskiy.ui.theme.AddForSaleAppTheme
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val eventsContainer: EventsContainer by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AddForSaleAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val scaffoldState = rememberScaffoldState()
                    LaunchedEffect(this) {
                        eventsContainer.events.collect { event ->
                            when (event) {
                                is Event.SnackBar -> {
                                    scaffoldState.snackbarHostState.showSnackbar(message = event.msg)
                                }
                            }
                        }
                    }
                    Scaffold(scaffoldState = scaffoldState) { paddingValues ->
                        AppNavigation(
                            modifier = Modifier.padding(paddingValues)
                        )
                    }
                }
            }
        }
    }
}