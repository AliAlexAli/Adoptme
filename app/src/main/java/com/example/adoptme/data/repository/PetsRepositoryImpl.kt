package com.example.adoptme.data.repository

import android.util.Log
import com.example.adoptme.domain.model.Pet
import com.example.adoptme.domain.model.Response
import com.example.adoptme.domain.repository.PetsRepository
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PetsRepositoryImpl @Inject constructor(
  private val petsRef: CollectionReference,
  private val petsQuery: Query,
) : PetsRepository {
  override suspend fun getPetsFromFirestore() = callbackFlow {
    val snapshotListener = petsQuery.addSnapshotListener { snapshot, e ->
      val response = if (snapshot != null) {
        val pets = snapshot.toObjects(Pet::class.java)
        Response.Success(pets)
      } else {
        Error(e?.message ?: e.toString())
      }
      trySend(response).isSuccess
    }
    awaitClose {
      snapshotListener.remove()
    }
  } as Flow<Response<Void?>>

  override suspend fun getPetFromFirestore(id: String) =
    callbackFlow {
      Log.d("getPettt", id)
      val snapshotListener = petsRef.whereEqualTo("id", id).addSnapshotListener { snapshot, e ->
        val response = if (snapshot != null) {
          val pets = snapshot.toObjects(Pet::class.java)
          Response.Success(pets)
        } else {
          Error(e?.message ?: e.toString())
        }
        trySend(response).isSuccess
      }
      awaitClose {
        snapshotListener.remove()
      }
    } as Flow<Response<Void?>>


  override suspend fun addPetToFirestore(
    name: String?,
    birth: Date?,
    sex: String?,
    size: String?,
    description: String?,
    image: String?
  ) = flow {
    try {
      emit(Response.Loading)
      val petId = petsRef.document().id
      val pet = Pet(petId, name, birth, sex, size, description, image)
      val addition = petsRef.document(petId).set(pet).await()
      emit(Response.Success(addition))
    } catch (e: Exception) {
      emit(Error(e.message ?: e.toString()))
    }
  } as Flow<Response<Void?>>

}
