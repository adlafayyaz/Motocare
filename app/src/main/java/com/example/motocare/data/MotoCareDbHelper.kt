package com.example.motocare.data

import android.content.Context
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
