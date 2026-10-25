package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.data.models.Transaction
import com.example.data.repository.MasjidRepository
import com.example.ui.components.AppHeader
import com.example.ui.components.CashFlowBarChart
import com.example.ui.theme.*

@Composable
fun KeuanganScreen(
    onNavigateBack: () -> Unit,
    onNavigateToTambahTransaksi: () -> Unit,
    onNavigateToLaporan: () -> Unit,
    onShowToast: (String) -> Unit
) {
    val transactions by MasjidRepository.transactions.collectAsState()
    val totalBalance = remember(transactions) { MasjidRepository.getTotalBalance() }
    val totalIncome = remember(transactions) { MasjidRepository.getTotalIncome() }
    val totalExpense = remember(transactions) { MasjidRepository.getTotalExpense() }

    var selectedFilter by remember { mutableStateOf("Semua") } // "Semua", "Pemasukan", "Pengeluaran"

    val filteredTransactions = remember(transactions, selectedFilter) {
        when (selectedFilter) {
            "Pemasukan" -> transactions.filter { it.type == "income" }
            "Pengeluaran" -> transactions.filter { it.type == "expense" }
            else -> transactions
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundWarm)
    ) {
        AppHeader(
            title = "Keuangan",
            subtitle = "KAS OPERASIONAL & INFAQ",
            onBackClick = onNavigateBack,
            onAvatarClick = { }
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 90.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Saldo Utama Card
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(SurfaceWhite)
                        .border(1.dp, SurfaceBorder, RoundedCornerShape(20.dp))
                        .padding(18.dp)
                ) {
                    Text(
                        text = "SALDO UTAMA",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextSecondary,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = MasjidRepository.formatRupiah(totalBalance),
                        fontSize = 26.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.TrendingUp,
                            contentDescription = null,
                            tint = GreenText,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "+8.4% bulan ini",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = GreenText
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // 2 Side by Side Income & Expense Box
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Pemasukan
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(14.dp))
                                .background(GreenLight)
                                .padding(12.dp)
                        ) {
                            Column {
                                Text(
                                    text = "Pemasukan",
                                    fontSize = 11.sp,
                                    color = GreenText,
                                    fontWeight = FontWeight.Medium
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = MasjidRepository.formatRupiah(totalIncome),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = GreenPrimary
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "• Bulan Ini",
                                    fontSize = 10.sp,
                                    color = GreenText
                                )
                            }
                        }

                        // Pengeluaran
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(14.dp))
                                .background(RedExpense)
                                .padding(12.dp)
                        ) {
                            Column {
                                Text(
                                    text = "Pengeluaran",
                                    fontSize = 11.sp,
                                    color = RedExpenseText,
                                    fontWeight = FontWeight.Medium
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = MasjidRepository.formatRupiah(totalExpense),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = RedExpenseText
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "• Bulan Ini",
                                    fontSize = 10.sp,
                                    color = RedExpenseText
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Action Buttons: + Transaksi & Laporan
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = onNavigateToTambahTransaksi,
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .testTag("btn_tambah_transaksi"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = GreenPrimary,
                                contentColor = Color.White
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Transaksi", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }

                        OutlinedButton(
                            onClick = onNavigateToLaporan,
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .testTag("btn_laporan_keuangan"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = GreenPrimary
                            ),
                            border = androidx.compose.foundation.BorderStroke(1.dp, GreenPrimary)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Assessment,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Laporan", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                }
            }

            // Cashflow Bar Chart
            item {
                CashFlowBarChart()
            }

            // Riwayat Transaksi Section
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Riwayat Transaksi",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )

                    // Filter chips row
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf("Semua", "Pemasukan", "Pengeluaran").forEach { filter ->
                            val isSel = selectedFilter == filter
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(if (isSel) GreenPrimary else SurfaceWhite)
                                    .border(1.dp, if (isSel) GreenPrimary else SurfaceBorder, RoundedCornerShape(14.dp))
                                    .clickable { selectedFilter = filter }
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = filter,
                                    fontSize = 10.sp,
                                    fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSel) Color.White else TextSecondary
                                )
                            }
                        }
                    }
                }
            }

            // Transactions list
            if (filteredTransactions.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Belum ada transaksi pada kategori ini",
                            fontSize = 13.sp,
                            color = TextSecondary
                        )
                    }
                }
            } else {
                items(filteredTransactions, key = { it.id }) { tx ->
                    val isIncome = tx.type == "income"
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(SurfaceWhite)
                            .border(1.dp, SurfaceBorder, RoundedCornerShape(16.dp))
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Category Icon
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isIncome) GreenLight else RedExpense),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isIncome) Icons.Default.ArrowDownward else Icons.Default.ArrowUpward,
                                contentDescription = null,
                                tint = if (isIncome) GreenText else RedExpenseText,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = tx.description,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = tx.transactionDate,
                                fontSize = 10.sp,
                                color = TextMuted
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isIncome) GreenLight else Color(0xFFFEF3C7))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = tx.categoryName,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (isIncome) GreenText else AmberDark
                                )
                            }
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "${if (isIncome) "+" else "-"}${MasjidRepository.formatRupiah(tx.amount)}",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isIncome) GreenText else RedExpenseText
                            )
                            if (tx.proofFile != null) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.clickable {
                                        onShowToast("Melihat lampiran: ${tx.proofFile}")
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AttachFile,
                                        contentDescription = "Lampiran",
                                        tint = TextSecondary,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Spacer(modifier = Modifier.width(2.dp))
                                    Text(
                                        text = "Bukti",
                                        fontSize = 10.sp,
                                        color = TextSecondary
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
