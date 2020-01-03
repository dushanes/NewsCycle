package com.newscycle.database

import android.provider.BaseColumns

object AccountContract{

    object AccountInfo : BaseColumns {
        val TABLE_NAME: String = "accounts"
        val COLUMN_EMAIL: String = "email"
        val COLUMN_PASS: String = "password"
        val COLUMN_ID: String = "id"
    }

    private val sqlCreateEntry =
        "CREATE TABLE " + AccountInfo.TABLE_NAME + " (" +
                AccountInfo.COLUMN_ID + " INTEGER PRIMARY KEY," +
                AccountInfo.COLUMN_EMAIL + " TEXT," +
                AccountInfo.COLUMN_PASS + " TEXT)"

    private val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${AccountInfo.TABLE_NAME}"

    fun createEntries() : String { return sqlCreateEntry}

    fun deleteEntries() : String { return sqlCreateEntry}
}