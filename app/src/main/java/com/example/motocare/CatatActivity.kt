package com.example.motocare

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.motocare.bensin.BensinFormActivity
import com.example.motocare.data.MotoCareDbHelper
import com.example.motocare.motor.MotorFormActivity
import com.example.motocare.navigation.BottomNavBinder
import com.example.motocare.oli.OliFormActivity
import com.example.motocare.pajak.PajakFormActivity
import com.example.motocare.servis.ServisFormActivity

class CatatActivity : AppCompatActivity() {
    private lateinit var dbHelper: MotoCareDbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catat)
        dbHelper = MotoCareDbHelper(this)

        findViewById<View>(R.id.buttonCatatMotor).setOnClickListener {
            openNoAnim(MotorFormActivity::class.java)
        }
        findViewById<View>(R.id.buttonCatatServis).setOnClickListener {
            openNoAnim(ServisFormActivity::class.java)
        }
        findViewById<View>(R.id.buttonCatatOli).setOnClickListener {
            openNoAnim(OliFormActivity::class.java)
        }
        findViewById<View>(R.id.buttonCatatBensin).setOnClickListener {
            openNoAnim(BensinFormActivity::class.java)
        }
        findViewById<View>(R.id.buttonCatatPajak).setOnClickListener {
            openNoAnim(PajakFormActivity::class.java)
        }
        BottomNavBinder.bind(this, BottomNavBinder.MENU_CATAT)
    }

    override fun onResume() {
        super.onResume()
        val activeMotor = dbHelper.getActiveMotor()
        findViewById<TextView>(R.id.textCatatMotorName).text = activeMotor?.name
            ?: getString(R.string.no_active_motor)
        findViewById<TextView>(R.id.textCatatMotorPlate).text = activeMotor?.plateNumber
            ?: getString(R.string.add_motor_first)
    }

    private fun openNoAnim(target: Class<out AppCompatActivity>) {
        startActivity(Intent(this, target))
        overridePendingTransition(0, 0)
    }
}
