package com.example.adoptme.data.repository

import android.util.Log
import com.example.adoptme.domain.model.Owner
import com.example.adoptme.domain.model.Response
import com.example.adoptme.domain.repository.OwnersRepository
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OwnersRepositoryImpl @Inject constructor(
  private val ownersRef: CollectionReference,
) : OwnersRepository {
  override suspend fun getOwnerFromFirestore(id: String) =
    callbackFlow {
      val snapshotListener = ownersRef.whereEqualTo("id", id).addSnapshotListener { snapshot, e ->
        val response = if (snapshot != null) {
          val owner = snapshot.toObjects(Owner::class.java)
          owner.first()
        } else {
          Owner()
        }
        trySend(response).isSuccess
      }
      awaitClose {
        snapshotListener.remove()
      }

    } as Flow<Owner>


  override suspend fun getOwnerFromFirestoreByEmail(email: String) =
    callbackFlow {
      val snapshotListener =
        ownersRef.whereEqualTo("email", email).addSnapshotListener { snapshot, e ->
          if (snapshot != null) {
            Log.d("getUserOwner", email + snapshot.size())
            val owner = snapshot.toObjects(Owner::class.java)
            trySend(owner.first()).isSuccess
          } else {
            trySend(Owner()).isSuccess
          }
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
    website: String?,
    id: String?
  ) = flow {
    emit(Response.Loading)
    try {
      val userId = id ?: ownersRef.document().id
      val owner = Owner(
        id = userId,
        name = name,
        email = email,
        phone = phone,
        address = address,
        website = website
      )
      ownersRef.document(userId).set(owner).await()
      emit(Response.Success(null))
    } catch (e: Exception) {
      Log.d("FIRESTONE ADD failed", e.message.toString())
      emit(Response.Error(e.message ?: e.toString()))
    }
  } as Flow<Response<Void?>>
}
