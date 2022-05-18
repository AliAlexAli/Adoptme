package com.example.adoptme.data.repository

import android.net.Uri
import android.util.Log
import com.example.adoptme.core.Constants
import com.example.adoptme.domain.model.Owner
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
  private val storageRef: StorageReference,
  private val ownersRef: CollectionReference
) : PetsRepository {
  override suspend fun getPetsFromFirestore(sex: String, size: String) = callbackFlow {
    var ref = petsRef.whereNotEqualTo("id", "a")
    if (sex != "") ref = ref.whereEqualTo("sex", sex)
    if (size != "") ref = ref.whereEqualTo("size", size)
    val snapshotListener = ref
      .addSnapshotListener { snapshot, e ->
        val response = if (snapshot != null) {
          val pets = snapshot.toObjects(Pet::class.java)
          Response.Success(pets)
        } else {
          Response.Error(e?.message ?: e.toString())
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
          Response.Error(e?.message ?: e.toString())
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
    image: String?,
    owner: String?
  ) = flow {
    try {
      emit(Response.Loading)
      val petId = petsRef.document().id
      val pet = Pet(petId, name, birth, sex, size, description, image, owner)
      val addition = petsRef.document(petId).set(pet).await()
      emit(Response.Success(addition))
    } catch (e: Exception) {
      emit(Response.Error(e.message ?: e.toString()))
    }
  } as Flow<Response<Void?>>

  override suspend fun addImageToStorage(fileName: String, file: Uri) = flow {
    try {
      var response = Constants.PLACEHOLDERIMG
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

  override suspend fun getOwnerFromFirestore(id: String) =
    callbackFlow {
      val snapshotListener = ownersRef.whereEqualTo("id", id).addSnapshotListener { snapshot, e ->
        val response = if (snapshot != null) {
          val owner = snapshot.toObjects(Owner::class.java)
          if (owner.isNotEmpty()) {
            Response.Success(owner.first())
          } else {
            Response.Error("empty")
          }
        } else {
          Response.Error(e?.message ?: e.toString())
        }
        trySend(response).isSuccess
      }
      awaitClose {
        snapshotListener.remove()
      }
    } as Flow<Response<Void?>>


  override suspend fun getOwnerFromFirestoreByEmail(email: String) =
    callbackFlow {
      val snapshotListener =
        ownersRef.whereEqualTo("email", email).addSnapshotListener { snapshot, e ->
          val response = if (snapshot != null) {
            val owner = snapshot.toObjects(Owner::class.java)
            if (owner.isNotEmpty()) {
              owner.first()
            } else {
            }
          } else {
            Owner()
          }
          trySend(response).isSuccess
        }
      awaitClose {
        snapshotListener.remove()
      }

    } as Flow<Owner>

  override suspend fun addOwnerToFirestore(
    name: String?,
    email: String?,
    phone: String?,
    address: String?,
    website: String?
  ) = flow {
    emit(Response.Loading)
    try {
      val userId = ownersRef.document().id
      val owner = Owner(id = userId, name = name, email = email, phone = phone, address = address, website = website)
      val addition = ownersRef.document(userId).set(owner).await()
      emit(Response.Success(addition))
    } catch (e: Exception) {
      Log.d("FIRESTONE ADD failed", e.message.toString())
      emit(Response.Error(e.message ?: e.toString()))
    }
  } as Flow<Response<Void?>>
}
