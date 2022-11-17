package com.example.adoptme.domain.use_case

import com.example.adoptme.domain.model.enums.Sex
import com.example.adoptme.domain.model.enums.Size
import com.example.adoptme.domain.repository.PetsRepository
import java.util.*

class AddPet(
  private val repository: PetsRepository
) {
  suspend operator fun invoke(
    id: String?,
    name: String?,
    birth: Date?,
    sex: Sex?,
    size: Size?,
    description: String?,
    image: String?,
    owner: String?
  ) = repository.addPetToFirestore(id, name, birth, sex, size, description, image, owner)
}

