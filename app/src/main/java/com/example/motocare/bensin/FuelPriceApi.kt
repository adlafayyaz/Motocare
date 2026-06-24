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

    fun fetchFuelPrice(fuelType: String, brand: String, octane: String): FuelPriceResult {
        val path = "/api-bbm/${fuelType.lowercase()}/${brand.lowercase()}/$octane"
        val connection = URL(BASE_URL + path).openConnection() as HttpURLConnection
        connection.connectTimeout = 8000
        connection.readTimeout = 8000
        connection.requestMethod = "GET"

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
}
