package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.NotificationConfig
import com.example.data.repository.MasjidRepository
import com.example.ui.components.AppHeader
import com.example.ui.theme.*

@Composable
fun PengaturanNotifikasiScreen(
    onNavigateBack: () -> Unit,
    onShowToast: (String) -> Unit
) {
    val initialConfig by MasjidRepository.notificationConfig.collectAsState()

    var adzanNotification by remember { mutableStateOf(initialConfig.adzanNotification) }
    var iqomahReminder by remember { mutableStateOf(initialConfig.iqomahReminder) }
    var newAnnouncements by remember { mutableStateOf(initialConfig.newAnnouncements) }
    var financialReport by remember { mutableStateOf(initialConfig.financialReport) }
    var studyAgendas by remember { mutableStateOf(initialConfig.studyAgendas) }
    var batterySaver by remember { mutableStateOf(initialConfig.batterySaver) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundWarm)
    ) {
        AppHeader(
            title = "Pengaturan Notifikasi",
            subtitle = "PREFERENSI PEMBERITAHUAN",
            onBackClick = onNavigateBack
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column {
                Text(
                    text = "Kelola Notifikasi",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Pilih informasi apa saja yang ingin Anda terima langsung di perangkat Anda.",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(SurfaceWhite)
                    .border(1.dp, SurfaceBorder, RoundedCornerShape(20.dp))
                    .padding(vertical = 4.dp)
            ) {
                NotificationToggleRow(
                    title = "Notifikasi Adzan",
                    desc = "Bunyikan pengingat waktu sholat tiba",
                    checked = adzanNotification,
                    onCheckedChange = { adzanNotification = it }
                )
                HorizontalDivider(color = SurfaceBorder, thickness = 0.5.dp, modifier = Modifier.padding(horizontal = 16.dp))

                NotificationToggleRow(
                    title = "Pengingat Iqomah",
                    desc = "Jeda waktu setelah adzan selesai",
                    checked = iqomahReminder,
                    onCheckedChange = { iqomahReminder = it }
                )
                HorizontalDivider(color = SurfaceBorder, thickness = 0.5.dp, modifier = Modifier.padding(horizontal = 16.dp))

                NotificationToggleRow(
                    title = "Pengumuman Baru",
                    desc = "Berita terkini dari pengurus masjid",
                    checked = newAnnouncements,
                    onCheckedChange = { newAnnouncements = it }
                )
                HorizontalDivider(color = SurfaceBorder, thickness = 0.5.dp, modifier = Modifier.padding(horizontal = 16.dp))

                NotificationToggleRow(
                    title = "Laporan Keuangan",
                    desc = "Transparansi kas masjid mingguan",
                    checked = financialReport,
                    onCheckedChange = { financialReport = it }
                )
                HorizontalDivider(color = SurfaceBorder, thickness = 0.5.dp, modifier = Modifier.padding(horizontal = 16.dp))

                NotificationToggleRow(
                    title = "Agenda Kajian",
                    desc = "Jadwal taklim & kegiatan rutin",
                    checked = studyAgendas,
                    onCheckedChange = { studyAgendas = it }
                )
                HorizontalDivider(color = SurfaceBorder, thickness = 0.5.dp, modifier = Modifier.padding(horizontal = 16.dp))

                NotificationToggleRow(
                    title = "Hemat Baterai",
                    desc = "Optimasi push notifikasi sistem",
                    checked = batterySaver,
                    onCheckedChange = { batterySaver = it }
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = {
                    MasjidRepository.updateNotificationConfig(
                        NotificationConfig(
                            adzanNotification = adzanNotification,
                            iqomahReminder = iqomahReminder,
                            newAnnouncements = newAnnouncements,
                            financialReport = financialReport,
                            studyAgendas = studyAgendas,
                            batterySaver = batterySaver
                        )
                    )
                    onShowToast("Pengaturan notifikasi berhasil disimpan")
                    onNavigateBack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = GreenPrimary,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Simpan Pengaturan",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun NotificationToggleRow(
    title: String,
    desc: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = desc,
                fontSize = 11.sp,
                color = TextSecondary
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = GreenPrimary,
                uncheckedThumbColor = TextMuted,
                uncheckedTrackColor = BackgroundWarm
            )
        )
    }
}
