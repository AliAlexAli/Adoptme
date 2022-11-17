package com.example.adoptme.data.repository

import android.net.Uri
import android.util.Log
import com.example.adoptme.core.Constants
import com.example.adoptme.domain.model.Pet
import com.example.adoptme.domain.model.Response
import com.example.adoptme.domain.model.enums.Sex
import com.example.adoptme.domain.model.enums.Size
import com.example.adoptme.domain.model.firebaseObjToPet
import com.example.adoptme.domain.model.petToFirebaseObj
import com.example.adoptme.domain.repository.PetsRepository
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.async
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
  private val storageRef: StorageReference,
  private val firebaseMessaging: FirebaseMessaging
  ) : PetsRepository {
  override suspend fun getPetsFromFirestore(
    sex: MutableList<Sex>,
    size: MutableList<Size>,
    owner: String?
  ): Flow<Response<ArrayList<Pet>>> = callbackFlow {
    var query = petsQuery
    if (owner != null) query = query.whereEqualTo("owner", owner)
    size.map { it.value }.forEach({Log.d("sizee", it)})
    fun sexFilter(value: String?) = sex.isEmpty() || sex.map { it.value }.contains(value)
    fun sizeFilter(value: String?) = size.isEmpty() || size.map { it.value }.contains(value)

    FirebaseMessaging.getInstance().subscribeToTopic("Global")

    val snapshotListener = query
      .addSnapshotListener { snapshot, e ->
        val response = if (snapshot != null) {
          val pets: MutableList<Pet> = ArrayList()
          snapshot.documents.filter { snapshot ->
            sexFilter(snapshot.getString(Pet.sex)) && sizeFilter(
              snapshot.getString(Pet.size)
            )
          }.forEach(action = { documentSnapshot -> pets.add(firebaseObjToPet(documentSnapshot)) })
          Response.Success(pets)
        } else {
          Response.Error(e?.message ?: e.toString())
        }
        trySend(response)
      }
    awaitClose {
      snapshotListener.remove()
    }
  } as Flow<Response<ArrayList<Pet>>>

  override suspend fun getPetFromFirestore(id: String) =
    callbackFlow {
      val snapshotListener = petsQuery.whereEqualTo("id", id).addSnapshotListener { snapshot, e ->
        Log.d("GetPet", id)
        val response = if (snapshot != null) {
          val pets: MutableList<Pet> = ArrayList()
          snapshot.documents.forEach(action = { documentSnapshot ->
            pets.add(
              firebaseObjToPet(
                documentSnapshot
              )
            )
          })
          Response.Success(pets)
        } else {
          Response.Error(e?.message ?: e.toString())
        }
        trySend(response)
      }
      awaitClose {
        snapshotListener.remove()
      }
    } as Flow<Response<ArrayList<Pet>>>

  override suspend fun deletePetFromFirestore(id: String) = callbackFlow {
    var response: Response<Unit> = Response.Loading
    val delete = async {
      petsRef.document(id).delete()
        .addOnSuccessListener {
          Log.d("deletePet", "winner")
          response = Response.Success(Unit)
        }
        .addOnFailureListener { e ->
          response = Response.Error(e.message ?: e.toString())
        }
    }
    delete.await()
    trySend(response)
    awaitClose {}
  }

  override suspend fun addPetToFirestore(
    id: String?,
    name: String?,
    birth: Date?,
    sex: Sex?,
    size: Size?,
    description: String?,
    image: String?,
    owner: String?
  ) = flow {
    try {
      emit(Response.Loading)
      val petId = id ?: petsRef.document().id
      val pet = Pet(petId, name, birth, sex, size, description, image, owner)
      val addition = petsRef.document(petId).set(petToFirebaseObj(pet)).await()
      emit(Response.Success(addition))
    } catch (e: Exception) {
      emit(Response.Error(e.message ?: e.toString()))
    }
  } as Flow<Response<Void?>>

  override suspend fun addImageToStorage(fileName: String, file: Uri) = flow {
    try {
      var response = Constants.PLACEHOLDER_IMG
      emit(Response.Loading)
      storageRef.child(fileName).putFile(file).continueWithTask { task ->
        if (!task.isSuccessful) task.exception?.let {
          throw it
        }
        storageRef.child(fileName).downloadUrl
      }.addOnCompleteListener { task ->
        response = task.result.toString()
      }.await()

      emit(Response.Success(response))

    } catch (e: Exception) {
      emit(Error(e.message ?: e.toString()))
    }
  } as Flow<Response<Void?>>
}
