package com.example.adoptme.domain.use_case

import android.net.Uri
import com.example.adoptme.domain.repository.PetsRepository
import java.util.*

class AddImage(
  private val repository: PetsRepository
) {
  suspend operator fun invoke(
    fileName : String,
    file: Uri
  )  = repository.addImageToStorage(fileName, file)
}


