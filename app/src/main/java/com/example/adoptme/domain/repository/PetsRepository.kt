package com.example.adoptme.domain.repository

import android.net.Uri
import com.example.adoptme.domain.model.Pet
import com.example.adoptme.domain.model.Response
import com.example.adoptme.domain.model.enums.Sex
import com.example.adoptme.domain.model.enums.Size
import kotlinx.coroutines.flow.Flow
import java.util.*
import kotlin.collections.ArrayList

interface PetsRepository {

    suspend fun getPetsFromFirestore(
        sex: MutableList<Sex>, size: MutableList<Size>, owner: String?, favorite: Boolean,
        favoriteIds: MutableList<String>
    ): Flow<Response<ArrayList<Pet>>>

    suspend fun getPetFromFirestore(id: String): Flow<Response<ArrayList<Pet>>>

    suspend fun deletePetFromFirestore(id: String): Flow<Response<Unit>>

    suspend fun addPetToFirestore(
        id: String?,
        name: String?,
        birth: Date?,
        sex: Sex?,
        size: Size?,
        description: String?,
        image: String?,
        owner: String?
    ): Flow<Response<Void?>>

    suspend fun addImageToStorage(fileName: String, file: Uri): Flow<Response<Void?>>
}
