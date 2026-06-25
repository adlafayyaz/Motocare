package com.example.motocare.profile

import android.app.Activity
import android.app.Dialog
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.activity.result.ActivityResultLauncher
import com.example.motocare.R

object BackupSheet {
    fun show(
        activity: Activity,
        exportLauncher: ActivityResultLauncher<String>,
        importLauncher: ActivityResultLauncher<Array<String>>
    ) {
        val dialog = Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_backup_data)
        dialog.findViewById<View>(R.id.rowExportData).setOnClickListener {
            exportLauncher.launch("motocare-backup.json")
        }
        dialog.findViewById<View>(R.id.rowImportData).setOnClickListener {
            importLauncher.launch(arrayOf("application/json", "text/*"))
        }
        dialog.show()
        dialog.window?.apply {
            setBackgroundDrawableResource(android.R.color.transparent)
            setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
            setGravity(Gravity.BOTTOM)
        }
    }
}
