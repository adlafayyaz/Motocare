package com.example.motocare.profile

import android.graphics.BitmapFactory
import android.net.Uri
import android.view.View
import android.widget.ImageView
import com.example.motocare.R
import java.net.URL
import kotlin.concurrent.thread

object ProfileAvatarLoader {
    fun load(image: ImageView, avatarUri: String?) {
        if (avatarUri.isNullOrBlank()) {
            image.showFallback()
            return
        }

        if (avatarUri.startsWith("http")) {
            image.showFallback()
            thread {
                val bitmap = runCatching {
                    URL(avatarUri).openStream().use(BitmapFactory::decodeStream)
                }.getOrNull()
                image.post {
                    if (bitmap == null) {
                        image.showFallback()
                    } else {
                        image.setPadding(0, 0, 0, 0)
                        image.setImageBitmap(bitmap)
                        image.visibility = View.VISIBLE
                    }
                }
            }
        } else {
            image.setPadding(0, 0, 0, 0)
            image.setImageURI(Uri.parse(avatarUri))
            image.visibility = View.VISIBLE
        }
    }

    private fun ImageView.showFallback() {
        val padding = (20 * resources.displayMetrics.density).toInt()
        setPadding(padding, padding, padding, padding)
        setImageResource(R.drawable.ic_user)
        visibility = View.VISIBLE
    }
}
