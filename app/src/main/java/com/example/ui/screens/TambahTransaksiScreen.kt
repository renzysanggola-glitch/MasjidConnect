package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.Transaction
import com.example.data.repository.MasjidRepository
import com.example.ui.components.AppHeader
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TambahTransaksiScreen(
    onNavigateBack: () -> Unit,
    onShowToast: (String) -> Unit
) {
    var transactionType by remember { mutableStateOf("income") } // "income" or "expense"
    var rawAmount by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Infaq & Sedekah") }
    var categoryDropdownExpanded by remember { mutableStateOf(false) }
    var transactionDate by remember { mutableStateOf("18 Sep 2026, 14:00") }
    var description by remember { mutableStateOf("") }
    var attachedFileName by remember { mutableStateOf<String?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val amountValue = rawAmount.toLongOrNull() ?: 0L

    val incomeCategories = listOf("Infaq & Sedekah", "Donasi Pembangunan", "Zakat Mal & Fitrah", "Kotak Amal Jumat", "Lainnya")
    val expenseCategories = listOf("Listrik & Air (PLN)", "Operasional & Kebersihan", "Honor Guru & Marbot", "Renovasi Atap & Fasilitas", "Sosial & Santunan", "Lainnya")
    val activeCategories = if (transactionType == "income") incomeCategories else expenseCategories

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundWarm)
    ) {
        AppHeader(
            title = "Tambah Transaksi",
            subtitle = "PENCATATAN KEUANGAN BARU",
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
            // Segmented Tab for Income / Expense
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(SurfaceWhite)
                    .border(1.dp, SurfaceBorder, RoundedCornerShape(14.dp))
                    .padding(4.dp)
            ) {
                // Pemasukan Tab
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (transactionType == "income") GreenPrimary else Color.Transparent)
                        .clickable {
                            transactionType = "income"
                            selectedCategory = incomeCategories.first()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.ArrowDownward,
                            contentDescription = null,
                            tint = if (transactionType == "income") Color.White else TextSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Pemasukan",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = if (transactionType == "income") Color.White else TextSecondary
                        )
                    }
                }

                // Pengeluaran Tab
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (transactionType == "expense") RedExpenseText else Color.Transparent)
                        .clickable {
                            transactionType = "expense"
                            selectedCategory = expenseCategories.first()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.ArrowUpward,
                            contentDescription = null,
                            tint = if (transactionType == "expense") Color.White else TextSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Pengeluaran",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = if (transactionType == "expense") Color.White else TextSecondary
                        )
                    }
                }
            }

            // Nominal Input Card
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(SurfaceWhite)
                    .border(1.dp, SurfaceBorder, RoundedCornerShape(18.dp))
                    .padding(18.dp)
            ) {
                Text(
                    text = "NOMINAL TRANSAKSI",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextSecondary,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Rp ",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (transactionType == "income") GreenPrimary else RedExpenseText
                    )
                    OutlinedTextField(
                        value = rawAmount,
                        onValueChange = { input ->
                            rawAmount = input.filter { it.isDigit() }
                            errorMessage = null
                        },
                        placeholder = { Text("0", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = TextMuted) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        textStyle = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 28.sp,
                            color = if (transactionType == "income") GreenPrimary else RedExpenseText
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_nominal_transaksi"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent
                        )
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Quick Increment Chips
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(50000L to "+50rb", 100000L to "+100rb", 500000L to "+500rb", 1000000L to "+1jt").forEach { (add, label) ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(BackgroundWarm)
                                .border(1.dp, SurfaceBorder, RoundedCornerShape(10.dp))
                                .clickable {
                                    val current = rawAmount.toLongOrNull() ?: 0L
                                    rawAmount = (current + add).toString()
                                }
                                .padding(vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = label,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextPrimary
                            )
                        }
                    }
                }
            }

            // Kategori Dropdown
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "KATEGORI TRANSAKSI",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(6.dp))
                ExposedDropdownMenuBox(
                    expanded = categoryDropdownExpanded,
                    onExpandedChange = { categoryDropdownExpanded = !categoryDropdownExpanded }
                ) {
                    OutlinedTextField(
                        value = selectedCategory,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryDropdownExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = SurfaceWhite,
                            unfocusedContainerColor = SurfaceWhite,
                            focusedBorderColor = GreenPrimary,
                            unfocusedBorderColor = SurfaceBorder
                        )
                    )
                    ExposedDropdownMenu(
                        expanded = categoryDropdownExpanded,
                        onDismissRequest = { categoryDropdownExpanded = false }
                    ) {
                        activeCategories.forEach { cat ->
                            DropdownMenuItem(
                                text = { Text(cat, fontSize = 13.sp) },
                                onClick = {
                                    selectedCategory = cat
                                    categoryDropdownExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            // Tanggal & Waktu
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "TANGGAL & WAKTU",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = transactionDate,
                    onValueChange = { transactionDate = it },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = null,
                            tint = TextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = SurfaceWhite,
                        unfocusedContainerColor = SurfaceWhite,
                        focusedBorderColor = GreenPrimary,
                        unfocusedBorderColor = SurfaceBorder
                    )
                )
            }

            // Keterangan / Memo
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "KETERANGAN / MEMO",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = description,
                    onValueChange = {
                        description = it
                        errorMessage = null
                    },
                    placeholder = { Text("Tulis catatan transaksi di sini...", fontSize = 13.sp, color = TextMuted) },
                    minLines = 3,
                    maxLines = 4,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = SurfaceWhite,
                        unfocusedContainerColor = SurfaceWhite,
                        focusedBorderColor = GreenPrimary,
                        unfocusedBorderColor = SurfaceBorder
                    )
                )
            }

            // Lampiran Bukti
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(SurfaceWhite)
                    .border(1.dp, SurfaceBorder, RoundedCornerShape(16.dp))
                    .padding(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(GreenLight),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AttachFile,
                                contentDescription = null,
                                tint = GreenText,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = attachedFileName ?: "Lampirkan Bukti",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextPrimary
                            )
                            Text(
                                text = "JPG, PNG atau PDF (Maks 2MB)",
                                fontSize = 10.sp,
                                color = TextSecondary
                            )
                        }
                    }

                    OutlinedButton(
                        onClick = {
                            attachedFileName = "bukti_transaksi_${System.currentTimeMillis() % 1000}.jpg"
                            onShowToast("Bukti transfer berhasil dipilih")
                        },
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text("PILIH", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = GreenPrimary)
                    }
                }
            }

            if (errorMessage != null) {
                Text(
                    text = errorMessage ?: "",
                    color = RedExpenseText,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Simpan Transaksi Button
            Button(
                onClick = {
                    if (amountValue <= 0) {
                        errorMessage = "Nominal transaksi harus lebih dari Rp 0"
                        return@Button
                    }
                    val finalDesc = if (description.isNotBlank()) description else selectedCategory
                    val newTx = Transaction(
                        id = "tx-${System.currentTimeMillis()}",
                        categoryId = if (transactionType == "income") "cat-1" else "cat-2",
                        categoryName = if (transactionType == "income") "Pemasukan" else "Operasional",
                        type = transactionType,
                        amount = amountValue,
                        description = finalDesc,
                        transactionDate = transactionDate,
                        proofFile = attachedFileName
                    )
                    MasjidRepository.addTransaction(newTx)
                    onShowToast("Transaksi ${MasjidRepository.formatRupiah(amountValue)} berhasil disimpan!")
                    onNavigateBack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("btn_simpan_transaksi"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = GreenPrimary,
                    contentColor = Color.White
                )
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = AmberPrimary, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Simpan Transaksi",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
