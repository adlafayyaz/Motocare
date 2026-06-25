package com.example.motocare.navigation

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.TextView
import com.example.motocare.R
import com.example.motocare.bensin.BensinFormActivity
import com.example.motocare.data.MotoCareDbHelper
import com.example.motocare.motor.MotorFormActivity
import com.example.motocare.oli.OliFormActivity
import com.example.motocare.pajak.PajakFormActivity
import com.example.motocare.servis.ServisFormActivity
import com.google.android.material.bottomsheet.BottomSheetDialog

object CatatSheet {
    fun show(activity: Activity) {
        val dialog = BottomSheetDialog(activity)
        val view = activity.layoutInflater.inflate(R.layout.sheet_catat, null)
        val db = MotoCareDbHelper(activity)
        val activeMotor = db.getActiveMotor()

        view.findViewById<TextView>(R.id.textSheetMotorName).text = activeMotor?.name
            ?: activity.getString(R.string.no_active_motor)
        view.findViewById<TextView>(R.id.textSheetMotorMeta).text = activeMotor?.let {
            activity.getString(R.string.motor_kilometer_value, it.currentKilometer)
        } ?: activity.getString(R.string.add_motor_first)

        view.findViewById<View>(R.id.sheetMotorSelector).setOnClickListener {
            dialog.dismiss()
            activity.openNoAnim(MotorFormActivity::class.java)
        }
        view.findViewById<View>(R.id.sheetActionServis).setOnClickListener {
            dialog.dismiss()
            activity.openNoAnim(ServisFormActivity::class.java)
        }
        view.findViewById<View>(R.id.sheetActionOli).setOnClickListener {
            dialog.dismiss()
            activity.openNoAnim(OliFormActivity::class.java)
        }
        view.findViewById<View>(R.id.sheetActionBensin).setOnClickListener {
            dialog.dismiss()
            activity.openNoAnim(BensinFormActivity::class.java)
        }
        view.findViewById<View>(R.id.sheetActionPajak).setOnClickListener {
            dialog.dismiss()
            activity.openNoAnim(PajakFormActivity::class.java)
        }

        dialog.setContentView(view)
        dialog.show()
    }

    private fun Activity.openNoAnim(target: Class<out Activity>) {
        startActivity(Intent(this, target))
        overridePendingTransition(0, 0)
    }
}
