package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

/**
 * Top App Header in deep green #0B3B2C with rounded bottom edge
 */
@Composable
fun AppHeader(
    title: String,
    subtitle: String? = null,
    onBackClick: (() -> Unit)? = null,
    onAvatarClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    extraContent: (@Composable () -> Unit)? = null
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp))
            .background(GreenPrimary)
            .statusBarsPadding()
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f, fill = false)
                ) {
                    if (onBackClick != null) {
                        IconButton(
                            onClick = onBackClick,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.12f))
                                .testTag("header_back_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Kembali",
                                tint = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                    } else {
                        // Mosque icon badge
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(AmberPrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Mosque,
                                contentDescription = "Logo Masjid",
                                tint = GreenPrimary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                    }

                    Column {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                color = Color.White
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        if (subtitle != null) {
                            Text(
                                text = subtitle,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 11.sp,
                                    color = AmberPrimary,
                                    letterSpacing = 1.sp
                                )
                            )
                        }
                    }
                }

                // Avatar / Profile in right corner
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .border(1.5.dp, AmberPrimary, CircleShape)
                        .background(Color(0xFF1E5242))
                        .clickable { onAvatarClick?.invoke() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "ZA",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }
            }

            if (extraContent != null) {
                Spacer(modifier = Modifier.height(14.dp))
                extraContent()
            }
        }
    }
}

/**
 * Floating Rounded Bottom Bar with 4 tabs: Dashboard, Keuangan, Agenda, Setting
 */
@Composable
fun FloatingBottomBar(
    currentTab: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        NavigationItem("Dashboard", Icons.Default.Dashboard, Icons.Outlined.Dashboard),
        NavigationItem("Keuangan", Icons.Default.AccountBalanceWallet, Icons.Outlined.AccountBalanceWallet),
        NavigationItem("Agenda", Icons.Default.Event, Icons.Outlined.Event),
        NavigationItem("Setting", Icons.Default.Settings, Icons.Outlined.Settings)
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(68.dp)
                .shadow(16.dp, RoundedCornerShape(34.dp), spotColor = Color(0x40000000))
                .clip(RoundedCornerShape(34.dp))
                .background(GreenPrimary)
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            items.forEachIndexed { index, item ->
                val isSelected = currentTab == index
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { onTabSelected(index) }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                        .testTag("tab_${item.label.lowercase()}")
                ) {
                    if (isSelected) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(AmberPrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = item.selectedIcon,
                                contentDescription = item.label,
                                tint = GreenPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    } else {
                        Box(
                            modifier = Modifier.size(36.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = item.unselectedIcon,
                                contentDescription = item.label,
                                tint = Color.White.copy(alpha = 0.6f),
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = item.label,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 10.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) AmberPrimary else Color.White.copy(alpha = 0.65f)
                        )
                    )
                }
            }
        }
    }
}

