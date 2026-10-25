package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.PrayerConfig
import com.example.data.repository.MasjidRepository
import com.example.ui.components.AppHeader
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KonfigurasiJadwalSalatScreen(
    onNavigateBack: () -> Unit,
    onShowToast: (String) -> Unit
) {
    val initialConfig by MasjidRepository.prayerConfig.collectAsState()

    var autoGps by remember { mutableStateOf(initialConfig.autoGps) }
    var calculationMethod by remember { mutableStateOf(initialConfig.calculationMethod) }
    var methodDropdownExpanded by remember { mutableStateOf(false) }

    var subuhOffset by remember { mutableIntStateOf(initialConfig.subuhOffset) }
    var dzuhurOffset by remember { mutableIntStateOf(initialConfig.dzuhurOffset) }
    var asharOffset by remember { mutableIntStateOf(initialConfig.asharOffset) }
    var maghribOffset by remember { mutableIntStateOf(initialConfig.maghribOffset) }
    var isyaOffset by remember { mutableIntStateOf(initialConfig.isyaOffset) }

    val methods = listOf(
        "Kemenag RI (SIHAT)",
        "Muslim World League (MWL)",
        "Islamic Society of North America (ISNA)",
        "Umm Al-Qura University, Makkah",
        "Egyptian General Authority of Survey"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundWarm)
    ) {
        AppHeader(
            title = "Jadwal Sholat",
            subtitle = "KONFIGURASI & KOREKSI WAKTU",
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
                    text = "Konfigurasi Waktu",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Sesuaikan parameter perhitungan waktu sholat sesuai dengan ketetapan wilayah Anda.",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }

            // GPS Auto Toggle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(SurfaceWhite)
                    .border(1.dp, SurfaceBorder, RoundedCornerShape(18.dp))
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(GreenLight),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.GpsFixed,
                            contentDescription = null,
                            tint = GreenText,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Otomatis Lokasi",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Gunakan GPS untuk jadwal akurat",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                }

                Switch(
                    checked = autoGps,
                    onCheckedChange = { autoGps = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = GreenPrimary
                    )
                )
            }

            // Metode Perhitungan
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "METODE PERHITUNGAN",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(6.dp))
                ExposedDropdownMenuBox(
                    expanded = methodDropdownExpanded,
                    onExpandedChange = { methodDropdownExpanded = !methodDropdownExpanded }
                ) {
                    OutlinedTextField(
                        value = calculationMethod,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = methodDropdownExpanded) },
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
                        expanded = methodDropdownExpanded,
                        onDismissRequest = { methodDropdownExpanded = false }
                    ) {
                        methods.forEach { method ->
                            DropdownMenuItem(
                                text = { Text(method, fontSize = 13.sp) },
                                onClick = {
                                    calculationMethod = method
                                    methodDropdownExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            // Koreksi Waktu (Menit) Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(SurfaceWhite)
                    .border(1.dp, SurfaceBorder, RoundedCornerShape(18.dp))
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Tune,
                        contentDescription = null,
                        tint = GreenPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Koreksi Waktu (Menit)",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                PrayerStepperRow(name = "Subuh", value = subuhOffset, onValueChange = { subuhOffset = it })
                HorizontalDivider(color = SurfaceBorder, thickness = 0.5.dp, modifier = Modifier.padding(vertical = 8.dp))

                PrayerStepperRow(name = "Dzuhur", value = dzuhurOffset, onValueChange = { dzuhurOffset = it })
                HorizontalDivider(color = SurfaceBorder, thickness = 0.5.dp, modifier = Modifier.padding(vertical = 8.dp))

                PrayerStepperRow(name = "Ashar", value = asharOffset, onValueChange = { asharOffset = it })
                HorizontalDivider(color = SurfaceBorder, thickness = 0.5.dp, modifier = Modifier.padding(vertical = 8.dp))

                PrayerStepperRow(name = "Maghrib", value = maghribOffset, onValueChange = { maghribOffset = it })
                HorizontalDivider(color = SurfaceBorder, thickness = 0.5.dp, modifier = Modifier.padding(vertical = 8.dp))

                PrayerStepperRow(name = "Isya", value = isyaOffset, onValueChange = { isyaOffset = it })
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = {
                    MasjidRepository.updatePrayerConfig(
                        PrayerConfig(
                            autoGps = autoGps,
                            calculationMethod = calculationMethod,
                            subuhOffset = subuhOffset,
                            dzuhurOffset = dzuhurOffset,
                            asharOffset = asharOffset,
                            maghribOffset = maghribOffset,
                            isyaOffset = isyaOffset
                        )
                    )
                    onShowToast("Konfigurasi waktu salat berhasil disimpan")
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
                    text = "Simpan Konfigurasi",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun PrayerStepperRow(
    name: String,
    value: Int,
    onValueChange: (Int) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = name,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextPrimary
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            // Decrement Button
            IconButton(
                onClick = { onValueChange(value - 1) },
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(BackgroundWarm)
            ) {
                Icon(
                    imageVector = Icons.Default.Remove,
                    contentDescription = "Kurang",
                    tint = TextPrimary,
                    modifier = Modifier.size(16.dp)
                )
            }

            Box(
                modifier = Modifier
                    .width(44.dp)
                    .padding(horizontal = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (value > 0) "+$value" else "$value",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (value != 0) GreenPrimary else TextSecondary
                )
            }

            // Increment Button
            IconButton(
                onClick = { onValueChange(value + 1) },
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(GreenPrimary)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Tambah",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}
