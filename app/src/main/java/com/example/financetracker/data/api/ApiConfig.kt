package com.example.financetracker.data.api

/**
 * Konfigurasi koneksi ke Backend SmartFinance.
 *
 * CARA MENGHUBUNGKAN KE BACKEND:
 * 1. Jika memakai Android Emulator:
 *    Gunakan "http://10.0.2.2:8080/api/" (10.0.2.2 merujuk ke localhost pada PC host).
 *
 * 2. Jika memakai HP fisik / Real Device:
 *    Gunakan IP lokal laptop Anda di jaringan WiFi yang sama, misalnya:
 *    "http://192.168.1.10:8080/api/"
 *
 * 3. Jika backend berjalan di port lain (misal 3000 untuk Express/NestJS, 8000 untuk Laravel/FastAPI, 5000 dsb):
 *    Cukup ubah port di BASE_URL di bawah ini.
 *
 * 4. Mode Mock Fallback:
 *    Jika USE_MOCK_FALLBACK = true, aplikasi akan tetap berjalan dengan data contoh yang interaktif
 *    meskipun backend belum dinyalakan atau tidak dapat dijangkau.
 */
object ApiConfig {
    // Ganti port & host sesuai backend Anda di folder be/
    var BASE_URL: String = "http://10.0.2.2:8080/api/"

    // Ubah ke false jika ingin hanya mengandalkan data dari server backend
    var USE_MOCK_FALLBACK: Boolean = true

    // Token JWT / Bearer yang otomatis disimpan setelah Login/Register
    var authToken: String? = null
}

