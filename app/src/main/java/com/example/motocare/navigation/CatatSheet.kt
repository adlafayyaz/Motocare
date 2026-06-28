package com.example.motocare.navigation

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.TextView
import com.example.motocare.R
import com.example.motocare.bensin.BensinFormActivity
import com.example.motocare.data.MotoCareDbHelper
import com.example.motocare.data.Motor
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
        view.findViewById<TextView>(R.id.textSheetMotorMeta).text =
            activeMotor?.plateNumber ?: activity.getString(R.string.add_motor_first)

        view.findViewById<View>(R.id.sheetMotorSelector).setOnClickListener {
            dialog.dismiss()
            showMotorPicker(activity)
        }
        view.findViewById<TextView>(R.id.textSheetChangeMotor).setOnClickListener {
            dialog.dismiss()
            showMotorPicker(activity)
        }
        view.findViewById<View>(R.id.sheetAddMotor).setOnClickListener {
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
        view.alpha = 0f
        view.translationY = 42f
        view.animate().alpha(1f).translationY(0f).setDuration(180).start()
    }

    fun showMotorPicker(activity: Activity, onMotorChanged: (() -> Unit)? = null) {
        val dialog = BottomSheetDialog(activity)
        val view = activity.layoutInflater.inflate(R.layout.sheet_motor_picker, null)
        val db = MotoCareDbHelper(activity)
        val container = view.findViewById<android.widget.LinearLayout>(R.id.motorPickerList)
        db.getAllMotors().forEach { motor ->
            val row = activity.layoutInflater.inflate(R.layout.item_motor_picker, container, false)
            row.findViewById<TextView>(R.id.textPickerMotorName).text = motor.name
            row.findViewById<TextView>(R.id.textPickerMotorPlate).text = motor.plateNumber
            row.findViewById<TextView>(R.id.textPickerMotorKm).text =
                activity.getString(R.string.km_value_short, motor.currentKilometer.toString())
            row.findViewById<TextView>(R.id.textPickerMotorActive).visibility =
                if (motor.isActive) View.VISIBLE else View.GONE
            row.setOnClickListener {
                db.setActiveMotor(motor.id)
                dialog.dismiss()
                if (onMotorChanged == null) {
                    show(activity)
                } else {
                    onMotorChanged()
                }
            }
            container.addView(row)
        }
        view.findViewById<View>(R.id.buttonPickerAddMotor).setOnClickListener {
            dialog.dismiss()
            activity.openNoAnim(MotorFormActivity::class.java)
        }
        dialog.setContentView(view)
        dialog.show()
        view.alpha = 0f
        view.translationY = 42f
        view.animate().alpha(1f).translationY(0f).setDuration(180).start()
    }

    private fun Activity.openNoAnim(target: Class<out Activity>) {
        startActivity(Intent(this, target))
        overridePendingTransition(0, 0)
    }
}
