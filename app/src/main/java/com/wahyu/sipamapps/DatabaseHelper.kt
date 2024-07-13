package com.wahyu.sipamapps

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "riwayatPembayaran.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "Riwayat"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TANGGAL = "tanggal"
        private const val COLUMN_JUMLAH = "jumlah"
        private const val COLUMN_STATUS = "status"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_TANGGAL TEXT, $COLUMN_JUMLAH TEXT, $COLUMN_STATUS TEXT)"
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addRiwayat(tanggal: String, jumlah: String, status: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TANGGAL, tanggal)
            put(COLUMN_JUMLAH, jumlah)
            put(COLUMN_STATUS, status)
        }
        val result = db.insert(TABLE_NAME, null, values)
        db.close()
        return result
    }

    @SuppressLint("Range")
    fun getAllRiwayat(): List<RiwayatPembayaran> {
        val riwayatList = mutableListOf<RiwayatPembayaran>()
        val selectQuery = "SELECT * FROM $TABLE_NAME"
        val db = readableDatabase
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(selectQuery, null)
            cursor?.let {
                while (it.moveToNext()) {
                    val riwayat = RiwayatPembayaran(
                        it.getString(it.getColumnIndex(COLUMN_TANGGAL)),
                        it.getString(it.getColumnIndex(COLUMN_JUMLAH)),
                        it.getString(it.getColumnIndex(COLUMN_STATUS))
                    )
                    riwayatList.add(riwayat)
                }
            }
        } finally {
            cursor?.close()
            db.close()
        }

        return riwayatList
    }

    fun clearAllRiwayat() {
        val db = writableDatabase
        db.delete(TABLE_NAME, null, null)
        db.close()
    }
}

data class RiwayatPembayaran(val tanggal: String, val jumlah: String, val status: String)
