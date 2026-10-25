package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.Activity
import com.example.data.models.PrayerTime
import com.example.data.repository.MasjidRepository
import com.example.ui.components.AppHeader
import com.example.ui.components.CashFlowBarChart
import com.example.ui.theme.*

@Composable
fun DashboardScreen(
    onNavigateToKeuangan: () -> Unit,
    onNavigateToAgenda: () -> Unit,
    onNavigateToSetting: () -> Unit,
    onShowToast: (String) -> Unit
) {
    val mosque by MasjidRepository.mosque.collectAsState()
    val transactions by MasjidRepository.transactions.collectAsState()
    val activities by MasjidRepository.activities.collectAsState()
    val prayerTimes = remember(MasjidRepository.prayerConfig.collectAsState().value) {
        MasjidRepository.getPrayerTimes()
    }

    val totalBalance = remember(transactions) { MasjidRepository.getTotalBalance() }
    val totalIncome = remember(transactions) { MasjidRepository.getTotalIncome() }
    val totalExpense = remember(transactions) { MasjidRepository.getTotalExpense() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundWarm)
    ) {
        // App Header with Hijri Date Subheader
        AppHeader(
            title = "Masjid Connect",
            subtitle = "• ${mosque.name.uppercase()} •",
            onAvatarClick = onNavigateToSetting,
            extraContent = {
                // Kalender Hijriyah Bar
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFF07271D))
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    Text(
                        text = "KALENDER HIJRIYAH",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = AmberPrimary,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Jumat, 18 September 2026 / 7 Rabi'ul Akhir 1448 H",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                }
            }
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 90.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Horizontal Stat Cards (Total Kas, Pemasukan, Pengeluaran)
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Total Kas Card
                    Box(
                        modifier = Modifier
                            .width(180.dp)
                            .clip(RoundedCornerShape(18.dp))
                            .background(SurfaceWhite)
                            .border(1.dp, SurfaceBorder, RoundedCornerShape(18.dp))
                            .clickable { onNavigateToKeuangan() }
                            .padding(14.dp)
                    ) {
                        Column {
                            Text(
                                text = "Total Kas Masjid",
                                fontSize = 11.sp,
                                color = TextSecondary,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = MasjidRepository.formatRupiah(totalBalance),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.TrendingUp,
                                    contentDescription = null,
                                    tint = GreenText,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "+8.4% bulan ini",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = GreenText
                                )
                            }
                        }
                    }

                    // Pemasukan Card
                    Box(
                        modifier = Modifier
                            .width(160.dp)
                            .clip(RoundedCornerShape(18.dp))
                            .background(GreenLight)
                            .border(1.dp, Color(0xFFC3E8D5), RoundedCornerShape(18.dp))
                            .clickable { onNavigateToKeuangan() }
                            .padding(14.dp)
                    ) {
                        Column {
                            Text(
                                text = "Pemasukan",
                                fontSize = 11.sp,
                                color = GreenText,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = MasjidRepository.formatRupiah(totalIncome),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = GreenPrimary
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "• Penerimaan Kas",
                                fontSize = 11.sp,
                                color = GreenText
                            )
                        }
                    }

                    // Pengeluaran Card
                    Box(
                        modifier = Modifier
                            .width(160.dp)
                            .clip(RoundedCornerShape(18.dp))
                            .background(RedExpense)
                            .border(1.dp, Color(0xFFF9C8C8), RoundedCornerShape(18.dp))
                            .clickable { onNavigateToKeuangan() }
                            .padding(14.dp)
                    ) {
                        Column {
                            Text(
                                text = "Pengeluaran",
                                fontSize = 11.sp,
                                color = RedExpenseText,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = MasjidRepository.formatRupiah(totalExpense),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = RedExpenseText
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "• Bulan Ini",
                                fontSize = 11.sp,
                                color = RedExpenseText
                            )
                        }
                    }
                }
            }

            // Card Jadwal Salat
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(SurfaceWhite)
                        .border(1.dp, SurfaceBorder, RoundedCornerShape(18.dp))
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Jadwal Salat",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = "Kota Tangerang Selatan, Banten",
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(GreenLight)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "WIB",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = GreenPrimary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Prayer Items Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        prayerTimes.forEach { prayer ->
                            val isActive = prayer.isActive
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(if (isActive) AmberPrimary else BackgroundWarm)
                                    .border(
                                        1.dp,
                                        if (isActive) AmberDark else SurfaceBorder,
                                        RoundedCornerShape(14.dp)
                                    )
                                    .padding(vertical = 10.dp, horizontal = 4.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = prayer.name,
                                        fontSize = 11.sp,
                                        fontWeight = if (isActive) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isActive) GreenPrimary else TextSecondary
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = prayer.time,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isActive) GreenPrimary else TextPrimary
                                    )
                                    if (isActive) {
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "AKTIF",
                                            fontSize = 8.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = GreenPrimary
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Card Analisis Arus Kas (Bar Chart)
            item {
                CashFlowBarChart()
            }

            // Card Agenda & Kajian
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(SurfaceWhite)
                        .border(1.dp, SurfaceBorder, RoundedCornerShape(18.dp))
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Agenda & Kajian",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )

                        Text(
                            text = "LIHAT SEMUA",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = GreenPrimary,
                            modifier = Modifier
                                .clickable { onNavigateToAgenda() }
                                .padding(vertical = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    activities.take(2).forEach { act ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Date Block
                            Box(
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(GreenLight),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = act.dateBadgeMonth,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = GreenText
                                    )
                                    Text(
                                        text = act.dateBadgeDay,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = GreenPrimary
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = act.title,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = act.speaker,
                                    fontSize = 11.sp,
                                    color = TextSecondary
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "${act.time} • ${act.location}",
                                    fontSize = 10.sp,
                                    color = TextMuted
                                )
                            }
                        }
                        if (act != activities.take(2).last()) {
                            HorizontalDivider(
                                modifier = Modifier.padding(vertical = 6.dp),
                                color = SurfaceBorder,
                                thickness = 0.5.dp
                            )
                        }
                    }
                }
            }
        }
    }
}
