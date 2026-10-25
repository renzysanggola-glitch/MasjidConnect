package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.ui.components.FloatingBottomBar
import com.example.ui.screens.*
import com.example.ui.theme.BackgroundWarm
import com.example.ui.theme.GreenPrimary
import com.example.ui.theme.MyApplicationTheme
import kotlinx.coroutines.launch

enum class AppScreen {
    LOGIN,
    DASHBOARD,
    KEUANGAN,
    AGENDA,
    PENGATURAN,
    TAMBAH_TRANSAKSI,
    LAPORAN_KEUANGAN,
    PROFIL_MASJID,
    PENGATURAN_NOTIFIKASI,
    KONFIGURASI_JADWAL_SALAT,
    MANAJEMEN_USER,
    KEAMANAN_PASSWORD,
    DIGITAL_SIGNAGE,
    TENTANG_APLIKASI
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MasjidConnectApp()
            }
        }
    }
}

@Composable
fun MasjidConnectApp() {
    var currentScreen by remember { mutableStateOf(AppScreen.DASHBOARD) }
    var selectedTab by remember { mutableIntStateOf(0) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    fun showToast(msg: String) {
        coroutineScope.launch {
            snackbarHostState.currentSnackbarData?.dismiss()
            snackbarHostState.showSnackbar(
                message = msg,
                duration = SnackbarDuration.Short
            )
        }
    }

    // Handle Hardware / Gesture Back Navigation
    BackHandler(enabled = currentScreen != AppScreen.LOGIN) {
        when (currentScreen) {
            AppScreen.TAMBAH_TRANSAKSI, AppScreen.LAPORAN_KEUANGAN -> {
                currentScreen = AppScreen.KEUANGAN
                selectedTab = 1
            }
            AppScreen.PROFIL_MASJID, AppScreen.PENGATURAN_NOTIFIKASI,
            AppScreen.KONFIGURASI_JADWAL_SALAT, AppScreen.MANAJEMEN_USER,
            AppScreen.KEAMANAN_PASSWORD, AppScreen.DIGITAL_SIGNAGE,
            AppScreen.TENTANG_APLIKASI -> {
                currentScreen = AppScreen.PENGATURAN
                selectedTab = 3
            }
            AppScreen.KEUANGAN, AppScreen.AGENDA, AppScreen.PENGATURAN -> {
                currentScreen = AppScreen.DASHBOARD
                selectedTab = 0
            }
            AppScreen.DASHBOARD -> {
                showLogoutDialog = true
            }
            AppScreen.LOGIN -> {}
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.padding(bottom = if (currentScreen in listOf(AppScreen.DASHBOARD, AppScreen.KEUANGAN, AppScreen.AGENDA, AppScreen.PENGATURAN)) 80.dp else 16.dp)
            ) { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = GreenPrimary,
                    contentColor = Color.White,
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
                )
            }
        },
        containerColor = BackgroundWarm,
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { _ ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundWarm)
        ) {
            // Main Screen Animated Transition
            AnimatedContent(
                targetState = currentScreen,
                transitionSpec = {
                    fadeIn(animationSpec = tween(220)) togetherWith fadeOut(animationSpec = tween(180))
                },
                label = "ScreenTransition"
            ) { screen ->
                when (screen) {
                    AppScreen.LOGIN -> {
                        LoginScreen(
                            onLoginSuccess = {
                                currentScreen = AppScreen.DASHBOARD
                                selectedTab = 0
                            },
                            onShowToast = ::showToast
                        )
                    }
                    AppScreen.DASHBOARD -> {
                        DashboardScreen(
                            onNavigateToKeuangan = {
                                currentScreen = AppScreen.KEUANGAN
                                selectedTab = 1
                            },
                            onNavigateToAgenda = {
                                currentScreen = AppScreen.AGENDA
                                selectedTab = 2
                            },
                            onNavigateToSetting = {
                                currentScreen = AppScreen.PENGATURAN
                                selectedTab = 3
                            },
                            onShowToast = ::showToast
                        )
                    }
                    AppScreen.KEUANGAN -> {
                        KeuanganScreen(
                            onNavigateBack = {
                                currentScreen = AppScreen.DASHBOARD
                                selectedTab = 0
                            },
                            onNavigateToTambahTransaksi = {
                                currentScreen = AppScreen.TAMBAH_TRANSAKSI
                            },
                            onNavigateToLaporan = {
                                currentScreen = AppScreen.LAPORAN_KEUANGAN
                            },
                            onShowToast = ::showToast
                        )
                    }
                    AppScreen.TAMBAH_TRANSAKSI -> {
                        TambahTransaksiScreen(
                            onNavigateBack = {
                                currentScreen = AppScreen.KEUANGAN
                                selectedTab = 1
                            },
                            onShowToast = ::showToast
                        )
                    }
                    AppScreen.LAPORAN_KEUANGAN -> {
                        LaporanKeuanganScreen(
                            onNavigateBack = {
                                currentScreen = AppScreen.KEUANGAN
                                selectedTab = 1
                            },
                            onShowToast = ::showToast
                        )
                    }
                    AppScreen.AGENDA -> {
                        AgendaScreen(
                            onNavigateBack = {
                                currentScreen = AppScreen.DASHBOARD
                                selectedTab = 0
                            },
                            onShowToast = ::showToast
                        )
                    }
                    AppScreen.PENGATURAN -> {
                        PengaturanScreen(
                            onNavigateBack = {
                                currentScreen = AppScreen.DASHBOARD
                                selectedTab = 0
                            },
                            onNavigateToProfilMasjid = { currentScreen = AppScreen.PROFIL_MASJID },
                            onNavigateToNotifikasi = { currentScreen = AppScreen.PENGATURAN_NOTIFIKASI },
                            onNavigateToJadwalSalat = { currentScreen = AppScreen.KONFIGURASI_JADWAL_SALAT },
                            onNavigateToManajemenUser = { currentScreen = AppScreen.MANAJEMEN_USER },
                            onNavigateToKeamanan = { currentScreen = AppScreen.KEAMANAN_PASSWORD },
                            onNavigateToDigitalSignage = { currentScreen = AppScreen.DIGITAL_SIGNAGE },
                            onNavigateToTentang = { currentScreen = AppScreen.TENTANG_APLIKASI },
                            onLogoutClick = { showLogoutDialog = true }
                        )
                    }
                    AppScreen.PROFIL_MASJID -> {
                        ProfilMasjidScreen(
                            onNavigateBack = {
                                currentScreen = AppScreen.PENGATURAN
                                selectedTab = 3
                            },
                            onShowToast = ::showToast
                        )
                    }
                    AppScreen.PENGATURAN_NOTIFIKASI -> {
                        PengaturanNotifikasiScreen(
                            onNavigateBack = {
                                currentScreen = AppScreen.PENGATURAN
                                selectedTab = 3
                            },
                            onShowToast = ::showToast
                        )
                    }
                    AppScreen.KONFIGURASI_JADWAL_SALAT -> {
                        KonfigurasiJadwalSalatScreen(
                            onNavigateBack = {
                                currentScreen = AppScreen.PENGATURAN
                                selectedTab = 3
                            },
                            onShowToast = ::showToast
                        )
                    }
                    AppScreen.MANAJEMEN_USER -> {
                        ManajemenUserScreen(
                            onNavigateBack = {
                                currentScreen = AppScreen.PENGATURAN
                                selectedTab = 3
                            },
                            onShowToast = ::showToast
                        )
                    }
                    AppScreen.KEAMANAN_PASSWORD -> {
                        KeamananPasswordScreen(
                            onNavigateBack = {
                                currentScreen = AppScreen.PENGATURAN
                                selectedTab = 3
                            },
                            onShowToast = ::showToast
                        )
                    }
                    AppScreen.DIGITAL_SIGNAGE -> {
                        DigitalSignageScreen(
                            onNavigateBack = {
                                currentScreen = AppScreen.PENGATURAN
                                selectedTab = 3
                            },
                            onShowToast = ::showToast
                        )
                    }
                    AppScreen.TENTANG_APLIKASI -> {
                        TentangAplikasiScreen(
                            onNavigateBack = {
                                currentScreen = AppScreen.PENGATURAN
                                selectedTab = 3
                            },
                            onShowToast = ::showToast
                        )
                    }
                }
            }

            // Bottom Floating Bar (Visible on 4 main tabs)
            val showBottomBar = currentScreen in listOf(
                AppScreen.DASHBOARD,
                AppScreen.KEUANGAN,
                AppScreen.AGENDA,
                AppScreen.PENGATURAN
            )

            if (showBottomBar) {
                FloatingBottomBar(
                    currentTab = selectedTab,
                    onTabSelected = { index ->
                        selectedTab = index
                        currentScreen = when (index) {
                            0 -> AppScreen.DASHBOARD
                            1 -> AppScreen.KEUANGAN
                            2 -> AppScreen.AGENDA
                            3 -> AppScreen.PENGATURAN
                            else -> AppScreen.DASHBOARD
                        }
                    },
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }

            // Modal Keluar Akun
            if (showLogoutDialog) {
                LogoutConfirmDialog(
                    onConfirm = {
                        showLogoutDialog = false
                        currentScreen = AppScreen.LOGIN
                        showToast("Anda telah keluar dari akun Masjid Connect")
                    },
                    onDismiss = {
                        showLogoutDialog = false
                    }
                )
            }
        }
    }
}
