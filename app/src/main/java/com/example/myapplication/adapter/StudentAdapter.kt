package com.example.myapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.model.Student

class StudentAdapter(
    private var studentList: List<Student>,
    private val onItemClick: (Student) -> Unit
) : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivAvatar: ImageView = itemView.findViewById(R.id.ivAvatar)
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvStudentCode: TextView = itemView.findViewById(R.id.tvStudentCode)
        val tvEmail: TextView = itemView.findViewById(R.id.tvEmail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_student, parent, false)
        return StudentViewHolder(view)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student = studentList[position]
        holder.tvName.text = student.name
        holder.tvStudentCode.text = student.studentCode
        holder.tvEmail.text = student.email
        
        // For avatar, we can set default or try loading if it's a valid uri/url. If empty, keep default.
        if (student.avatar.isNotBlank()) {
            // If it's a URL or path, we can use simple placeholder or android built-in.
            // Since we don't have Glide/Picasso added by default, we can set default icon or handle image uri if needed.
        }

        holder.itemView.setOnClickListener {
            onItemClick(student)
        }
    }

    override fun getItemCount(): Int = studentList.size

    fun updateData(newList: List<Student>) {
        studentList = newList
        notifyDataSetChanged()
    }
}
