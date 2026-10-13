package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.db.StudentDatabaseHelper
import com.example.myapplication.model.Student

class StudentDetailActivity : AppCompatActivity() {

    private lateinit var dbHelper: StudentDatabaseHelper
    private lateinit var student: Student

    private lateinit var tvName: TextView
    private lateinit var tvCode: TextView
    private lateinit var tvEmail: TextView
    private lateinit var btnEdit: Button
    private lateinit var btnDelete: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_student_detail)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        title = getString(R.string.title_student_detail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        dbHelper = StudentDatabaseHelper(this)

        @Suppress("DEPRECATION")
        student = intent.getSerializableExtra("EXTRA_STUDENT") as? Student ?: finish().let { return }

        tvName = findViewById(R.id.tvDetailName)
        tvCode = findViewById(R.id.tvDetailCode)
        tvEmail = findViewById(R.id.tvDetailEmail)
        btnEdit = findViewById(R.id.btnEdit)
        btnDelete = findViewById(R.id.btnDelete)

        displayStudentInfo()

        btnEdit.setOnClickListener {
            val intent = Intent(this, StudentFormActivity::class.java).apply {
                putExtra("EXTRA_STUDENT", student)
            }
            startActivity(intent)
        }

        btnDelete.setOnClickListener {
            showDeleteConfirmationDialog()
        }
    }

    override fun onResume() {
        super.onResume()
        // Refresh student info if edited
        val students = dbHelper.getAllStudents()
        val updated = students.find { it.id == student.id }
        if (updated != null) {
            student = updated
            displayStudentInfo()
        } else {
            finish()
        }
    }

    private fun displayStudentInfo() {
        tvName.text = student.name
        tvCode.text = student.studentCode
        tvEmail.text = student.email
    }

    private fun showDeleteConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle(R.string.dialog_confirm_title)
            .setMessage(R.string.dialog_confirm_delete)
            .setPositiveButton(R.string.yes) { _, _ ->
                dbHelper.deleteStudent(student.id)
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
