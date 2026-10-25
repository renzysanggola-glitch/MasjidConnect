package com.example.data.models

data class Mosque(
    val id: String = "MC-2024001",
    val name: String = "Masjid Al-Ikhlas",
    val address: String = "Jl. Kebon Jeruk No. 12, Jakarta Barat, DKI Jakarta, 11530",
    val phone: String = "+62 812-3456-7890",
    val email: String = "info@masjidalikhlas.id",
    val website: String = "www.masjidalikhlas.id",
    val logo: String = "mosque_logo",
    val photo: String = "mosque_photo"
)

data class User(
    val id: String,
    val name: String,
    val email: String,
    val role: String, // "ADMINISTRATOR", "BENDAHARA", "SEKRETARIS", "MARBOT"
    val avatar: String = "",
    val isActive: Boolean = true
)

data class TransactionCategory(
    val id: String,
    val name: String,
    val type: String, // "income" or "expense"
    val icon: String = "category"
)

data class Transaction(
    val id: String,
    val categoryId: String,
    val categoryName: String,
    val type: String, // "income" or "expense"
    val amount: Long,
    val description: String,
    val transactionDate: String, // e.g. "18 Sep 2026 • 13:45"
    val proofFile: String? = null
)

data class Activity(
    val id: String,
    val title: String,
    val speaker: String,
    val category: String, // "Kajian", "Rapat", "Event"
    val dateBadgeMonth: String, // "SEPT"
    val dateBadgeDay: String, // "19"
    val fullDate: String, // "19 September 2026"
    val time: String, // "Ba'da Ashar" or "09:00 - 11:00"
    val location: String, // "Ruang Utama", "Lantai 2"
    val description: String = ""
)

data class PrayerTime(
    val name: String,
    val time: String,
    val isActive: Boolean = false
)

data class PrayerConfig(
    val autoGps: Boolean = true,
    val calculationMethod: String = "Kemenag RI (SIHAT)",
    val subuhOffset: Int = 0,
    val dzuhurOffset: Int = 2,
    val asharOffset: Int = 0,
    val maghribOffset: Int = 2,
    val isyaOffset: Int = 0
)

data class NotificationConfig(
    val adzanNotification: Boolean = true,
    val iqomahReminder: Boolean = true,
    val newAnnouncements: Boolean = true,
    val financialReport: Boolean = false,
    val studyAgendas: Boolean = true,
    val batterySaver: Boolean = false
)

data class DisplayContent(
    val id: String,
    val type: String, // "image", "video", "ticker"
    val title: String,
    val fileUrl: String = "",
    val durationSeconds: Int = 15,
    val durationLabel: String = "Dur: 15s",
    val uploadedTime: String = "2 hours ago",
    val sortOrder: Int = 0,
    val isActive: Boolean = true
)

// Sediakan tipe tambahan sesuai arahan prompt (disiapkan untuk pengembangan berikutnya)
data class Announcement(
    val id: String,
    val title: String,
    val content: String,
    val date: String,
    val author: String
)

data class NotificationItem(
    val id: String,
    val title: String,
    val message: String,
    val timestamp: String,
    val isRead: Boolean = false
)

data class Donation(
    val id: String,
    val donorName: String,
    val campaignId: String,
    val amount: Long,
    val date: String,
    val paymentMethod: String
)

data class DonationCampaign(
    val id: String,
    val title: String,
    val targetAmount: Long,
    val collectedAmount: Long,
    val deadline: String
)

data class Inventory(
    val id: String,
    val itemName: String,
    val quantity: Int,
    val condition: String,
    val location: String
)

data class Maintenance(
    val id: String,
    val title: String,
    val cost: Long,
    val status: String,
    val scheduledDate: String
)
