package com.example.motocare.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MotoCareDbHelper(context: Context) : SQLiteOpenHelper(
    context,
    DATABASE_NAME,
    null,
    DATABASE_VERSION
) {
    override fun onCreate(db: SQLiteDatabase) {
        CREATE_TABLES.forEach(db::execSQL)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        DROP_TABLES.forEach(db::execSQL)
        onCreate(db)
    }

    fun insertMotor(motor: Motor): Long {
        val db = writableDatabase
        val shouldBeActive = getMotorCount(db) == 0
        val values = ContentValues().apply {
            put("name", motor.name)
            put("plate_number", motor.plateNumber)
            put("current_kilometer", motor.currentKilometer)
            put("is_active", if (shouldBeActive || motor.isActive) 1 else 0)
        }
        return db.insert(TABLE_MOTORS, null, values)
    }

    fun updateMotor(motor: Motor): Int {
        val values = ContentValues().apply {
            put("name", motor.name)
            put("plate_number", motor.plateNumber)
            put("current_kilometer", motor.currentKilometer)
            put("is_active", if (motor.isActive) 1 else 0)
        }
        return writableDatabase.update(
            TABLE_MOTORS,
            values,
            "id = ?",
            arrayOf(motor.id.toString())
        )
    }

    fun deleteMotor(id: Long): Int {
        return writableDatabase.delete(TABLE_MOTORS, "id = ?", arrayOf(id.toString()))
    }

    fun getMotor(id: Long): Motor? {
        readableDatabase.query(
            TABLE_MOTORS,
            null,
            "id = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        ).use { cursor ->
            return if (cursor.moveToFirst()) cursor.toMotor() else null
        }
    }

    fun getAllMotors(): List<Motor> {
        val motors = mutableListOf<Motor>()
        readableDatabase.query(
            TABLE_MOTORS,
            null,
            null,
            null,
            null,
            null,
            "is_active DESC, name ASC"
        ).use { cursor ->
            while (cursor.moveToNext()) {
                motors.add(cursor.toMotor())
            }
        }
        return motors
    }

    fun getActiveMotor(): Motor? {
        readableDatabase.query(
            TABLE_MOTORS,
            null,
            "is_active = 1",
            null,
            null,
            null,
            "id DESC",
            "1"
        ).use { cursor ->
            return if (cursor.moveToFirst()) cursor.toMotor() else null
        }
    }

    fun getMonthlyExpenseTotal(): Int {
        return getServiceMonthlyTotal() + getOilMonthlyTotal() + getFuelMonthlyTotal() + getTaxMonthlyTotal()
    }

    fun getFuelMonthlyTotal(): Int = sumCost(TABLE_FUEL_RECORDS)

    fun getTaxMonthlyTotal(): Int = 0

    fun getOilMonthlyTotal(): Int = sumCost(TABLE_OIL_RECORDS)

    fun getServiceMonthlyTotal(): Int = sumCost(TABLE_SERVICE_RECORDS)

    fun insertServis(servis: Servis): Long {
        return writableDatabase.insert(TABLE_SERVICE_RECORDS, null, servis.toValues())
    }

    fun updateServis(servis: Servis): Int {
        return writableDatabase.update(
            TABLE_SERVICE_RECORDS,
            servis.toValues(),
            "id = ?",
            arrayOf(servis.id.toString())
        )
    }

    fun deleteServis(id: Long): Int {
        return writableDatabase.delete(TABLE_SERVICE_RECORDS, "id = ?", arrayOf(id.toString()))
    }

    fun getServis(id: Long): Servis? {
        readableDatabase.query(
            TABLE_SERVICE_RECORDS,
            null,
            "id = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        ).use { cursor ->
            return if (cursor.moveToFirst()) cursor.toServis() else null
        }
    }

    fun getServisByMotor(motorId: Long): List<Servis> {
        val items = mutableListOf<Servis>()
        readableDatabase.query(
            TABLE_SERVICE_RECORDS,
            null,
            "motor_id = ?",
            arrayOf(motorId.toString()),
            null,
            null,
            "service_date DESC, id DESC"
        ).use { cursor ->
            while (cursor.moveToNext()) items.add(cursor.toServis())
        }
        return items
    }

    fun getLatestServis(motorId: Long): Servis? {
        readableDatabase.query(
            TABLE_SERVICE_RECORDS,
            null,
            "motor_id = ?",
            arrayOf(motorId.toString()),
            null,
            null,
            "kilometer DESC, id DESC",
            "1"
        ).use { cursor ->
            return if (cursor.moveToFirst()) cursor.toServis() else null
        }
    }

    fun insertOli(oli: Oli): Long {
        return writableDatabase.insert(TABLE_OIL_RECORDS, null, oli.toValues())
    }

    fun updateOli(oli: Oli): Int {
        return writableDatabase.update(
            TABLE_OIL_RECORDS,
            oli.toValues(),
            "id = ?",
            arrayOf(oli.id.toString())
        )
    }

    fun deleteOli(id: Long): Int {
        return writableDatabase.delete(TABLE_OIL_RECORDS, "id = ?", arrayOf(id.toString()))
    }

    fun getOli(id: Long): Oli? {
        readableDatabase.query(
            TABLE_OIL_RECORDS,
            null,
            "id = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        ).use { cursor ->
            return if (cursor.moveToFirst()) cursor.toOli() else null
        }
    }

    fun getOliByMotor(motorId: Long): List<Oli> {
        val items = mutableListOf<Oli>()
        readableDatabase.query(
            TABLE_OIL_RECORDS,
            null,
            "motor_id = ?",
            arrayOf(motorId.toString()),
            null,
            null,
            "oil_change_date DESC, id DESC"
        ).use { cursor ->
            while (cursor.moveToNext()) items.add(cursor.toOli())
        }
        return items
    }

    fun getLatestOli(motorId: Long): Oli? {
        readableDatabase.query(
            TABLE_OIL_RECORDS,
            null,
            "motor_id = ?",
            arrayOf(motorId.toString()),
            null,
            null,
            "kilometer DESC, id DESC",
            "1"
        ).use { cursor ->
            return if (cursor.moveToFirst()) cursor.toOli() else null
        }
    }

    fun insertBensin(bensin: Bensin): Long {
        return writableDatabase.insert(TABLE_FUEL_RECORDS, null, bensin.toValues())
    }

    fun updateBensin(bensin: Bensin): Int {
        return writableDatabase.update(
            TABLE_FUEL_RECORDS,
            bensin.toValues(),
            "id = ?",
            arrayOf(bensin.id.toString())
        )
    }

    fun deleteBensin(id: Long): Int {
        return writableDatabase.delete(TABLE_FUEL_RECORDS, "id = ?", arrayOf(id.toString()))
    }

    fun getBensin(id: Long): Bensin? {
        readableDatabase.query(
            TABLE_FUEL_RECORDS,
            null,
            "id = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        ).use { cursor ->
            return if (cursor.moveToFirst()) cursor.toBensin() else null
        }
    }

    fun getBensinByMotor(motorId: Long): List<Bensin> {
        val items = mutableListOf<Bensin>()
        readableDatabase.query(
            TABLE_FUEL_RECORDS,
            null,
            "motor_id = ?",
            arrayOf(motorId.toString()),
            null,
            null,
            "fuel_date DESC, id DESC"
        ).use { cursor ->
            while (cursor.moveToNext()) items.add(cursor.toBensin())
        }
        return items
    }

    fun setActiveMotor(id: Long) {
        writableDatabase.beginTransaction()
        try {
            val inactive = ContentValues().apply { put("is_active", 0) }
            writableDatabase.update(TABLE_MOTORS, inactive, null, null)

            val active = ContentValues().apply { put("is_active", 1) }
            writableDatabase.update(TABLE_MOTORS, active, "id = ?", arrayOf(id.toString()))
            writableDatabase.setTransactionSuccessful()
        } finally {
            writableDatabase.endTransaction()
        }
    }

    private fun getMotorCount(db: SQLiteDatabase): Int {
        db.rawQuery("SELECT COUNT(*) FROM $TABLE_MOTORS", null).use { cursor ->
            cursor.moveToFirst()
            return cursor.getInt(0)
        }
    }

    private fun android.database.Cursor.toMotor(): Motor {
        return Motor(
            id = getLong(getColumnIndexOrThrow("id")),
            name = getString(getColumnIndexOrThrow("name")),
            plateNumber = getString(getColumnIndexOrThrow("plate_number")),
            currentKilometer = getInt(getColumnIndexOrThrow("current_kilometer")),
            isActive = getInt(getColumnIndexOrThrow("is_active")) == 1
        )
    }

    private fun Servis.toValues(): ContentValues {
        return ContentValues().apply {
            put("motor_id", motorId)
            put("service_date", serviceDate)
            put("service_type", serviceType)
            put("kilometer", kilometer)
            put("interval_km", intervalKm)
            put("interval_month", intervalMonth)
            put("cost", cost)
            put("note", note)
        }
    }

    private fun Oli.toValues(): ContentValues {
        return ContentValues().apply {
            put("motor_id", motorId)
            put("oil_change_date", oilChangeDate)
            put("kilometer", kilometer)
            put("next_kilometer", nextKilometer)
            put("interval_km", intervalKm)
            put("interval_month", intervalMonth)
            put("oil_type", oilType)
            put("cost", cost)
        }
    }

    private fun Bensin.toValues(): ContentValues {
        return ContentValues().apply {
            put("motor_id", motorId)
            put("fuel_date", fuelDate)
            put("fuel_type", fuelType)
            put("fuel_brand", fuelBrand)
            put("octane", octane)
            put("price_per_liter", pricePerLiter)
            put("liter", liter)
            put("cost", cost)
            put("kilometer", kilometer)
        }
    }

    private fun Cursor.toServis(): Servis {
        return Servis(
            id = getLong(getColumnIndexOrThrow("id")),
            motorId = getLong(getColumnIndexOrThrow("motor_id")),
            serviceDate = getString(getColumnIndexOrThrow("service_date")),
            serviceType = getString(getColumnIndexOrThrow("service_type")),
            kilometer = getInt(getColumnIndexOrThrow("kilometer")),
            intervalKm = getInt(getColumnIndexOrThrow("interval_km")),
            intervalMonth = getInt(getColumnIndexOrThrow("interval_month")),
            cost = getInt(getColumnIndexOrThrow("cost")),
            note = getString(getColumnIndexOrThrow("note")).orEmpty()
        )
    }

    private fun Cursor.toOli(): Oli {
        return Oli(
            id = getLong(getColumnIndexOrThrow("id")),
            motorId = getLong(getColumnIndexOrThrow("motor_id")),
            oilChangeDate = getString(getColumnIndexOrThrow("oil_change_date")),
            kilometer = getInt(getColumnIndexOrThrow("kilometer")),
            nextKilometer = getInt(getColumnIndexOrThrow("next_kilometer")),
            intervalKm = getInt(getColumnIndexOrThrow("interval_km")),
            intervalMonth = getInt(getColumnIndexOrThrow("interval_month")),
            oilType = getString(getColumnIndexOrThrow("oil_type")),
            cost = getInt(getColumnIndexOrThrow("cost"))
        )
    }

    private fun Cursor.toBensin(): Bensin {
        return Bensin(
            id = getLong(getColumnIndexOrThrow("id")),
            motorId = getLong(getColumnIndexOrThrow("motor_id")),
            fuelDate = getString(getColumnIndexOrThrow("fuel_date")),
            fuelType = getString(getColumnIndexOrThrow("fuel_type")),
            fuelBrand = getString(getColumnIndexOrThrow("fuel_brand")),
            octane = getString(getColumnIndexOrThrow("octane")),
            pricePerLiter = getInt(getColumnIndexOrThrow("price_per_liter")),
            liter = getDouble(getColumnIndexOrThrow("liter")),
            cost = getInt(getColumnIndexOrThrow("cost")),
            kilometer = getInt(getColumnIndexOrThrow("kilometer"))
        )
    }

    private fun sumCost(table: String): Int {
        readableDatabase.rawQuery("SELECT COALESCE(SUM(cost), 0) FROM $table", null).use { cursor ->
            cursor.moveToFirst()
            return cursor.getInt(0)
        }
    }

    companion object {
        const val DATABASE_NAME = "motocare.db"
        const val DATABASE_VERSION = 1

        const val TABLE_USERS = "users"
        const val TABLE_MOTORS = "motors"
        const val TABLE_SERVICE_RECORDS = "service_records"
        const val TABLE_OIL_RECORDS = "oil_records"
        const val TABLE_FUEL_RECORDS = "fuel_records"
        const val TABLE_TAX_RECORDS = "tax_records"

        val CREATE_TABLES = listOf(
            """
            CREATE TABLE $TABLE_USERS (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                display_name TEXT NOT NULL,
                email TEXT NOT NULL
            )
            """.trimIndent(),
            """
            CREATE TABLE $TABLE_MOTORS (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                plate_number TEXT NOT NULL,
                current_kilometer INTEGER NOT NULL,
                is_active INTEGER NOT NULL DEFAULT 0
            )
            """.trimIndent(),
            """
            CREATE TABLE $TABLE_SERVICE_RECORDS (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                motor_id INTEGER NOT NULL,
                service_date TEXT NOT NULL,
                service_type TEXT NOT NULL,
                kilometer INTEGER NOT NULL,
                interval_km INTEGER NOT NULL,
                interval_month INTEGER NOT NULL,
                cost INTEGER NOT NULL,
                note TEXT,
                FOREIGN KEY(motor_id) REFERENCES $TABLE_MOTORS(id) ON DELETE CASCADE
            )
            """.trimIndent(),
            """
            CREATE TABLE $TABLE_OIL_RECORDS (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                motor_id INTEGER NOT NULL,
                oil_change_date TEXT NOT NULL,
                kilometer INTEGER NOT NULL,
                next_kilometer INTEGER NOT NULL,
                interval_km INTEGER NOT NULL,
                interval_month INTEGER NOT NULL,
                oil_type TEXT NOT NULL,
                cost INTEGER NOT NULL,
                FOREIGN KEY(motor_id) REFERENCES $TABLE_MOTORS(id) ON DELETE CASCADE
            )
            """.trimIndent(),
            """
            CREATE TABLE $TABLE_FUEL_RECORDS (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                motor_id INTEGER NOT NULL,
                fuel_date TEXT NOT NULL,
                fuel_type TEXT NOT NULL,
                fuel_brand TEXT NOT NULL,
                octane TEXT NOT NULL,
                price_per_liter INTEGER NOT NULL,
                liter REAL NOT NULL,
                cost INTEGER NOT NULL,
                kilometer INTEGER NOT NULL,
                FOREIGN KEY(motor_id) REFERENCES $TABLE_MOTORS(id) ON DELETE CASCADE
            )
            """.trimIndent(),
            """
            CREATE TABLE $TABLE_TAX_RECORDS (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                motor_id INTEGER NOT NULL,
                due_date TEXT NOT NULL,
                cost INTEGER NOT NULL,
                status TEXT NOT NULL,
                FOREIGN KEY(motor_id) REFERENCES $TABLE_MOTORS(id) ON DELETE CASCADE
            )
            """.trimIndent()
        )

        val DROP_TABLES = listOf(
            TABLE_TAX_RECORDS,
            TABLE_FUEL_RECORDS,
            TABLE_OIL_RECORDS,
            TABLE_SERVICE_RECORDS,
            TABLE_MOTORS,
            TABLE_USERS
        ).map { tableName -> "DROP TABLE IF EXISTS $tableName" }
    }
}
