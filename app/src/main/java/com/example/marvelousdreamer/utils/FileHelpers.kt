package com.example.marvelousdreamer.utils

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

private fun imageDir(context: Context): File =
    File(context.filesDir, "images").apply { mkdirs() }

fun copyUriInternal(context: Context, source: Uri): Uri {
    val file = File(imageDir(context), "${UUID.randomUUID()}.jpg")
    context.contentResolver.openInputStream(source).use { input ->
        FileOutputStream(file).use { output -> input?.copyTo(output) }
    }
    return file.toUri()
}
