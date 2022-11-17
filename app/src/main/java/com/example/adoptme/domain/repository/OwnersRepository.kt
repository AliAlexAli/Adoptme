package com.example.adoptme.domain.repository

import com.example.adoptme.domain.model.Owner
import com.example.adoptme.domain.model.Response
import kotlinx.coroutines.flow.Flow

interface OwnersRepository {
  suspend fun getOwnerFromFirestore(id: String): Flow<Owner>

  suspend fun getOwnerFromFirestoreByEmail(email: String): Flow<Owner>

  suspend fun addOwnerToFirestore(
    name: String?,
    email: String?,
    phone: String?,
    address: String?,
    website: String?,
    id: String?
  ): Flow<Response<Void?>>
}
