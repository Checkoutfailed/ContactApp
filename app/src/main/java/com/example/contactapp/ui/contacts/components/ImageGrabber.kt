package com.example.contactapp.ui.contacts.components

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.contactapp.ui.theme.HyperBlue
import java.io.File

@Composable
fun ImageGrabber(onImageSelected: (String?) -> Unit) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let {
                val imagePath = saveImageToInternalStorage(context, it)
                onImageSelected(imagePath)
            }
        }
    )

    Surface(
        modifier = Modifier
            .padding(10.dp)
            .height(IntrinsicSize.Min)
        ,
        color = Color.White,
        shape = RoundedCornerShape(10.dp)
    ) {
        Button(
            onClick = {
                launcher.launch("image/*")
            },
            modifier = Modifier
                .fillMaxWidth(),
            colors = ButtonColors(
                containerColor = Color.White,
                contentColor = Color.Black,
                disabledContainerColor = Color.LightGray,
                disabledContentColor = Color.DarkGray,
            ),
            shape = RoundedCornerShape(10.dp),

            ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Add Photo",
                    color = HyperBlue
                )
            }
        }
    }

}

private fun saveImageToInternalStorage(context: Context, uri: Uri): String {
    val directory = File(context.filesDir, "contact_images")
    if (!directory.exists()) directory.mkdir()


    val timestamp = System.currentTimeMillis()
    val file = File(directory, "$timestamp.jpg")

    context.contentResolver.openInputStream(uri)?.use { input ->
        file.outputStream().use { output ->
            input.copyTo(output)
        }
    }
    return file.absolutePath
}