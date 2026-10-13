package com.example.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.db.StudentDatabaseHelper
import com.example.myapplication.model.Student

class StudentFormActivity : AppCompatActivity() {

    private lateinit var dbHelper: StudentDatabaseHelper
    private var editingStudent: Student? = null

    private lateinit var etStudentCode: EditText
    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etAvatar: EditText
    private lateinit var btnSave: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_student_form)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dbHelper = StudentDatabaseHelper(this)

        etStudentCode = findViewById(R.id.etStudentCode)
        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        etAvatar = findViewById(R.id.etAvatar)
        btnSave = findViewById(R.id.btnSave)

        @Suppress("DEPRECATION")
        editingStudent = intent.getSerializableExtra("EXTRA_STUDENT") as? Student

        if (editingStudent != null) {
            title = getString(R.string.title_edit_student)
            etStudentCode.setText(editingStudent?.studentCode)
            etName.setText(editingStudent?.name)
            etEmail.setText(editingStudent?.email)
            etAvatar.setText(editingStudent?.avatar)
        } else {
            title = getString(R.string.title_add_student)
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        btnSave.setOnClickListener {
            validateAndConfirmSave()
        }
    }

    private fun validateAndConfirmSave() {
        val code = etStudentCode.text.toString().trim()
        val name = etName.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val avatar = etAvatar.text.toString().trim()

        if (code.isEmpty() || name.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, R.string.msg_empty_fields, Toast.LENGTH_SHORT).show()
            return
        }

        val messageRes = if (editingStudent != null) {
            R.string.dialog_confirm_update
        } else {
            R.string.dialog_confirm_add
        }

        AlertDialog.Builder(this)
            .setTitle(R.string.dialog_confirm_title)
            .setMessage(messageRes)
            .setPositiveButton(R.string.yes) { _, _ ->
                if (editingStudent != null) {
                    val updatedStudent = Student(
                        id = editingStudent!!.id,
                        studentCode = code,
                        name = name,
                        email = email,
                        avatar = avatar
                    )
                    dbHelper.updateStudent(updatedStudent)
                } else {
                    val newStudent = Student(
                        studentCode = code,
                        name = name,
                        email = email,
                        avatar = avatar
                    )
                    dbHelper.insertStudent(newStudent)
                }
                finish()
            }
            .setNegativeButton(R.string.no, null)
            .show()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
