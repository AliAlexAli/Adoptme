package com.example.adoptme.data.repository

import android.net.Uri
import android.util.Log
import com.example.adoptme.domain.model.Pet
import com.example.adoptme.domain.model.Response
import com.example.adoptme.domain.repository.PetsRepository
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import com.google.firebase.storage.StorageReference
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
  private val storageRef: StorageReference
) : PetsRepository {
  override suspend fun getPetsFromFirestore(sex: String, size: String) = callbackFlow {
    var ref = petsRef.whereNotEqualTo("id","")
    if(sex != "") ref = ref.whereEqualTo("sex", sex)
    if(size != "") ref = ref.whereEqualTo("size", size)
    val snapshotListener = ref
      .addSnapshotListener { snapshot, e ->
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

  override suspend fun addImageToStorage(fileName: String, file: Uri) = flow {
    try {
      var response =
        "https://firebasestorage.googleapis.com/v0/b/adoptme-36e2a.appspot.com/o/WE4CEX3HBhoNbeV9FLLI.jpg?alt=media&token=39c85f4b-9980-41dd-8984-11d889e1f4db"
      emit(Response.Loading)
      var uploadTask = storageRef.child(fileName).putFile(file).continueWithTask { task ->
        if (!task.isSuccessful) task.exception?.let {
          throw it
        }
        storageRef.child(fileName).downloadUrl
      }.addOnCompleteListener { task ->
        response = task.result.toString()
        Log.d("NEMMUKODIK", response)
      }.await()

      emit(Response.Success(response))

    } catch (e: Exception) {
      emit(Error(e.message ?: e.toString()))
    }
  } as Flow<Response<Void?>>
}
