package com.example.definitivofeedback

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues

class UserDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = "CREATE TABLE $TABLE_USERS (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_USERNAME TEXT NOT NULL, " +
                "$COLUMN_PASSWORD TEXT NOT NULL, " +
                "$COLUMN_DARK_MODE INTEGER NOT NULL DEFAULT 0)"
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        onCreate(db)
    }

    fun getAllUsers(): List<User> {
        val users = mutableListOf<User>()
        val db = readableDatabase
        val cursor = db.query(TABLE_USERS, null, null, null, null, null, null)
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
                val username = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERNAME))
                val password = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD))
                val darkMode = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_DARK_MODE)) == 1
                users.add(User(id, username, password, darkMode))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return users
    }

    fun updateDarkMode(isDarkMode: Boolean) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_DARK_MODE, if (isDarkMode) 1 else 0)
        }
        db.update(TABLE_USERS, values, null, null)
    }

    fun updateUserDarkMode(username: String, isDarkMode: Boolean) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_DARK_MODE, if (isDarkMode) 1 else 0)
        }
        db.update(TABLE_USERS, values, "$COLUMN_USERNAME = ?", arrayOf(username))
    }

    fun deleteUser(username: String, password: String): Boolean {
        val db = writableDatabase
        val cursor = db.query(
            TABLE_USERS,
            arrayOf(COLUMN_ID),
            "$COLUMN_USERNAME = ? AND $COLUMN_PASSWORD = ?",
            arrayOf(username, password),
            null, null, null
        )
        return if (cursor.moveToFirst()) {
            val userId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            cursor.close()
            db.delete(TABLE_USERS, "$COLUMN_ID = ?", arrayOf(userId.toString())) > 0
        } else {
            cursor.close()
            false
        }
    }

    companion object {
        private const val DATABASE_NAME = "users.db"
        private const val DATABASE_VERSION = 2
        const val TABLE_USERS = "users"
        const val COLUMN_ID = "id"
        const val COLUMN_USERNAME = "username"
        const val COLUMN_PASSWORD = "password"
        const val COLUMN_DARK_MODE = "dark_mode"
    }
}

data class User(val id: Int, val username: String, val password: String, val darkMode: Boolean)