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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.User
import com.example.data.repository.MasjidRepository
import com.example.ui.components.AppHeader
import com.example.ui.theme.*

@Composable
fun ManajemenUserScreen(
    onNavigateBack: () -> Unit,
    onShowToast: (String) -> Unit
) {
    val users by MasjidRepository.users.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var showAddDialog by remember { mutableStateOf(false) }

    val filteredUsers = remember(users, searchQuery) {
        if (searchQuery.isBlank()) users
        else users.filter {
            it.name.contains(searchQuery, ignoreCase = true) ||
            it.role.contains(searchQuery, ignoreCase = true) ||
            it.email.contains(searchQuery, ignoreCase = true)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundWarm)
    ) {
        AppHeader(
            title = "Manajemen User",
            subtitle = "HAK AKSES & PENGELOLA",
            onBackClick = onNavigateBack
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .padding(20.dp)
        ) {
            Column {
                Text(
                    text = "Daftar Pengelola",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Kelola hak akses dan peran personil operasional Masjid Anda.",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Cari nama atau peran...", fontSize = 13.sp, color = TextMuted) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Cari",
                        tint = TextSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = SurfaceWhite,
                    unfocusedContainerColor = SurfaceWhite,
                    focusedBorderColor = GreenPrimary,
                    unfocusedBorderColor = SurfaceBorder
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Users List
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredUsers, key = { it.id }) { user ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(SurfaceWhite)
                            .border(1.dp, SurfaceBorder, RoundedCornerShape(16.dp))
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // User Avatar
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(GreenLight),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = user.name.split(" ").mapNotNull { it.firstOrNull()?.toString() }.take(2).joinToString(""),
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = GreenPrimary
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = user.name,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(
                                        when (user.role) {
                                            "ADMINISTRATOR" -> GreenLight
                                            "BENDAHARA" -> AmberLight
                                            "SEKRETARIS" -> Color(0xFFE0E7FF)
                                            else -> Color(0xFFF3F4F6)
                                        }
                                    )
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = user.role,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = when (user.role) {
                                        "ADMINISTRATOR" -> GreenText
                                        "BENDAHARA" -> AmberDark
                                        "SEKRETARIS" -> Color(0xFF4338CA)
                                        else -> TextSecondary
                                    }
                                )
                            }
                        }

                        // Status Shield Icon
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = "Status",
                            tint = if (user.role == "ADMINISTRATOR") GreenPrimary else TextMuted,
                            modifier = Modifier.size(20.dp)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        // Delete button
                        IconButton(
                            onClick = {
                                MasjidRepository.deleteUser(user.id)
                                onShowToast("Pengelola '${user.name}' telah dihapus")
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.DeleteOutline,
                                contentDescription = "Hapus",
                                tint = TextMuted,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = { showAddDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = GreenPrimary,
                    contentColor = Color.White
                )
            ) {
                Icon(Icons.Default.PersonAdd, contentDescription = null, tint = AmberPrimary, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Tambah User Baru",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }

    if (showAddDialog) {
        var newName by remember { mutableStateOf("") }
        var newEmail by remember { mutableStateOf("") }
        var newRole by remember { mutableStateOf("SEKRETARIS") }

        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Tambah Pengelola Masjid", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = newName,
                        onValueChange = { newName = it },
                        label = { Text("Nama Lengkap") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newEmail,
                        onValueChange = { newEmail = it },
                        label = { Text("Email") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Column {
                        Text("Pilih Peran:", fontSize = 12.sp, fontWeight = FontWeight.Medium)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf("SEKRETARIS", "BENDAHARA", "MARBOT").forEach { r ->
                                FilterChip(
                                    selected = newRole == r,
                                    onClick = { newRole = r },
                                    label = { Text(r, fontSize = 10.sp) }
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newName.isNotBlank()) {
                            MasjidRepository.addUser(
                                User(
                                    id = "usr-${System.currentTimeMillis()}",
                                    name = newName,
                                    email = newEmail.ifBlank { "pengurus@alikhlas.id" },
                                    role = newRole
                                )
                            )
                            showAddDialog = false
                            onShowToast("User '${newName}' berhasil ditambahkan")
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary)
                ) {
                    Text("Simpan")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }
}
