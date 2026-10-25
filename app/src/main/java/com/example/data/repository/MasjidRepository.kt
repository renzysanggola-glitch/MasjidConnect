package com.example.data.repository

import com.example.data.models.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.NumberFormat
import java.util.Locale

/**
 * MasjidRepository
 *
 * Catatan Integrasi Backend:
 * - API_BASE_URL: URL root endpoint REST API backend (misal: Node.js Express, Laravel, atau PHP MySQL REST)
 * - USE_MOCK: Set false jika ingin menghubungkan ke REST API sungguhan via Retrofit/Ktor
 *
 * Endpoint yang dibutuhkan:
 * 1. GET /transactions, POST /transactions, DELETE /transactions/{id}
 * 2. GET /activities, POST /activities, DELETE /activities/{id}
 * 3. GET /mosque, PUT /mosque
 * 4. GET /users, POST /users, DELETE /users/{id}
 * 5. GET /prayer-config, PUT /prayer-config
 * 6. GET /notification-config, PUT /notification-config
 * 7. GET /display-contents, POST /display-contents, PUT /display-contents/reorder, DELETE /display-contents/{id}
 */
object MasjidRepository {
    const val API_BASE_URL = "https://api.masjidconnect.id/v1"
    const val USE_MOCK = true

    // Current logged in user
    val currentUser = User(
        id = "usr-1",
        name = "Zaidan Ahmad",
        email = "admin@alikhlas.com",
        role = "ADMINISTRATOR",
        isActive = true
    )

    // Mosque Profile State
    private val _mosque = MutableStateFlow(
        Mosque(
            id = "MC-2024001",
            name = "Masjid Al-Ikhlas",
            address = "Jl. Kebon Jeruk No. 12, Jakarta Barat, DKI Jakarta, 11530",
            phone = "+62 812-3456-7890",
            email = "info@masjidalikhlas.id",
            website = "www.masjidalikhlas.id"
        )
    )
    val mosque: StateFlow<Mosque> = _mosque.asStateFlow()

    // Base Saldo default
    private const val BASE_SALDO = 37740000L

    // Transactions State
    private val _transactions = MutableStateFlow(
        listOf(
            Transaction(
                id = "tx-1",
                categoryId = "cat-1",
                categoryName = "Pemasukan",
                type = "income",
                amount = 1250000L,
                description = "Infaq Jumat Berkah",
                transactionDate = "18 Sep 2026 • 13:45",
                proofFile = "bukti_infaq_jumat.jpg"
            ),
            Transaction(
                id = "tx-2",
                categoryId = "cat-2",
                categoryName = "Operasional",
                type = "expense",
                amount = 850000L,
                description = "Listrik & Air (PLN)",
                transactionDate = "17 Sep 2026 • 10:20",
                proofFile = "struk_pln_sep.pdf"
            ),
            Transaction(
                id = "tx-3",
                categoryId = "cat-3",
                categoryName = "Donasi",
                type = "income",
                amount = 5000000L,
                description = "Sumbangan Perbaikan Atap",
                transactionDate = "15 Sep 2026 • 09:12",
                proofFile = "transfer_donasi_atap.jpg"
            ),
            Transaction(
                id = "tx-4",
                categoryId = "cat-2",
                categoryName = "Operasional",
                type = "expense",
                amount = 270000L,
                description = "Maintenance Sound System",
                transactionDate = "12 Sep 2026 • 16:30",
                proofFile = "nota_servis_sound.jpg"
            )
        )
    )
    val transactions: StateFlow<List<Transaction>> = _transactions.asStateFlow()

    // Transaction Categories
    val categories = listOf(
        TransactionCategory("cat-1", "Infaq & Sedekah", "income"),
        TransactionCategory("cat-3", "Donasi Pembangunan", "income"),
        TransactionCategory("cat-4", "Zakat Mal & Fitrah", "income"),
        TransactionCategory("cat-2", "Operasional & Kebersihan", "expense"),
        TransactionCategory("cat-5", "Listrik & Air", "expense"),
        TransactionCategory("cat-6", "Honor Guru & Marbot", "expense"),
        TransactionCategory("cat-7", "Sosial & Santunan", "expense"),
        TransactionCategory("cat-8", "Lainnya", "expense")
    )

