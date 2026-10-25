package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.repository.MasjidRepository
import com.example.ui.components.AppHeader
import com.example.ui.theme.*

@Composable
fun PengaturanScreen(
    onNavigateBack: () -> Unit,
    onNavigateToProfilMasjid: () -> Unit,
    onNavigateToNotifikasi: () -> Unit,
    onNavigateToJadwalSalat: () -> Unit,
    onNavigateToManajemenUser: () -> Unit,
    onNavigateToKeamanan: () -> Unit,
    onNavigateToDigitalSignage: () -> Unit,
    onNavigateToTentang: () -> Unit,
    onLogoutClick: () -> Unit
) {
    val currentUser = MasjidRepository.currentUser

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundWarm)
    ) {
        AppHeader(
            title = "Pengaturan",
            subtitle = "PENGELOLAAN SISTEM & AKUN",
            onBackClick = onNavigateBack
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 90.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // User Profile Card
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(GreenPrimary)
                        .padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .border(2.dp, AmberPrimary, CircleShape)
                            .background(Color(0xFF1E5242)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "ZA",
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 18.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = currentUser.name,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "${currentUser.role} • MASJID AL-IKHLAS",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = AmberPrimary,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = currentUser.email,
                            fontSize = 11.sp,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                    }
                }
            }

            // Menu Options Card
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(SurfaceWhite)
                        .border(1.dp, SurfaceBorder, RoundedCornerShape(20.dp))
                        .padding(vertical = 6.dp)
                ) {
                    MenuItemRow(
                        icon = Icons.Default.Mosque,
                        title = "Profil Masjid",
                        onClick = onNavigateToProfilMasjid
                    )
                    HorizontalDivider(color = SurfaceBorder, thickness = 0.5.dp, modifier = Modifier.padding(horizontal = 16.dp))

                    MenuItemRow(
                        icon = Icons.Default.Notifications,
                        title = "Pengaturan Notifikasi",
                        onClick = onNavigateToNotifikasi
                    )
                    HorizontalDivider(color = SurfaceBorder, thickness = 0.5.dp, modifier = Modifier.padding(horizontal = 16.dp))

                    MenuItemRow(
                        icon = Icons.Default.AccessTime,
                        title = "Konfigurasi Jadwal Salat",
                        onClick = onNavigateToJadwalSalat
                    )
                    HorizontalDivider(color = SurfaceBorder, thickness = 0.5.dp, modifier = Modifier.padding(horizontal = 16.dp))

                    MenuItemRow(
                        icon = Icons.Default.Group,
                        title = "Manajemen User",
                        onClick = onNavigateToManajemenUser
                    )
                    HorizontalDivider(color = SurfaceBorder, thickness = 0.5.dp, modifier = Modifier.padding(horizontal = 16.dp))

                    MenuItemRow(
                        icon = Icons.Default.Lock,
                        title = "Keamanan & Password",
                        onClick = onNavigateToKeamanan
                    )
                    HorizontalDivider(color = SurfaceBorder, thickness = 0.5.dp, modifier = Modifier.padding(horizontal = 16.dp))

                    MenuItemRow(
                        icon = Icons.Default.Tv,
                        title = "Digital Signage / TV",
                        onClick = onNavigateToDigitalSignage
                    )
                    HorizontalDivider(color = SurfaceBorder, thickness = 0.5.dp, modifier = Modifier.padding(horizontal = 16.dp))

                    MenuItemRow(
                        icon = Icons.Default.Info,
                        title = "Tentang Aplikasi",
                        onClick = onNavigateToTentang
                    )
                }
            }

            // Logout Button (Red Outlined / Soft Red Background)
            item {
                OutlinedButton(
                    onClick = onLogoutClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("btn_menu_keluar_akun"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = RedExpense,
                        contentColor = RedExpenseText
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFCA5A5))
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Logout,
                        contentDescription = null,
                        tint = RedExpenseText,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Keluar Akun",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = RedExpenseText
                    )
                }
            }

            // Footer
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Masjid Connect v2.4.0",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextMuted
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "© 2026 Karya Digital Muslim",
                        fontSize = 10.sp,
                        color = TextMuted
                    )
                }
            }
        }
    }
}

@Composable
private fun MenuItemRow(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(BackgroundWarm),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = GreenPrimary,
                modifier = Modifier.size(18.dp)
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Text(
            text = title,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextPrimary,
            modifier = Modifier.weight(1f)
        )

        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
            contentDescription = null,
            tint = TextMuted,
            modifier = Modifier.size(14.dp)
        )
    }
}
