package com.example.motocare.bensin

import java.net.HttpURLConnection
import java.net.URL

data class FuelPriceResult(
    val brand: String,
    val product: String,
    val octane: String,
    val price: Int,
    val update: String
)

object FuelPriceApi {
    private const val LIVE_SOURCE_URL = "https://isibens.in/"
    private val SCRAPED_BRAND_ORDER = listOf("Pertamina", "Vivo", "BP", "Shell")

    fun availableBensinOptions(): LinkedHashMap<String, List<String>> {
        return STATIC_BENSIN.entries.associateTo(linkedMapOf()) { (brand, options) ->
            brand to options.keys.toList()
        }
    }

    fun fetchFuelPrice(fuelType: String, brand: String, octane: String): FuelPriceResult {
        if (!fuelType.equals("bensin", true)) throw IllegalArgumentException("Jenis BBM belum tersedia.")
        if (supportedBrand(brand) == null) throw IllegalArgumentException("Merek BBM belum tersedia.")
        return fetchFromIsibensin(brand, octane) ?: throw IllegalStateException("Harga tidak ditemukan.")
    }

    fun localFallback(fuelType: String, brand: String, octane: String): FuelPriceResult? {
        if (!fuelType.equals("bensin", true)) return null
        return staticFallback(brand, octane)
    }

    private fun supportedBrand(brand: String): String? {
        return STATIC_BENSIN.keys.firstOrNull { it.equals(brand, true) }
    }

    private fun fetchFromIsibensin(brand: String, octane: String): FuelPriceResult? {
        val body = (URL(LIVE_SOURCE_URL).openConnection() as HttpURLConnection).run {
            connectTimeout = 8000
            readTimeout = 8000
            requestMethod = "GET"
            inputStream.bufferedReader().use { it.readText() }
        }
        val update = Regex("Update harga per\\s+([^<]+)").find(body)
            ?.groupValues
            ?.get(1)
            ?.trim()
            ?: "-"
        val table = body.substringAfter("<h4>Bensin")
            .substringBefore("<h4>Diesel")
        val row = Regex("<tr>\\s*<th scope=\"row\">$octane</th>(.*?)</tr>", RegexOption.DOT_MATCHES_ALL)
            .find(table)
            ?.groupValues
            ?.get(1)
            ?: return null
        val brandIndex = SCRAPED_BRAND_ORDER.indexOfFirst {
            it.equals(brand, true)
        }
        if (brandIndex < 0) return null
        val cells = Regex("<td>(.*?)</td>", RegexOption.DOT_MATCHES_ALL)
            .findAll(row)
            .map { it.groupValues[1] }
            .toList()
        val cell = cells.getOrNull(brandIndex) ?: return null
        if (cell.contains("-")) return null
        val priceText = Regex("(\\d{1,3}(?:\\.\\d{3})*)").find(cell)?.value ?: return null
        val price = priceText.filter(Char::isDigit).toInt()
        if (price <= 0) return null
        val product = Regex("<small>\\s*([^<]+)\\s*</small>").find(cell)
            ?.groupValues
            ?.get(1)
            ?.trim()
            ?: "-"
        return FuelPriceResult(
            brand = SCRAPED_BRAND_ORDER[brandIndex],
            product = product,
            octane = octane,
            price = price,
            update = update
        )
    }

    private fun staticFallback(brand: String, octane: String): FuelPriceResult? {
        val normalizedBrand = supportedBrand(brand) ?: return null
        val data = STATIC_BENSIN[normalizedBrand]?.get(octane) ?: return null
        return FuelPriceResult(
            brand = normalizedBrand,
            product = data.first,
            octane = octane,
            price = data.second,
            update = "fallback statis"
        )
    }

    private val STATIC_BENSIN = mapOf(
        "Pertamina" to mapOf(
            "90" to ("Pertalite" to 10000),
            "92" to ("Pertamax" to 16250),
            "95" to ("Pertamax Green" to 17000),
            "98" to ("Pertamax Turbo" to 20750)
        ),
        "Vivo" to mapOf(
            "95" to ("Revvo95" to 17240)
        ),
        "BP" to mapOf(
            "92" to ("BP 92" to 16670),
            "95" to ("BP Ultimate" to 17240)
        )
    )
}