    // Activities / Kajian State
    private val _activities = MutableStateFlow(
        listOf(
            Activity(
                id = "act-1",
                title = "Kajian Fiqih Ibadah",
                speaker = "Ust. Salim A. Fillah",
                category = "Kajian",
                dateBadgeMonth = "SEPT",
                dateBadgeDay = "19",
                fullDate = "19 September 2026",
                time = "Ba'da Ashar",
                location = "Ruang Utama",
                description = "Membahas kitab Bidayatul Mujtahid bab Thaharah dan Sholat Berjamaah."
            ),
            Activity(
                id = "act-2",
                title = "Tahsin Al-Qur'an Dewasa",
                speaker = "Ust. Abu Hurairah",
                category = "Kajian",
                dateBadgeMonth = "SEPT",
                dateBadgeDay = "20",
                fullDate = "20 September 2026",
                time = "09:00 - 11:00",
                location = "Lantai 2",
                description = "Pelatihan makharijul huruf dan sifatul huruf untuk jamaah ikhwan & akhwat."
            ),
            Activity(
                id = "act-3",
                title = "Rapat Pengurus Harian",
                speaker = "Bpk. H. Ahmad Fauzi",
                category = "Rapat",
                dateBadgeMonth = "SEPT",
                dateBadgeDay = "21",
                fullDate = "21 September 2026",
                time = "20:00 - Selesai",
                location = "Kantor Masjid",
                description = "Evaluasi program Maulid Nabi dan penyusunan laporan keuangan triwulan III."
            ),
            Activity(
                id = "act-4",
                title = "Santunan Anak Yatim",
                speaker = "Divisi Sosial",
                category = "Event",
                dateBadgeMonth = "SEPT",
                dateBadgeDay = "25",
                fullDate = "25 September 2026",
                time = "08:30 - 12:00",
                location = "Halaman Masjid",
                description = "Penyaluran dana santunan dan paket sekolah untuk 100 anak yatim binaan."
            )
        )
    )
    val activities: StateFlow<List<Activity>> = _activities.asStateFlow()

    // Users List State
    private val _users = MutableStateFlow(
        listOf(
            User("u-1", "H. Ahmad Fauzi", "ahmad@alikhlas.id", "ADMINISTRATOR"),
            User("u-2", "Bpk. Rahmat", "rahmat@alikhlas.id", "BENDAHARA"),
            User("u-3", "Ust. Abdullah", "abdullah@alikhlas.id", "SEKRETARIS"),
            User("u-4", "Sdr. Ikhsan", "ikhsan@alikhlas.id", "MARBOT")
        )
    )
    val users: StateFlow<List<User>> = _users.asStateFlow()

    // Notification Configuration
    private val _notificationConfig = MutableStateFlow(NotificationConfig())
    val notificationConfig: StateFlow<NotificationConfig> = _notificationConfig.asStateFlow()

    // Prayer Schedule Configuration
    private val _prayerConfig = MutableStateFlow(PrayerConfig())
    val prayerConfig: StateFlow<PrayerConfig> = _prayerConfig.asStateFlow()

    // Digital Signage Content
    private val _displayContents = MutableStateFlow(
        listOf(
            DisplayContent(
                id = "d-1",
                type = "image",
                title = "Poster Kajian Jumat Malam.png",
                durationSeconds = 15,
                durationLabel = "Dur: 15s",
                uploadedTime = "2 hours ago",
                sortOrder = 1,
                isActive = true
            ),
            DisplayContent(
                id = "d-2",
                type = "image",
                title = "Donasi Renovasi Atap.jpg",
                durationSeconds = 10,
                durationLabel = "Dur: 10s",
                uploadedTime = "1 day ago",
                sortOrder = 2,
                isActive = true
            ),
            DisplayContent(
                id = "d-3",
                type = "video",
                title = "Video Qurban.mp4",
                durationSeconds = 60,
                durationLabel = "FULL VIDEO",
                uploadedTime = "3 days ago",
                sortOrder = 3,
                isActive = true
            )
        )
    )
    val displayContents: StateFlow<List<DisplayContent>> = _displayContents.asStateFlow()

