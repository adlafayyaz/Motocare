package com.example.motocare

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.motocare.bensin.BensinFormActivity
import com.example.motocare.motor.MotorFormActivity
import com.example.motocare.oli.OliFormActivity
import com.example.motocare.pajak.PajakFormActivity
import com.example.motocare.servis.ServisFormActivity

class CatatActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catat)

        findViewById<Button>(R.id.buttonCatatMotor).setOnClickListener {
            startActivity(Intent(this, MotorFormActivity::class.java))
        }
        findViewById<Button>(R.id.buttonCatatServis).setOnClickListener {
            startActivity(Intent(this, ServisFormActivity::class.java))
        }
        findViewById<Button>(R.id.buttonCatatOli).setOnClickListener {
            startActivity(Intent(this, OliFormActivity::class.java))
        }
        findViewById<Button>(R.id.buttonCatatBensin).setOnClickListener {
            startActivity(Intent(this, BensinFormActivity::class.java))
        }
        findViewById<Button>(R.id.buttonCatatPajak).setOnClickListener {
            startActivity(Intent(this, PajakFormActivity::class.java))
        }
    }

    private fun showNextStepToast() {
        Toast.makeText(this, R.string.feature_next_step, Toast.LENGTH_SHORT).show()
    }
}
