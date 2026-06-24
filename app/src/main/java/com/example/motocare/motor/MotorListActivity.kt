package com.example.motocare.motor

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.motocare.R
import com.example.motocare.data.MotoCareDbHelper

class MotorListActivity : AppCompatActivity() {
    private lateinit var dbHelper: MotoCareDbHelper
    private lateinit var adapter: MotorAdapter
    private lateinit var emptyText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_motor_list)

        dbHelper = MotoCareDbHelper(this)
        emptyText = findViewById(R.id.textEmptyMotor)
        adapter = MotorAdapter { motor ->
            val intent = Intent(this, MotorDetailActivity::class.java)
            intent.putExtra(MotorDetailActivity.EXTRA_MOTOR_ID, motor.id)
            startActivity(intent)
        }

        findViewById<RecyclerView>(R.id.recyclerMotors).apply {
            layoutManager = LinearLayoutManager(this@MotorListActivity)
            adapter = this@MotorListActivity.adapter
        }

        findViewById<Button>(R.id.buttonAddMotor).setOnClickListener {
            startActivity(Intent(this, MotorFormActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        val motors = dbHelper.getAllMotors()
        adapter.submitList(motors)
        emptyText.visibility = if (motors.isEmpty()) View.VISIBLE else View.GONE
    }
}
