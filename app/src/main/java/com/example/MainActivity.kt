package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.NavTab
import com.example.model.SubScreen
import com.example.ui.components.TakaEarnBottomNavBar
import com.example.ui.screens.AccountScreen
import com.example.ui.screens.AdsterraScreen
import com.example.ui.screens.AuthScreen
import com.example.ui.screens.CampaignScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.IncomeScreen
import com.example.ui.screens.InfoContentScreen
import com.example.ui.screens.ReferScreen
import com.example.ui.screens.WithdrawScreen
import com.example.ui.screens.getAboutUsItems
import com.example.ui.screens.getFaqItems
import com.example.ui.screens.getPrivacyPolicyItems
import com.example.ui.screens.getTermsItems
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.EmeraldSecondary
import com.example.ui.theme.TakaEarnTheme
import com.example.util.Localization
import com.example.viewmodel.EarnViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: EarnViewModel = viewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val snackbarHostState = remember { SnackbarHostState() }

            LaunchedEffect(uiState.snackbarMessage) {
                uiState.snackbarMessage?.let { message ->
                    snackbarHostState.showSnackbar(message)
                    viewModel.clearSnackbar()
                }
            }

            // Handle hardware/gesture back button for sub-screens
            if (uiState.activeSubScreen != SubScreen.NONE) {
                BackHandler {
                    viewModel.navigateBackFromSubScreen()
                }
            }

            TakaEarnTheme(darkTheme = uiState.isDarkMode) {
                if (!uiState.isLoggedIn) {
                    // Auth Flow (Login & Registration)
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        snackbarHost = {
                            SnackbarHost(hostState = snackbarHostState) { data ->
                                Snackbar(
                                    snackbarData = data,
                                    containerColor = EmeraldPrimary,
                                    contentColor = Color.White,
                                    actionColor = EmeraldSecondary
                                )
                            }
                        }
                    ) { innerPadding ->
                        AuthScreen(
                            language = uiState.language,
                            authMode = uiState.authMode,
                            onSetAuthMode = { viewModel.setAuthMode(it) },
                            onLogin = { identifier, pass -> viewModel.login(identifier, pass) },
                            onRegister = { name, phone, email, pass -> viewModel.register(name, phone, email, pass) },
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                } else if (uiState.activeSubScreen != SubScreen.NONE) {
                    // Dedicated Sub-Screens (Withdraw, Adsterra, Privacy, About, Terms, FAQ)
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        snackbarHost = {
                            SnackbarHost(hostState = snackbarHostState) { data ->
                                Snackbar(
                                    snackbarData = data,
                                    containerColor = EmeraldPrimary,
                                    contentColor = Color.White,
                                    actionColor = EmeraldSecondary
                                )
                            }
                        }
                    ) { innerPadding ->
                        when (uiState.activeSubScreen) {
                            SubScreen.WITHDRAW -> {
                                WithdrawScreen(
                                    balance = uiState.userStats.balance,
                                    language = uiState.language,
                                    currency = uiState.currency,
                                    transactions = uiState.transactions,
                                    onBack = { viewModel.navigateBackFromSubScreen() },
                                    onSubmitCashout = { method, accountNumber, amount ->
                                        viewModel.requestCashout(method, accountNumber, amount)
                                    },
                                    modifier = Modifier.padding(innerPadding)
                                )
                            }
                            SubScreen.ADSTERRA -> {
                                AdsterraScreen(
                                    adsterraStats = uiState.adsterraStats,
                                    language = uiState.language,
                                    onBack = { viewModel.navigateBackFromSubScreen() },
                                    onConnectApiKey = { apiKey -> viewModel.connectAdsterra(apiKey) },
                                    onRefreshStats = { viewModel.refreshAdsterraStats() },
                                    modifier = Modifier.padding(innerPadding)
                                )
                            }
                            SubScreen.PRIVACY -> {
                                InfoContentScreen(
                                    title = Localization.privacyPolicy(uiState.language),
                                    icon = Icons.Default.Policy,
                                    language = uiState.language,
                                    onBack = { viewModel.navigateBackFromSubScreen() },
                                    items = getPrivacyPolicyItems(uiState.language),
                                    modifier = Modifier.padding(innerPadding)
                                )
                            }
                            SubScreen.ABOUT -> {
                                InfoContentScreen(
                                    title = Localization.aboutUs(uiState.language),
                                    icon = Icons.Default.Info,
                                    language = uiState.language,
                                    onBack = { viewModel.navigateBackFromSubScreen() },
                                    items = getAboutUsItems(uiState.language),
                                    modifier = Modifier.padding(innerPadding)
                                )
                            }
                            SubScreen.TERMS -> {
                                InfoContentScreen(
                                    title = Localization.termsOfService(uiState.language),
                                    icon = Icons.Default.Security,
                                    language = uiState.language,
                                    onBack = { viewModel.navigateBackFromSubScreen() },
                                    items = getTermsItems(uiState.language),
                                    modifier = Modifier.padding(innerPadding)
                                )
                            }
                            SubScreen.FAQ -> {
                                InfoContentScreen(
                                    title = Localization.faq(uiState.language),
                                    icon = Icons.AutoMirrored.Filled.Help,
                                    language = uiState.language,
                                    onBack = { viewModel.navigateBackFromSubScreen() },
                                    items = getFaqItems(uiState.language),
                                    modifier = Modifier.padding(innerPadding)
                                )
                            }
                            SubScreen.NONE -> {}
                        }
                    }
                } else {
                    // Main Tabs Navigation Flow
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        bottomBar = {
                            TakaEarnBottomNavBar(
                                currentTab = uiState.currentTab,
                                language = uiState.language,
                                onTabSelected = { tab ->
                                    viewModel.selectTab(tab)
                                }
                            )
                        },
                        snackbarHost = {
                            SnackbarHost(hostState = snackbarHostState) { data ->
                                Snackbar(
                                    snackbarData = data,
                                    containerColor = EmeraldPrimary,
                                    contentColor = Color.White,
                                    actionColor = EmeraldSecondary
                                )
                            }
                        }
                    ) { innerPadding ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                        ) {
                            Crossfade(
                                targetState = uiState.currentTab,
                                label = "tabCrossfade"
                            ) { tab ->
                                when (tab) {
                                    NavTab.HOME -> {
                                        HomeScreen(
                                            userStats = uiState.userStats,
                                            language = uiState.language,
                                            currency = uiState.currency,
                                            onNavigateTab = { viewModel.selectTab(it) },
                                            onWithdrawClick = { viewModel.navigateToSubScreen(SubScreen.WITHDRAW) },
                                            onToggleLanguage = { viewModel.toggleLanguage() }
                                        )
                                    }
                                    NavTab.INCOME -> {
                                        IncomeScreen(
                                            campaigns = uiState.campaigns,
                                            currentAdIndex = uiState.currentAdIndex,
                                            timerProgress = uiState.timerProgress,
                                            remainingSeconds = uiState.remainingSeconds,
                                            elapsedSeconds = uiState.elapsedSeconds,
                                            isImageClickedInCurrentCycle = uiState.isImageClickedInCurrentCycle,
                                            isTimerRunning = uiState.isTimerRunning,
                                            language = uiState.language,
                                            currency = uiState.currency,
                                            onAdImageClicked = { context ->
                                                viewModel.onAdImageClicked(context)
                                            },
                                            onSkipAd = { viewModel.skipAd() },
                                            onTogglePause = { viewModel.toggleTimerPause() },
                                            onToggleLanguage = { viewModel.toggleLanguage() }
                                        )
                                    }
                                    NavTab.CAMPAIGN -> {
                                        CampaignScreen(
                                            language = uiState.language,
                                            onSubmitCampaign = { title, targetUrl, imageUrl, reward, presetRes ->
                                                viewModel.addCampaign(title, targetUrl, imageUrl, reward, presetRes)
                                            },
                                            onToggleLanguage = { viewModel.toggleLanguage() }
                                        )
                                    }
                                    NavTab.REFER -> {
                                        ReferScreen(
                                            referralLink = uiState.userStats.referralLink,
                                            referralCode = uiState.userStats.referralCode,
                                            totalRefers = uiState.userStats.totalRefers,
                                            referEarnings = uiState.userStats.referEarnings,
                                            referralFriends = uiState.referralFriends,
                                            language = uiState.language,
                                            onCopyLink = { context -> viewModel.copyReferralLink(context) },
                                            onShareLink = { context -> viewModel.shareReferralLink(context) },
                                            onToggleLanguage = { viewModel.toggleLanguage() }
                                        )
                                    }
                                    NavTab.ACCOUNT -> {
                                        AccountScreen(
                                            userStats = uiState.userStats,
                                            isDarkMode = uiState.isDarkMode,
                                            language = uiState.language,
                                            currency = uiState.currency,
                                            transactions = uiState.transactions,
                                            onToggleDarkMode = { viewModel.toggleDarkMode() },
                                            onToggleLanguage = { viewModel.toggleLanguage() },
                                            onSelectCurrency = { viewModel.setCurrency(it) },
                                            onNavigateToSubScreen = { subScreen ->
                                                viewModel.navigateToSubScreen(subScreen)
                                            },
                                            onLogout = { viewModel.logout() }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

