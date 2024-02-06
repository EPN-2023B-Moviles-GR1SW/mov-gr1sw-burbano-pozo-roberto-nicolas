package com.example.examen

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.text.SimpleDateFormat
import kotlin.collections.ArrayList

class SQLHelperE(
    contexto: Context?,
) : SQLiteOpenHelper(
    contexto, "exam_db", null, 1
) {
    val TABLE_PLAYERS = "players"
    val COLUMN_ID = "id"
    val COLUMN_DEBUT_DATE = "debut_date"
    val COLUMN_IS_INJURED = "is_injured"
    val COLUMN_NAME = "name"
    val COLUMN_VALUE = "value"
    val COLUMN_TEAM = "team"
    val TABLE_TEAMS = "teams"
    val COLUMN_FOUNDATION_DATE = "foundation_date"
    val COLUMN_NET_INCOME = "net_income"
    val COLUMN_IS_ACTIVE = "is_active"

    val dateFormat = SimpleDateFormat("yyyy-MM-dd")

    override fun onCreate(db: SQLiteDatabase?) {
        Log.d("DB", "PLAYERS")
        val createSQL = """
            CREATE TABLE  IF NOT EXISTS  $TABLE_PLAYERS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_DEBUT_DATE TEXT,
                $COLUMN_IS_INJURED INTEGER,
                $COLUMN_NAME TEXT,
                $COLUMN_VALUE REAL,
                $COLUMN_TEAM INTEGER
            )
            """.trimIndent()
        val createTeams = """
            CREATE TABLE IF NOT EXISTS teams (
            $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $COLUMN_NAME TEXT,
            $COLUMN_FOUNDATION_DATE TEXT,
            $COLUMN_NET_INCOME REAL,
            $COLUMN_IS_ACTIVE INTEGER
            )
        """.trimIndent()
        db?.execSQL(createTeams)
        db?.execSQL(createSQL)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    fun createPlayer(
        debutDate: String, isInjured: Boolean, name: String, value: Float, team: Int
    ): Boolean {
        val writeDB = writableDatabase
        val values = ContentValues()
        values.put(COLUMN_DEBUT_DATE, debutDate)
        values.put(COLUMN_IS_INJURED, isInjured)
        values.put(COLUMN_NAME, name)
        values.put(COLUMN_VALUE, value)
        values.put(COLUMN_TEAM, team)
        val resultadoGuardar = writeDB.insert(
             TABLE_PLAYERS,null, values
        )
        writeDB.close()
        return resultadoGuardar.toInt() != -1
    }

    fun delete(id: Int, tableName: String): Boolean {
        val writeDB = writableDatabase
        val values = arrayOf(id.toString())
        val result = writeDB.delete(
            tableName, "id=?", values
        )
        writeDB.close()
        return result.toInt() != -1
    }

    fun updatePlayer(
        debutDate: String, isInjured: Boolean, name: String, value: Float, team: Int, id: Int
    ): Boolean {
        val writeDB = writableDatabase
        val values = ContentValues()
        values.put(COLUMN_DEBUT_DATE, debutDate)
        values.put(COLUMN_IS_INJURED, isInjured)
        values.put(COLUMN_NAME, name)
        values.put(COLUMN_VALUE, value)
        values.put(COLUMN_TEAM, team)
        val updateParameter = arrayOf(id.toString())
        val result = writeDB.update(
            TABLE_PLAYERS, values, "id=?", updateParameter
        )
        writeDB.close()
        return result != -1
    }

    fun retrievePlayer(id: Int): DPlayer? {
        val readDB = readableDatabase
        val sqlCommand = """
            SELECT * FROM $TABLE_PLAYERS WHERE ID = ?
        """.trimIndent()
        val retrieveParameter = arrayOf(id.toString())
        val result = readDB.rawQuery(
            sqlCommand,
            retrieveParameter,
        )
        val obj = result.moveToFirst()
        var instancedObj: DPlayer? = null;
        if (obj) {
            val debutDateStr = dateFormat.parse(result.getString(1))
            instancedObj = DPlayer(
                result.getInt(0),
                debutDateStr,
                result.getInt(2) == 1,
                result.getString(3),
                result.getFloat(4),
                result.getInt(5),
            )
        }
        result.close()
        readDB.close()
        return instancedObj
    }

    fun retrieveByTeam(id: Int): ArrayList<DPlayer>? {
        val readDB = readableDatabase
        val sqlCommand = """
            SELECT * FROM $TABLE_PLAYERS WHERE team = ?
        """.trimIndent()
        val retrieveParameter = arrayOf(id.toString())
        val result = readDB.rawQuery(
            sqlCommand,
            retrieveParameter,
        )
        val objs = arrayListOf<DPlayer>()
        while (result.moveToNext()) {
            println(result)
            val debutDateStr = dateFormat.parse(result.getString(1))
            val player = DPlayer(
                result.getInt(0),
                debutDateStr,
                result.getInt(2) == 1,
                result.getString(3),
                result.getFloat(4),
                result.getInt(5),
            )
            objs.add(player)
        }
        result.close()
        readDB.close()
        return objs
    }

    fun createTeam(
        name: String, foundationDate: String, netIncome: Float, isActive: Boolean,
    ): Boolean {
        val writeDB = writableDatabase
        val values = ContentValues()
        values.put(COLUMN_NAME, name)
        values.put(COLUMN_FOUNDATION_DATE, foundationDate)
        values.put(COLUMN_NET_INCOME, netIncome)
        values.put(COLUMN_IS_ACTIVE, isActive)
        val result = writeDB.insert(
            TABLE_TEAMS, null, values
        )
        writeDB.close()
        return result.toInt() != -1
    }


    fun updateTeam(
        name: String, foundationDate: String, netIncome: Float, isActive: Boolean, id: Int
    ): Boolean {
        val writeDB = writableDatabase
        val values = ContentValues()
        values.put(COLUMN_NAME, name)
        values.put(COLUMN_FOUNDATION_DATE, foundationDate)
        values.put(COLUMN_NET_INCOME, netIncome)
        values.put(COLUMN_IS_ACTIVE, isActive)
        val updateParameter = arrayOf(id.toString())
        val result = writeDB.update(
            TABLE_TEAMS, values, "id=?", updateParameter
        )
        writeDB.close()
        return result != -1
    }

    fun retrieveAllTeams(): ArrayList<DTeam> {
        val readDB = readableDatabase
        val sqlCommand = """
            SELECT * FROM $TABLE_TEAMS
        """.trimIndent()
        val result = readDB.rawQuery(
            sqlCommand,
            null,
        )
        val objs = arrayListOf<DTeam>()
        while (result.moveToNext()) {
            val debutDateStr = dateFormat.parse(result.getString(2))
            val obj = DTeam(
                result.getInt(0),
                result.getString(1),
                debutDateStr,
                result.getFloat(3),
                result.getInt(4) == 1,
            )
            objs.add(obj)
        }
        result.close()
        readDB.close()
        return objs
    }

    fun retrieveTeam(id: Int): DTeam? {
        val readDB = readableDatabase
        val sqlCommand = """
            SELECT * FROM $TABLE_TEAMS WHERE ID = ?
        """.trimIndent()
        val retrieveParameter = arrayOf(id.toString())
        val result = readDB.rawQuery(
            sqlCommand,
            retrieveParameter,
        )
        val obj = result.moveToFirst()
        var instancedObj: DTeam? = null;

        if (obj) {
            val debutDateStr = dateFormat.parse(result.getString(2))
            instancedObj = DTeam(
                result.getInt(0),
                result.getString(1),
                debutDateStr,
                result.getFloat(3),
                result.getInt(4) == 1,
            )
        }
        result.close()
        readDB.close()
        return instancedObj
    }

    fun getMaxId(table: String): Int {
        val readDB = readableDatabase
        val sqlCommand = """
            SELECT max(id) FROM $table
        """.trimIndent()
        val result = readDB.rawQuery(
            sqlCommand,
            null,
        )
        result.moveToFirst()
        return result.getInt(0)

    }

}


