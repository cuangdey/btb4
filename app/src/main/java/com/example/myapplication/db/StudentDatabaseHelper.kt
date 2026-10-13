package com.example.myapplication.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.myapplication.model.Student

class StudentDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "student_manager.db"
        private const val DATABASE_VERSION = 1

        private const val TABLE_STUDENTS = "students"
        private const val COLUMN_ID = "id"
        private const val COLUMN_STUDENT_CODE = "student_code"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_EMAIL = "email"
        private const val COLUMN_AVATAR = "avatar"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = ("CREATE TABLE $TABLE_STUDENTS ("
                + "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "$COLUMN_STUDENT_CODE TEXT, "
                + "$COLUMN_NAME TEXT, "
                + "$COLUMN_EMAIL TEXT, "
                + "$COLUMN_AVATAR TEXT)")
        db.execSQL(createTableQuery)

        // Insert sample data
        val sampleStudents = listOf(
            Student(-1L, "SV001", "Nguyễn Văn A", "nva@gmail.com", "https://i.imgur.com/71916X8.png"),
            Student(-1L, "SV002", "Trần Thị B", "ttb@gmail.com", "https://i.imgur.com/71916X8.png"),
            Student(-1L, "SV003", "Lê Văn C", "lvc@gmail.com", "https://i.imgur.com/71916X8.png")
        )
        for (s in sampleStudents) {
            val values = ContentValues().apply {
                put(COLUMN_STUDENT_CODE, s.studentCode)
                put(COLUMN_NAME, s.name)
                put(COLUMN_EMAIL, s.email)
                put(COLUMN_AVATAR, s.avatar)
            }
            db.insert(TABLE_STUDENTS, null, values)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_STUDENTS")
        onCreate(db)
    }

    fun insertStudent(student: Student): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_STUDENT_CODE, student.studentCode)
            put(COLUMN_NAME, student.name)
            put(COLUMN_EMAIL, student.email)
            put(COLUMN_AVATAR, student.avatar)
        }
        val id = db.insert(TABLE_STUDENTS, null, values)
        db.close()
        return id
    }

    fun getAllStudents(): List<Student> {
        val studentList = mutableListOf<Student>()
        val selectQuery = "SELECT * FROM $TABLE_STUDENTS ORDER BY $COLUMN_ID DESC"
        val db = readableDatabase
        val cursor: Cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            val idIndex = cursor.getColumnIndex(COLUMN_ID)
            val codeIndex = cursor.getColumnIndex(COLUMN_STUDENT_CODE)
            val nameIndex = cursor.getColumnIndex(COLUMN_NAME)
            val emailIndex = cursor.getColumnIndex(COLUMN_EMAIL)
            val avatarIndex = cursor.getColumnIndex(COLUMN_AVATAR)

            do {
                val id = cursor.getLong(idIndex)
                val code = cursor.getString(codeIndex)
                val name = cursor.getString(nameIndex)
                val email = cursor.getString(emailIndex)
                val avatar = cursor.getString(avatarIndex)
                studentList.add(Student(id, code, name, email, avatar))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return studentList
    }

    fun updateStudent(student: Student): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_STUDENT_CODE, student.studentCode)
            put(COLUMN_NAME, student.name)
            put(COLUMN_EMAIL, student.email)
            put(COLUMN_AVATAR, student.avatar)
        }
        val rowsAffected = db.update(TABLE_STUDENTS, values, "$COLUMN_ID = ?", arrayOf(student.id.toString()))
        db.close()
        return rowsAffected
    }

    fun deleteStudent(id: Long): Int {
        val db = writableDatabase
        val rowsAffected = db.delete(TABLE_STUDENTS, "$COLUMN_ID = ?", arrayOf(id.toString()))
        db.close()
        return rowsAffected
    }
}
