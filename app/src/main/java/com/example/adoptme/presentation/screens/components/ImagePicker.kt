package com.example.adoptme.presentation.screens.components

import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.documentfile.provider.DocumentFile
import com.example.adoptme.R


@Composable
fun ImagePicker(context: Context, filePath: MutableState<Uri>, extension: MutableState<String>) {

  val launcher = rememberLauncherForActivityResult(
    contract =
    ActivityResultContracts.GetContent()
  ) {
    if (it != null) {
      filePath.value = it
      extension.value = MimeTypeMap.getSingleton()
        .getExtensionFromMimeType(context.contentResolver.getType(filePath.value))!!
    }
  }
  Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
    Button(onClick = {
      launcher.launch("image/*")
    }) {
      Text(text = stringResource(R.string.uploadImage))
    }
    DocumentFile.fromSingleUri(context, filePath.value)?.name
      ?.let { Text(text = it, color = Color.White.copy(0.6f)) }
  }
}
