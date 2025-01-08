package com.wahyu.sipamapps

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "sipamapps.db"
        private const val DATABASE_VERSION = 1

        // Tabel Riwayat Pembayaran
        private const val TABLE_RIWAYAT = "Riwayat"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TANGGAL = "tanggal"
        private const val COLUMN_JUMLAH = "jumlah"
        private const val COLUMN_STATUS = "status"

        // Tabel Pesanan Baru
        private const val TABLE_ORDER = "OrderTable"
        private const val COLUMN_ORDER_ID = "order_id"
        private const val COLUMN_ORDER_NAME = "order_name"
        private const val COLUMN_ORDER_LATITUDE = "latitude"
        private const val COLUMN_ORDER_LONGITUDE = "longitude"
        private const val COLUMN_ORDER_CLASS = "order_class"
        private const val COLUMN_ORDER_ADDRESS = "order_address"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createRiwayatTable = """
            CREATE TABLE $TABLE_RIWAYAT (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, 
                $COLUMN_TANGGAL TEXT, 
                $COLUMN_JUMLAH TEXT, 
                $COLUMN_STATUS TEXT)
        """

        val createOrderTable = """
            CREATE TABLE $TABLE_ORDER (
                $COLUMN_ORDER_ID INTEGER PRIMARY KEY AUTOINCREMENT, 
                $COLUMN_ORDER_NAME TEXT, 
                $COLUMN_ORDER_LATITUDE TEXT, 
                $COLUMN_ORDER_LONGITUDE TEXT, 
                $COLUMN_ORDER_CLASS TEXT, 
                $COLUMN_ORDER_ADDRESS TEXT)
        """

        db.execSQL(createRiwayatTable)
        db.execSQL(createOrderTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_RIWAYAT")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ORDER")
        onCreate(db)
    }

    fun addRiwayat(tanggal: String, jumlah: String, status: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TANGGAL, tanggal)
            put(COLUMN_JUMLAH, jumlah)
            put(COLUMN_STATUS, status)
        }
        val result = db.insert(TABLE_RIWAYAT, null, values)
        db.close()
        return result
    }

    @SuppressLint("Range")
    fun getAllRiwayat(): List<RiwayatPembayaran> {
        val riwayatList = mutableListOf<RiwayatPembayaran>()
        val selectQuery = "SELECT * FROM $TABLE_RIWAYAT"
        val db = readableDatabase
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(selectQuery, null)
            cursor?.let {
                while (it.moveToNext()) {
                    val riwayat = RiwayatPembayaran(
                        it.getString(it.getColumnIndexOrThrow(COLUMN_TANGGAL)),
                        it.getString(it.getColumnIndexOrThrow(COLUMN_JUMLAH)),
                        it.getString(it.getColumnIndexOrThrow(COLUMN_STATUS))
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

    fun addOrder(name: String, latitude: String, longitude: String, orderClass: String, address: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_ORDER_NAME, name)
            put(COLUMN_ORDER_LATITUDE, latitude)
            put(COLUMN_ORDER_LONGITUDE, longitude)
            put(COLUMN_ORDER_CLASS, orderClass)
            put(COLUMN_ORDER_ADDRESS, address)
        }
        val result = db.insert(TABLE_ORDER, null, values)
        db.close()
        return result
    }

    @SuppressLint("Range")
    fun getAllOrders(): List<Order> {
        val orderList = mutableListOf<Order>()
        val selectQuery = "SELECT * FROM $TABLE_ORDER"
        val db = readableDatabase
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(selectQuery, null)
            cursor?.let {
                while (it.moveToNext()) {
                    val order = Order(
                        it.getInt(it.getColumnIndexOrThrow(COLUMN_ORDER_ID)),
                        it.getString(it.getColumnIndexOrThrow(COLUMN_ORDER_NAME)),
                        it.getString(it.getColumnIndexOrThrow(COLUMN_ORDER_LATITUDE)),
                        it.getString(it.getColumnIndexOrThrow(COLUMN_ORDER_LONGITUDE)),
                        it.getString(it.getColumnIndexOrThrow(COLUMN_ORDER_CLASS)),
                        it.getString(it.getColumnIndexOrThrow(COLUMN_ORDER_ADDRESS))
                    )
                    orderList.add(order)
                }
            }
        } finally {
            cursor?.close()
            db.close()
        }

        return orderList
    }

    fun clearAllRiwayat() {
        TODO("Not yet implemented")
    }
}

data class RiwayatPembayaran(val tanggal: String, val jumlah: String, val status: String)

data class Order(
    val id: Int,
    val nama: String,
    val latitude: String,
    val longitude: String,
    val kelas: String,
    val alamat: String
) {
    // Tidak perlu properti tambahan di sini karena sudah diakomodasi oleh konstruktor
}
