package com.example.motocare.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.google.firebase.auth.FirebaseAuth
import org.json.JSONArray
import org.json.JSONObject

class MotoCareDbHelper(context: Context) : SQLiteOpenHelper(
    context,
    databaseName(),
    null,
    DATABASE_VERSION
) {
    override fun onCreate(db: SQLiteDatabase) {
        CREATE_TABLES.forEach(db::execSQL)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE $TABLE_TAX_RECORDS ADD COLUMN tax_type TEXT NOT NULL DEFAULT 'STNK tahunan'")
            return
        }
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

    fun getRecordCount(): Int {
        return countRows(TABLE_SERVICE_RECORDS) +
            countRows(TABLE_OIL_RECORDS) +
            countRows(TABLE_FUEL_RECORDS) +
            countRows(TABLE_TAX_RECORDS)
    }

    fun getFuelMonthlyTotal(): Int = sumCost(TABLE_FUEL_RECORDS)

    fun getTaxMonthlyTotal(): Int = sumCost(TABLE_TAX_RECORDS)

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

    fun insertPajak(pajak: Pajak): Long {
        return writableDatabase.insert(TABLE_TAX_RECORDS, null, pajak.toValues())
    }

    fun updatePajak(pajak: Pajak): Int {
        return writableDatabase.update(
            TABLE_TAX_RECORDS,
            pajak.toValues(),
            "id = ?",
            arrayOf(pajak.id.toString())
        )
    }

    fun deletePajak(id: Long): Int {
        return writableDatabase.delete(TABLE_TAX_RECORDS, "id = ?", arrayOf(id.toString()))
    }

    fun getPajak(id: Long): Pajak? {
        readableDatabase.query(
            TABLE_TAX_RECORDS,
            null,
            "id = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        ).use { cursor ->
            return if (cursor.moveToFirst()) cursor.toPajak() else null
        }
    }

    fun getPajakByMotor(motorId: Long): List<Pajak> {
        val items = mutableListOf<Pajak>()
        readableDatabase.query(
            TABLE_TAX_RECORDS,
            null,
            "motor_id = ?",
            arrayOf(motorId.toString()),
            null,
            null,
            "due_date ASC, id DESC"
        ).use { cursor ->
            while (cursor.moveToNext()) items.add(cursor.toPajak())
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

    fun resetAllData() {
        writableDatabase.beginTransaction()
        try {
            DROP_TABLES.forEach(writableDatabase::execSQL)
            CREATE_TABLES.forEach(writableDatabase::execSQL)
            writableDatabase.setTransactionSuccessful()
        } finally {
            writableDatabase.endTransaction()
        }
    }

    fun exportJson(): JSONObject {
        return JSONObject().apply {
            put("motors", tableToJson(TABLE_MOTORS))
            put("service_records", tableToJson(TABLE_SERVICE_RECORDS))
            put("oil_records", tableToJson(TABLE_OIL_RECORDS))
            put("fuel_records", tableToJson(TABLE_FUEL_RECORDS))
            put("tax_records", tableToJson(TABLE_TAX_RECORDS))
        }
    }

    fun importJson(root: JSONObject) {
        writableDatabase.beginTransaction()
        try {
            listOf(TABLE_TAX_RECORDS, TABLE_FUEL_RECORDS, TABLE_OIL_RECORDS, TABLE_SERVICE_RECORDS, TABLE_MOTORS)
                .forEach { writableDatabase.delete(it, null, null) }
            importTable(TABLE_MOTORS, root.optJSONArray("motors") ?: JSONArray())
            importTable(TABLE_SERVICE_RECORDS, root.optJSONArray("service_records") ?: JSONArray())
            importTable(TABLE_OIL_RECORDS, root.optJSONArray("oil_records") ?: JSONArray())
            importTable(TABLE_FUEL_RECORDS, root.optJSONArray("fuel_records") ?: JSONArray())
            importTable(TABLE_TAX_RECORDS, root.optJSONArray("tax_records") ?: JSONArray())
            writableDatabase.setTransactionSuccessful()
        } finally {
            writableDatabase.endTransaction()
        }
    }

    private fun tableToJson(table: String): JSONArray {
        val rows = JSONArray()
        readableDatabase.query(table, null, null, null, null, null, null).use { cursor ->
            while (cursor.moveToNext()) {
                val row = JSONObject()
                cursor.columnNames.forEach { column ->
                    val index = cursor.getColumnIndexOrThrow(column)
                    when (cursor.getType(index)) {
                        Cursor.FIELD_TYPE_INTEGER -> row.put(column, cursor.getLong(index))
                        Cursor.FIELD_TYPE_FLOAT -> row.put(column, cursor.getDouble(index))
                        Cursor.FIELD_TYPE_NULL -> row.put(column, JSONObject.NULL)
                        else -> row.put(column, cursor.getString(index))
                    }
                }
                rows.put(row)
            }
        }
        return rows
    }

    private fun importTable(table: String, rows: JSONArray) {
        repeat(rows.length()) { index ->
            val row = rows.getJSONObject(index)
            val values = ContentValues()
            row.keys().forEach { key ->
                when (val value = row.get(key)) {
                    JSONObject.NULL -> values.putNull(key)
                    is Int -> values.put(key, value)
                    is Long -> values.put(key, value)
                    is Double -> values.put(key, value)
                    else -> values.put(key, value.toString())
                }
            }
            writableDatabase.insert(table, null, values)
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

    private fun Pajak.toValues(): ContentValues {
        return ContentValues().apply {
            put("motor_id", motorId)
            put("tax_type", taxType)
            put("due_date", dueDate)
            put("cost", cost)
            put("status", status)
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

    private fun Cursor.toPajak(): Pajak {
        return Pajak(
            id = getLong(getColumnIndexOrThrow("id")),
            motorId = getLong(getColumnIndexOrThrow("motor_id")),
            taxType = getString(getColumnIndexOrThrow("tax_type")),
            dueDate = getString(getColumnIndexOrThrow("due_date")),
            cost = getInt(getColumnIndexOrThrow("cost")),
            status = getString(getColumnIndexOrThrow("status"))
        )
    }

    private fun sumCost(table: String): Int {
        readableDatabase.rawQuery("SELECT COALESCE(SUM(cost), 0) FROM $table", null).use { cursor ->
            cursor.moveToFirst()
            return cursor.getInt(0)
        }
    }

    private fun countRows(table: String): Int {
        readableDatabase.rawQuery("SELECT COUNT(*) FROM $table", null).use { cursor ->
            cursor.moveToFirst()
            return cursor.getInt(0)
        }
    }

    companion object {
        const val DATABASE_NAME = "motocare.db"
        const val DATABASE_VERSION = 2

        private fun databaseName(): String {
            val user = FirebaseAuth.getInstance().currentUser
            val accountKey = user?.uid ?: user?.email ?: "local"
            val safeKey = accountKey.replace(Regex("[^A-Za-z0-9_]"), "_")
            return "motocare_$safeKey.db"
        }

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
                tax_type TEXT NOT NULL,
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
