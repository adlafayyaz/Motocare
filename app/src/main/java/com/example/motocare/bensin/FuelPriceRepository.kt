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
                .recoverCatching {
                    FuelPriceApi.localFallback(fuelType, brand, octane) ?: throw it
                }
            onResult(result)
        }.start()
    }
}
