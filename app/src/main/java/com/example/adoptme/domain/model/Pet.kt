package com.example.adoptme.domain.model

import com.example.adoptme.domain.model.enums.Sex
import com.example.adoptme.domain.model.enums.Size
import com.google.firebase.firestore.DocumentSnapshot
import java.util.*
import javax.inject.Inject

class FirebasePet @Inject constructor(
  val id: String?,
  val name: String?,
  val birth: Date?,
  val sex: String?,
  val size: String?,
  val description: String?,
  val image: String?,
  val owner: String?,
  val date: Date? = Calendar.getInstance().time
)

  class Pet @Inject constructor(
  val id: String?,
  val name: String?,
  val birth: Date?,
  val sex: Sex?,
  val size: Size?,
  val description: String?,
  val image: String?,
  val owner: String?,
  val date: Date? = Calendar.getInstance().time
) {

  companion object {
    const val id = "id"
    const val name = "name"
    const val birth = "birth"
    const val sex = "sex"
    const val size = "size"
    const val description = "description"
    const val image = "image"
    const val owner = "owner"
  }

  constructor() : this(null, null, null, null, null, null, null, null)
}

fun firebaseObjToPet(snapshot: DocumentSnapshot): Pet {
  return Pet(
    snapshot.getString(Pet.id),
    snapshot.getString(Pet.name),
    snapshot.getDate(Pet.birth),
    snapshot.getString(Pet.sex)?.let { Sex.values().find { sex -> sex.value.equals(it) } },
    snapshot.getString(Pet.size)?.let { Size.values().find { size -> size.value.equals(it) } },
    snapshot.getString(Pet.description),
    snapshot.getString(Pet.image),
    snapshot.getString(Pet.owner)
  )
}

fun petToFirebaseObj(pet: Pet): FirebasePet {
  return FirebasePet(
    pet.id,
    pet.name,
    pet.birth,
    pet.sex?.value,
    pet.size?.value,
    pet.description,
    pet.image,
    pet.owner,
  )
}
