package com.example.motocare.bensin

class FuelPriceRepository {
    fun fetch(
        fuelType: String,
        brand: String,
        octane: String,
        onResult: (Result<FuelPriceResult>) -> Unit
    ) {
        Thread {
            val result = runCatching { FuelPriceApi.fetchFuelPrice(fuelType, brand, octane) }
            onResult(result)
        }.start()
    }
}
