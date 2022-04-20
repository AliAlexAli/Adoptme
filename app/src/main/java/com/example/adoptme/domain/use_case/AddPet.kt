package com.example.adoptme.domain.use_case

import com.example.adoptme.domain.model.Pet
import com.example.adoptme.domain.repository.PetsRepository
import java.util.*

class AddPet(
  private val repository: PetsRepository
) {
  suspend operator fun invoke(
    name: String?,
    birth: Date?,
    sex: String?,
    size: String?,
    description: String?,
    image: String?,
    owner: String?
  )  = repository.addPetToFirestore(name, birth, sex, size, description, image, owner)
}

