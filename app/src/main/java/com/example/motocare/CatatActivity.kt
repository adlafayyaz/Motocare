package com.example.motocare

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.motocare.motor.MotorFormActivity

class CatatActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catat)

        findViewById<Button>(R.id.buttonCatatMotor).setOnClickListener {
            startActivity(Intent(this, MotorFormActivity::class.java))
        }
        findViewById<Button>(R.id.buttonCatatServis).setOnClickListener { showNextStepToast() }
        findViewById<Button>(R.id.buttonCatatOli).setOnClickListener { showNextStepToast() }
        findViewById<Button>(R.id.buttonCatatBensin).setOnClickListener { showNextStepToast() }
        findViewById<Button>(R.id.buttonCatatPajak).setOnClickListener { showNextStepToast() }
    }

    private fun showNextStepToast() {
        Toast.makeText(this, R.string.feature_next_step, Toast.LENGTH_SHORT).show()
    }
}
