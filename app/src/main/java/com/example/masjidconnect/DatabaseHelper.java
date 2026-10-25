package com.example.masjidconnect;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "masjid_connect.db";
    private static final int DATABASE_VERSION = 1;

    // Tabel Keuangan
    public static final String TABLE_KEUANGAN = "keuangan";
    public static final String COL_K_ID = "id";
    public static final String COL_K_JUDUL = "judul";
    public static final String COL_K_JUMLAH = "jumlah";
    public static final String COL_K_TIPE = "tipe"; // "pemasukan" atau "pengeluaran"
    public static final String COL_K_TANGGAL = "tanggal";

    // Tabel Agenda
    public static final String TABLE_AGENDA = "agenda";
    public static final String COL_A_ID = "id";
    public static final String COL_A_JUDUL = "judul";
    public static final String COL_A_PEMBICARA = "pembicara";
    public static final String COL_A_TANGGAL = "tanggal";
    public static final String COL_A_WAKTU = "waktu";
    public static final String COL_A_KATEGORI = "kategori";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableKeuangan = "CREATE TABLE " + TABLE_KEUANGAN + " (" +
                COL_K_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_K_JUDUL + " TEXT, " +
                COL_K_JUMLAH + " REAL, " +
                COL_K_TIPE + " TEXT, " +
                COL_K_TANGGAL + " TEXT)";

        String createTableAgenda = "CREATE TABLE " + TABLE_AGENDA + " (" +
                COL_A_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_A_JUDUL + " TEXT, " +
                COL_A_PEMBICARA + " TEXT, " +
                COL_A_TANGGAL + " TEXT, " +
                COL_A_WAKTU + " TEXT, " +
                COL_A_KATEGORI + " TEXT)";

        db.execSQL(createTableKeuangan);
        db.execSQL(createTableAgenda);

        // Data awal sampel
        db.execSQL("INSERT INTO " + TABLE_KEUANGAN + " (judul, jumlah, tipe, tanggal) VALUES ('Infaq Jumat Berkah', 1250000, 'pemasukan', '2026-09-18')");
        db.execSQL("INSERT INTO " + TABLE_KEUANGAN + " (judul, jumlah, tipe, tanggal) VALUES ('Listrik & Air (PLN)', 850000, 'pengeluaran', '2026-09-17')");

        db.execSQL("INSERT INTO " + TABLE_AGENDA + " (judul, pembicara, tanggal, waktu, kategori) VALUES ('Kajian Fiqih Ibadah', 'Ust. Salim A. Fillah', '19 SEPT', 'Ba''da Ashar', 'Kajian')");
        db.execSQL("INSERT INTO " + TABLE_AGENDA + " (judul, pembicara, tanggal, waktu, kategori) VALUES ('Tahsin Al-Qur''an Dewasa', 'Ust. Abu Hurairah', '20 SEPT', '09:00 - 11:00', 'Kajian')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_KEUANGAN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AGENDA);
        onCreate(db);
    }

    // --- FITUR KEUANGAN ---
    public boolean insertKeuangan(String judul, double jumlah, String tipe, String tanggal) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_K_JUDUL, judul);
        cv.put(COL_K_JUMLAH, jumlah);
        cv.put(COL_K_TIPE, tipe);
        cv.put(COL_K_TANGGAL, tanggal);
        long result = db.insert(TABLE_KEUANGAN, null, cv);
        return result != -1;
    }

    public List<HashMap<String, String>> getAllKeuangan() {
        List<HashMap<String, String>> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_KEUANGAN + " ORDER BY " + COL_K_ID + " DESC", null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<>();
                map.put("id", cursor.getString(cursor.getColumnIndexOrThrow(COL_K_ID)));
                map.put("judul", cursor.getString(cursor.getColumnIndexOrThrow(COL_K_JUDUL)));
                map.put("jumlah", String.valueOf(cursor.getDouble(cursor.getColumnIndexOrThrow(COL_K_JUMLAH))));
                map.put("tipe", cursor.getString(cursor.getColumnIndexOrThrow(COL_K_TIPE)));
                map.put("tanggal", cursor.getString(cursor.getColumnIndexOrThrow(COL_K_TANGGAL)));
                list.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public double getTotalPemasukan() {
        double total = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(" + COL_K_JUMLAH + ") FROM " + TABLE_KEUANGAN + " WHERE " + COL_K_TIPE + "='pemasukan'", null);
        if (cursor.moveToFirst()) {
            total = cursor.getDouble(0);
        }
        cursor.close();
        return total;
    }

    public double getTotalPengeluaran() {
        double total = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(" + COL_K_JUMLAH + ") FROM " + TABLE_KEUANGAN + " WHERE " + COL_K_TIPE + "='pengeluaran'", null);
        if (cursor.moveToFirst()) {
            total = cursor.getDouble(0);
        }
        cursor.close();
        return total;
    }

    // Rekap Arus Kas 6 Bulan Terakhir untuk Diagram Bar Chart
    public List<HashMap<String, Object>> getMonthlyCashFlow() {
        List<HashMap<String, Object>> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT strftime('%m', " + COL_K_TANGGAL + ") as bulan_num, " +
                "SUM(CASE WHEN " + COL_K_TIPE + " = 'pemasukan' THEN " + COL_K_JUMLAH + " ELSE 0 END) as total_masuk, " +
                "SUM(CASE WHEN " + COL_K_TIPE + " = 'pengeluaran' THEN " + COL_K_JUMLAH + " ELSE 0 END) as total_keluar " +
                "FROM " + TABLE_KEUANGAN + " " +
                "GROUP BY strftime('%m', " + COL_K_TANGGAL + ") " +
                "ORDER BY strftime('%Y-%m', " + COL_K_TANGGAL + ") DESC LIMIT 6";

        Cursor cursor = db.rawQuery(query, null);

        String[] namaBulan = {"Jan", "Feb", "Mar", "Apr", "Mei", "Jun", "Jul", "Agu", "Sep", "Okt", "Nov", "Des"};

        if (cursor.moveToLast()) { // Urutkan dari bulan tertua ke terbaru
            do {
                HashMap<String, Object> map = new HashMap<>();
                String bulanStr = cursor.getString(0);
                if (bulanStr != null) {
                    int bulanIdx = Integer.parseInt(bulanStr) - 1;
                    if (bulanIdx >= 0 && bulanIdx < 12) {
                        map.put("bulan", namaBulan[bulanIdx]);
                    } else {
                        map.put("bulan", "Bulan");
                    }
                } else {
                    map.put("bulan", "Bulan");
                }
                map.put("pemasukan", cursor.getDouble(1));
                map.put("pengeluaran", cursor.getDouble(2));
                list.add(map);
            } while (cursor.moveToPrevious());
        }
        cursor.close();
        return list;
    }

    // --- FITUR AGENDA ---
    public boolean insertAgenda(String judul, String pembicara, String tanggal, String waktu, String kategori) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_A_JUDUL, judul);
        cv.put(COL_A_PEMBICARA, pembicara);
        cv.put(COL_A_TANGGAL, tanggal);
        cv.put(COL_A_WAKTU, waktu);
        cv.put(COL_A_KATEGORI, kategori);
        long result = db.insert(TABLE_AGENDA, null, cv);
        return result != -1;
    }

    public List<HashMap<String, String>> getAllAgenda() {
        List<HashMap<String, String>> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_AGENDA + " ORDER BY " + COL_A_ID + " DESC", null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<>();
                map.put("id", cursor.getString(cursor.getColumnIndexOrThrow(COL_A_ID)));
                map.put("judul", cursor.getString(cursor.getColumnIndexOrThrow(COL_A_JUDUL)));
                map.put("pembicara", cursor.getString(cursor.getColumnIndexOrThrow(COL_A_PEMBICARA)));
                map.put("tanggal", cursor.getString(cursor.getColumnIndexOrThrow(COL_A_TANGGAL)));
                map.put("waktu", cursor.getString(cursor.getColumnIndexOrThrow(COL_A_WAKTU)));
                map.put("kategori", cursor.getString(cursor.getColumnIndexOrThrow(COL_A_KATEGORI)));
                list.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
}