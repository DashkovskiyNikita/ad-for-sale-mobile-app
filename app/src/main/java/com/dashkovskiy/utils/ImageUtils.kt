package com.dashkovskiy.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import java.io.ByteArrayOutputStream

fun Context.uriToBitmap(uri: Uri): Bitmap {
    return if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
        ImageDecoder.decodeBitmap(ImageDecoder.createSource(contentResolver, uri))
    } else {
        MediaStore.Images.Media.getBitmap(contentResolver, uri)
    }
}

class ImageUtils(private val context: Context) {
    fun convertUriContentToByteArray(uri: Uri): ByteArray {
        val bitmap = context.uriToBitmap(uri = uri)
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        return outputStream.toByteArray()
    }
}