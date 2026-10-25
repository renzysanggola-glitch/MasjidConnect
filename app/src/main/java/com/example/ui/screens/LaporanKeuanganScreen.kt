package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.data.repository.MasjidRepository
import com.example.ui.components.AppHeader
import com.example.ui.components.ExpenseDonutChart
import com.example.ui.theme.*

@Composable
fun LaporanKeuanganScreen(
    onNavigateBack: () -> Unit,
    onShowToast: (String) -> Unit
) {
    var selectedPeriod by remember { mutableStateOf("Bulan Ini") } // "Bulan Ini", "3 Bulan", "Tahunan"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundWarm)
    ) {
        AppHeader(
            title = "Laporan Keuangan",
            subtitle = "TRANSPARANSI KAS & ANGGARAN",
            onBackClick = onNavigateBack
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 40.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Segmented Period Selector
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(SurfaceWhite)
                        .border(1.dp, SurfaceBorder, RoundedCornerShape(14.dp))
                        .padding(4.dp)
                ) {
                    listOf("Bulan Ini", "3 Bulan", "Tahunan").forEach { period ->
                        val isSelected = selectedPeriod == period
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) GreenPrimary else Color.Transparent)
                                .clickable { selectedPeriod = period },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = period,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else TextSecondary
                            )
                        }
                    }
                }
            }

            // Summary Cards (Pemasukan & Pengeluaran)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Pemasukan
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(18.dp))
                            .background(SurfaceWhite)
                            .border(1.dp, SurfaceBorder, RoundedCornerShape(18.dp))
                            .padding(14.dp)
                    ) {
                        Column {
                            Text(
                                text = "PEMASUKAN",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextSecondary,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Rp 12.450k",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = GreenText
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.TrendingUp,
                                    contentDescription = null,
                                    tint = GreenText,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "+12%",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = GreenText
                                )
                            }
                        }
                    }

                    // Pengeluaran
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(18.dp))
                            .background(SurfaceWhite)
                            .border(1.dp, SurfaceBorder, RoundedCornerShape(18.dp))
                            .padding(14.dp)
                    ) {
                        Column {
                            Text(
                                text = "PENGELUARAN",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextSecondary,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Rp 8.120k",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = RedExpenseText
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.TrendingDown,
                                    contentDescription = null,
                                    tint = RedExpenseText,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "-5%",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = RedExpenseText
                                )
                            }
                        }
                    }
                }
            }

            // Donut Chart Alokasi Pengeluaran
            item {
                ExpenseDonutChart()
            }

            // Ringkasan Kategori
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Ringkasan Kategori",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )

                    Text(
                        text = "LIHAT DETAIL",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = GreenPrimary,
                        modifier = Modifier.clickable { onShowToast("Menampilkan detail rincian pos anggaran") }
                    )
                }
            }

            // Category breakdown items
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    // Item 1: Infaq & Sedekah
                    CategorySummaryCard(
                        title = "Infaq & Sedekah",
                        sub = "Total 42 Transaksi",
                        amount = "+Rp 9.250k",
                        badge = "Pemasukan",
                        isIncome = true
                    )

                    // Item 2: Listrik & Air
                    CategorySummaryCard(
                        title = "Listrik & Air",
                        sub = "Total 2 Transaksi",
                        amount = "-Rp 2.150k",
                        badge = "Operasional",
                        isIncome = false
                    )

                    // Item 3: Renovasi Atap
                    CategorySummaryCard(
                        title = "Renovasi Atap",
                        sub = "Total 1 Transaksi",
                        amount = "-Rp 5.000k",
                        badge = "Pembangunan",
                        isIncome = false
                    )
                }
            }

            // Unduh Laporan PDF Button
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        onShowToast("Laporan Keuangan (${selectedPeriod}) berhasil diunduh ke folder Dokumen (PDF)")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("btn_unduh_laporan_pdf"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GreenPrimary,
                        contentColor = Color.White
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Download,
                        contentDescription = null,
                        tint = AmberPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Unduh Laporan (PDF)",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun CategorySummaryCard(
    title: String,
    sub: String,
    amount: String,
    badge: String,
    isIncome: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(SurfaceWhite)
            .border(1.dp, SurfaceBorder, RoundedCornerShape(16.dp))
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(if (isIncome) GreenLight else RedExpense),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (isIncome) Icons.Default.Savings else Icons.Default.ReceiptLong,
                contentDescription = null,
                tint = if (isIncome) GreenText else RedExpenseText,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = sub,
                fontSize = 10.sp,
                color = TextSecondary
            )
        }

        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = amount,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = if (isIncome) GreenText else RedExpenseText
            )
            Spacer(modifier = Modifier.height(2.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(if (isIncome) GreenLight else Color(0xFFFEF3C7))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = badge,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isIncome) GreenText else AmberDark
                )
            }
        }
    }
}