    // Running Ticker Text
    private val _runningTicker = MutableStateFlow(
        "Selamat Datang di Masjid Al-Ikhlas • Luruskan dan rapatkan shaf saat sholat berjamaah • Mohon senyapkan nada dering HP Anda • Infaq & Donasi Renovasi Atap dapat disalurkan melalui QRIS di pintu utama"
    )
    val runningTicker: StateFlow<String> = _runningTicker.asStateFlow()

    // Calculated Balances
    fun getTotalIncome(): Long {
        return _transactions.value.filter { it.type == "income" }.sumOf { it.amount }
    }

    fun getTotalExpense(): Long {
        return _transactions.value.filter { it.type == "expense" }.sumOf { it.amount }
    }

    fun getTotalBalance(): Long {
        // Base initial reserve + net change
        return BASE_SALDO + getTotalIncome() - getTotalExpense()
    }

    // Dynamic prayer schedule reflecting user offsets
    fun getPrayerTimes(): List<PrayerTime> {
        val cfg = _prayerConfig.value
        fun formatTime(hour: Int, min: Int, offset: Int): String {
            val totalMins = (hour * 60 + min + offset + 1440) % 1440
            val h = totalMins / 60
            val m = totalMins % 60
            return "%02d:%02d".format(h, m)
        }

        return listOf(
            PrayerTime("Subuh", formatTime(4, 32, cfg.subuhOffset), isActive = false),
            PrayerTime("Dzuhur", formatTime(11, 49, cfg.dzuhurOffset), isActive = false),
            PrayerTime("Ashar", formatTime(15, 8, cfg.asharOffset), isActive = true), // Aktif
            PrayerTime("Maghrib", formatTime(17, 53, cfg.maghribOffset), isActive = false),
            PrayerTime("Isya", formatTime(19, 6, cfg.isyaOffset), isActive = false)
        )
    }

    // Actions
    fun addTransaction(tx: Transaction) {
        _transactions.value = listOf(tx) + _transactions.value
    }

    fun deleteTransaction(id: String) {
        _transactions.value = _transactions.value.filter { it.id != id }
    }

    fun addActivity(act: Activity) {
        _activities.value = _activities.value + act
    }

    fun deleteActivity(id: String) {
        _activities.value = _activities.value.filter { it.id != id }
    }

    fun addUser(user: User) {
        _users.value = _users.value + user
    }

    fun deleteUser(id: String) {
        _users.value = _users.value.filter { it.id != id }
    }

    fun updateMosque(updated: Mosque) {
        _mosque.value = updated
    }

    fun updateNotificationConfig(config: NotificationConfig) {
        _notificationConfig.value = config
    }

    fun updatePrayerConfig(config: PrayerConfig) {
        _prayerConfig.value = config
    }

    fun addDisplayMedia(item: DisplayContent) {
        _displayContents.value = _displayContents.value + item
    }

    fun deleteDisplayMedia(id: String) {
        _displayContents.value = _displayContents.value.filter { it.id != id }
    }

    fun moveDisplayItem(index: Int, up: Boolean) {
        val list = _displayContents.value.toMutableList()
        val targetIndex = if (up) index - 1 else index + 1
        if (targetIndex in 0 until list.size) {
            val item = list.removeAt(index)
            list.add(targetIndex, item)
            _displayContents.value = list
        }
    }

    fun toggleDisplayItemActive(id: String) {
        _displayContents.value = _displayContents.value.map {
            if (it.id == id) it.copy(isActive = !it.isActive) else it
        }
    }

    fun updateTicker(text: String) {
        _runningTicker.value = text
    }

    // Helper formatter
    fun formatRupiah(amount: Long): String {
        val format = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        format.maximumFractionDigits = 0
        return format.format(amount).replace("Rp", "Rp ")
    }
}
