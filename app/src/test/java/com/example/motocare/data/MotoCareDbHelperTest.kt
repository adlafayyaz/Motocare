package com.example.motocare.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MotoCareDbHelperTest {
    @Test
    fun databaseNameAndVersionAreStable() {
        assertEquals("motocare.db", MotoCareDbHelper.DATABASE_NAME)
        assertEquals(2, MotoCareDbHelper.DATABASE_VERSION)
    }

    @Test
    fun createTablesContainsAllRequiredTables() {
        val schema = MotoCareDbHelper.CREATE_TABLES.joinToString("\n")

        listOf(
            MotoCareDbHelper.TABLE_USERS,
            MotoCareDbHelper.TABLE_MOTORS,
            MotoCareDbHelper.TABLE_SERVICE_RECORDS,
            MotoCareDbHelper.TABLE_OIL_RECORDS,
            MotoCareDbHelper.TABLE_FUEL_RECORDS,
            MotoCareDbHelper.TABLE_TAX_RECORDS
        ).forEach { tableName ->
            assertTrue(schema.contains("CREATE TABLE $tableName"))
        }
    }
}
