package com.example.adoptme.domain.repository

import android.net.Uri
import com.example.adoptme.domain.model.Response
import kotlinx.coroutines.flow.Flow
import java.util.*

interface PetsRepository {

  suspend fun getPetsFromFirestore(sex: String = "", size: String = ""): Flow<Response<Void?>>

  suspend fun getPetFromFirestore(id: String): Flow<Response<Void?>>

  suspend fun addPetToFirestore(
    name: String?,
    birth: Date?,
    sex: String?,
    size: String?,
    description: String?,
    image: String?
  ): Flow<Response<Void?>>

  suspend fun addImageToStorage(fileName: String, file: Uri): Flow<Response<Void?>>

}
