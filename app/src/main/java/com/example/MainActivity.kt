package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.notifications.ReminderNotificationHelper
import com.example.ui.screens.AnalysisScreen
import com.example.ui.screens.AnalyticsScreen
import com.example.ui.screens.CreateRoomScreen
import com.example.ui.screens.GamePlayScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.JoinRoomScreen
import com.example.ui.screens.MemoryMatchScreen
import com.example.ui.screens.PracticeScreen
import com.example.ui.screens.QuestionBankScreen
import com.example.ui.screens.ResultsScreen
import com.example.ui.screens.RetrievalScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.WaitingRoomScreen
import com.example.ui.screens.WeakSpotsScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodels.ChemViewModel
import com.example.ui.viewmodels.ScreenState

class MainActivity : ComponentActivity() {
    private val chemViewModel: ChemViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Create notification channel
        ReminderNotificationHelper.createNotificationChannel(this)

        setContent {
            val userProfile by chemViewModel.userProfile.collectAsState()
            val themePreference = userProfile?.themeMode ?: "system"
            val isDark = when (themePreference) {
                "dark" -> true
                "light" -> false
                else -> isSystemInDarkTheme()
            }

            MyApplicationTheme(darkTheme = isDark) {
                ChemArenaApp(viewModel = chemViewModel)
            }
        }
    }
}

@Composable
fun ChemArenaApp(viewModel: ChemViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val toastMessage by viewModel.toastMessage.collectAsState()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(toastMessage) {
        toastMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    // System Back Press Handling
    BackHandler(enabled = currentScreen != ScreenState.HOME) {
        when (currentScreen) {
            ScreenState.HOME -> { /* Exit handled by OS */ }
            ScreenState.RESULTS -> viewModel.navigateTo(ScreenState.HOME)
            ScreenState.RETRIEVAL_MODE -> viewModel.navigateTo(ScreenState.HOME)
            ScreenState.ANALYSIS_MODE -> viewModel.navigateTo(ScreenState.HOME)
            ScreenState.WEAK_SPOTS_MODE -> viewModel.navigateTo(ScreenState.HOME)
            ScreenState.PRACTICE -> viewModel.navigateTo(ScreenState.HOME)
            ScreenState.MEMORY_MATCH -> viewModel.navigateTo(ScreenState.HOME)
            ScreenState.GAME_PLAY -> viewModel.leaveMatch()
            ScreenState.WAITING_ROOM -> viewModel.leaveMatch()
            ScreenState.CREATE_ROOM -> viewModel.navigateTo(ScreenState.HOME)
            ScreenState.JOIN_ROOM -> viewModel.navigateTo(ScreenState.HOME)
            ScreenState.QUESTION_BANK -> viewModel.navigateTo(ScreenState.HOME)
            ScreenState.ANALYTICS -> viewModel.navigateTo(ScreenState.HOME)
            ScreenState.SETTINGS -> viewModel.navigateTo(ScreenState.HOME)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                ScreenState.HOME -> HomeScreen(viewModel = viewModel)
                ScreenState.RETRIEVAL_MODE -> RetrievalScreen(viewModel = viewModel)
                ScreenState.ANALYSIS_MODE -> AnalysisScreen(viewModel = viewModel)
                ScreenState.WEAK_SPOTS_MODE -> WeakSpotsScreen(viewModel = viewModel)
                ScreenState.PRACTICE -> PracticeScreen(viewModel = viewModel)
                ScreenState.MEMORY_MATCH -> MemoryMatchScreen(viewModel = viewModel)
                ScreenState.GAME_PLAY -> GamePlayScreen(viewModel = viewModel)
                ScreenState.CREATE_ROOM -> CreateRoomScreen(viewModel = viewModel)
                ScreenState.JOIN_ROOM -> JoinRoomScreen(viewModel = viewModel)
                ScreenState.WAITING_ROOM -> WaitingRoomScreen(viewModel = viewModel)
                ScreenState.RESULTS -> ResultsScreen(viewModel = viewModel)
                ScreenState.QUESTION_BANK -> QuestionBankScreen(viewModel = viewModel)
                ScreenState.ANALYTICS -> AnalyticsScreen(viewModel = viewModel)
                ScreenState.SETTINGS -> SettingsScreen(viewModel = viewModel)
            }
        }
    }
}
