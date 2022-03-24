package com.example.adoptme.domain.repository

import com.example.adoptme.domain.model.Response
import kotlinx.coroutines.flow.Flow
import java.util.*

interface PetsRepository {

  suspend fun getPetsFromFirestore(): Flow<Response<Void?>>
/*
  {

    var pets = ArrayList<Pet>()

    firestore.collection("pets").get().addOnSuccessListener { documents ->
      for (document in documents) {
        Log.d(TAG, "${document.id} => ${document.data}")
        pets.add(
          Pet(
            id = document.id,
            birth = document.getTimestamp("birth")?.toDate() as Date,
            date = document.getTimestamp("date")
              ?.toDate() as Date,
            description = document["description"] as String,
            name = document["name"] as String,
            sex = document["sex"] as String,
            size = document["size"] as String,
            imageURL = document["image"] as String
          )
        )
      }
    }
      .addOnFailureListener { exception ->
        Log.w(TAG, "Error getting documents: ", exception)
      }.await()
    return pets
  }
*/

  suspend fun getPetFromFirestore(id: String): Flow<Response<Void?>>
  /*{

    var pets = ArrayList<Pet>()

    firestore.collection("pets").document(id).get().addOnSuccessListener { document ->
      Log.d(TAG, "${document.id} => ${document.data}")
      pets.add(
        Pet(
          id = document.id,
          birth = document.getTimestamp("birth")?.toDate() as Date,
          date = document.getTimestamp("date")
            ?.toDate() as Date,
          description = document["description"] as String,
          name = document["name"] as String,
          sex = document["sex"] as String,
          size = document["size"] as String,
          imageURL = document["image"] as String
        )
      )
    }
      .addOnFailureListener { exception ->
        Log.w(TAG, "Error getting Pet: ", exception)
      }.await()

    return pets
  }*/

  suspend fun addPetToFirestore(
    name: String?,
    birth: Date?,
    sex: String?,
    size: String?,
    description: String?,
    image: String?
  ): Flow<Response<Void?>>
}