private data class NavigationItem(
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

/**
 * Custom Bar Chart for Arus Kas (Pemasukan vs Pengeluaran)
 */
@Composable
fun CashFlowBarChart(
    modifier: Modifier = Modifier
) {
    val months = listOf("Apr", "Mei", "Jun", "Jul", "Agu", "Sep")
    // Values in Millions
    val incomeValues = listOf(4.2f, 4.8f, 5.5f, 5.1f, 6.0f, 5.23f)
    val expenseValues = listOf(1.5f, 2.1f, 1.8f, 2.4f, 1.9f, 1.12f)
    val maxValue = 7.0f

    Column(
        modifier = modifier
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
                    text = "Analisis Arus Kas",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = TextPrimary
                )
                Text(
                    text = "6 Bulan Terakhir",
                    fontSize = 11.sp,
                    color = TextSecondary
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(BackgroundWarm)
                    .border(1.dp, SurfaceBorder, RoundedCornerShape(20.dp))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "Bulanan",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextSecondary
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Custom Drawn Canvas Bar Chart
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val canvasWidth = size.width
                val canvasHeight = size.height
                val bottomAxisY = canvasHeight - 24.dp.toPx()
                val chartAreaHeight = bottomAxisY - 10.dp.toPx()

                // Draw horizontal dotted grid lines
                val gridLevels = 3
                for (i in 0..gridLevels) {
                    val y = 10.dp.toPx() + (chartAreaHeight * i / gridLevels)
                    drawLine(
                        color = Color(0xFFF1EFEA),
                        start = Offset(0f, y),
                        end = Offset(canvasWidth, y),
                        strokeWidth = 1.dp.toPx()
                    )
                }

                val numGroups = months.size
                val groupWidth = canvasWidth / numGroups
                val barWidth = 10.dp.toPx()
                val barGap = 4.dp.toPx()

                for (i in 0 until numGroups) {
                    val groupCenterX = (i * groupWidth) + (groupWidth / 2)
                    
                    // Income Bar
                    val incHeight = (incomeValues[i] / maxValue) * chartAreaHeight
                    val incLeft = groupCenterX - barWidth - (barGap / 2)
                    drawRoundRect(
                        color = GreenPrimary,
                        topLeft = Offset(incLeft, bottomAxisY - incHeight),
                        size = Size(barWidth, incHeight),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(4.dp.toPx(), 4.dp.toPx())
                    )

                    // Expense Bar
                    val expHeight = (expenseValues[i] / maxValue) * chartAreaHeight
                    val expLeft = groupCenterX + (barGap / 2)
                    drawRoundRect(
                        color = AmberPrimary,
                        topLeft = Offset(expLeft, bottomAxisY - expHeight),
                        size = Size(barWidth, expHeight),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(4.dp.toPx(), 4.dp.toPx())
                    )
                }
            }

            // Month Labels
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                months.forEach { month ->
                    Text(
                        text = month,
                        fontSize = 10.sp,
                        color = TextSecondary,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Legend
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(GreenPrimary)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = "Pemasukan", fontSize = 11.sp, color = TextSecondary)

            Spacer(modifier = Modifier.width(20.dp))

            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(AmberPrimary)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = "Pengeluaran", fontSize = 11.sp, color = TextSecondary)
        }
    }
}

/**
 * Donut Chart for Laporan Alokasi Pengeluaran
 */
@Composable
fun ExpenseDonutChart(
    modifier: Modifier = Modifier
) {
    val items = listOf(
        DonutItem("Operasional", 45f, GreenPrimary),
        DonutItem("Pembangunan", 30f, AmberPrimary),
        DonutItem("Sosial", 15f, Color(0xFF10B981)),
        DonutItem("Lainnya", 10f, Color(0xFF6B7280))
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(SurfaceWhite)
            .border(1.dp, SurfaceBorder, RoundedCornerShape(18.dp))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Alokasi Pengeluaran",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = TextPrimary
                )
                Text(
                    text = "Data berdasarkan kategori",
                    fontSize = 11.sp,
                    color = TextSecondary
                )
            }
            Icon(
                imageVector = Icons.Default.PieChart,
                contentDescription = null,
                tint = GreenPrimary,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Donut Canvas
        Box(
            modifier = Modifier.size(150.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val stroke = 26.dp.toPx()
                val radius = (size.minDimension - stroke) / 2
                var startAngle = -90f

                items.forEach { item ->
                    val sweepAngle = (item.percentage / 100f) * 360f
                    drawArc(
                        color = item.color,
                        startAngle = startAngle,
                        sweepAngle = sweepAngle,
                        useCenter = false,
                        topLeft = Offset(stroke / 2, stroke / 2),
                        size = Size(size.width - stroke, size.height - stroke),
                        style = Stroke(width = stroke, cap = StrokeCap.Butt)
                    )
                    startAngle += sweepAngle
                }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "100%",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = TextPrimary
                )
                Text(
                    text = "Terdistribusi",
                    fontSize = 10.sp,
                    color = TextSecondary
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Legend Grid
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            items.forEach { item ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(item.color)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = item.label,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextSecondary
                    )
                }
            }
        }
    }
}

private data class DonutItem(
    val label: String,
    val percentage: Float,
    val color: Color
)
