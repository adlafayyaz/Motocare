package com.example.motocare.bensin

import org.json.JSONObject
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
    private const val BASE_URL = "https://api.alifmaulidanar.my.id"
    private const val LIVE_SOURCE_URL = "https://isibens.in/"

    fun fetchFuelPrice(fuelType: String, brand: String, octane: String): FuelPriceResult {
        val path = "/api-bbm/${fuelType.lowercase()}/${brand.lowercase()}/$octane"
        val connection = URL(BASE_URL + path).openConnection() as HttpURLConnection
        connection.connectTimeout = 8000
        connection.readTimeout = 8000
        connection.requestMethod = "GET"
        if (connection.responseCode !in 200..299) {
            throw IllegalStateException("HTTP ${connection.responseCode}")
        }

        val body = connection.inputStream.bufferedReader().use { it.readText() }
        val json = JSONObject(body)
        val priceText = json.optString("harga")
        return FuelPriceResult(
            brand = json.optString("brand", brand),
            product = json.optString("produk", "-"),
            octane = json.optString("ron", json.optString("cn", octane)),
            price = priceText.filter(Char::isDigit).toIntOrNull() ?: 0,
            update = json.optString("update", "-")
        )
    }

    fun localFallback(fuelType: String, brand: String, octane: String): FuelPriceResult? {
        if (!fuelType.equals("bensin", true)) return null
        return fetchFromIsibensin(brand, octane) ?: staticFallback(brand, octane)
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
        val brandIndex = listOf("Pertamina", "Vivo", "BP", "Shell").indexOfFirst {
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
        val product = Regex("<small>\\s*([^<]+)\\s*</small>").find(cell)
            ?.groupValues
            ?.get(1)
            ?.trim()
            ?: "-"
        return FuelPriceResult(
            brand = listOf("Pertamina", "Vivo", "BP", "Shell")[brandIndex],
            product = product,
            octane = octane,
            price = priceText.filter(Char::isDigit).toInt(),
            update = update
        )
    }

    private fun staticFallback(brand: String, octane: String): FuelPriceResult? {
        val normalizedBrand = brand.replaceFirstChar { it.uppercase() }
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
            "92" to ("Pertamax" to 12400),
            "95" to ("Pertamax Green" to 13500),
            "98" to ("Pertamax Turbo" to 14400)
        ),
        "Vivo" to mapOf(
            "90" to ("Revvo90" to 11300),
            "92" to ("Revvo92" to 13087),
            "95" to ("Revvo95" to 13995)
        ),
        "BP" to mapOf(
            "90" to ("BP 90" to 12740),
            "92" to ("BP 92" to 12990),
            "95" to ("BP Ultimate" to 14190)
        ),
        "Shell" to mapOf(
            "92" to ("Super" to 13280),
            "95" to ("V-Power" to 14190),
            "98" to ("V-Power Nitro+" to 14540)
        )
    )
}
