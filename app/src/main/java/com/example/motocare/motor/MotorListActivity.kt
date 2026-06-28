package com.example.motocare.motor

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.motocare.R
import com.example.motocare.data.MotoCareDbHelper
import com.example.motocare.navigation.BottomNavBinder

class MotorListActivity : AppCompatActivity() {
    private lateinit var dbHelper: MotoCareDbHelper
    private lateinit var adapter: MotorAdapter
    private lateinit var sectionText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_motor_list)

        dbHelper = MotoCareDbHelper(this)
        sectionText = findViewById(R.id.textEmptyMotor)
        adapter = MotorAdapter { motor ->
            val intent = Intent(this, MotorDetailActivity::class.java)
            intent.putExtra(MotorDetailActivity.EXTRA_MOTOR_ID, motor.id)
            startActivity(intent)
        }

        findViewById<RecyclerView>(R.id.recyclerMotors).apply {
            layoutManager = LinearLayoutManager(this@MotorListActivity)
            adapter = this@MotorListActivity.adapter
        }

        findViewById<View>(R.id.buttonAddMotor).setOnClickListener {
            startActivity(Intent(this, MotorFormActivity::class.java))
            overridePendingTransition(0, 0)
        }
        BottomNavBinder.bind(this, BottomNavBinder.MENU_MOTOR)
    }

    override fun onResume() {
        super.onResume()
        val motors = dbHelper.getAllMotors()
        adapter.submitList(motors)
        sectionText.visibility = View.VISIBLE
    }
}
