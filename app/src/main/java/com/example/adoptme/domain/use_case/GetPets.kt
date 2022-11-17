package com.example.adoptme.domain.use_case

import com.example.adoptme.domain.model.enums.Sex
import com.example.adoptme.domain.model.enums.Size
import com.example.adoptme.domain.repository.PetsRepository

class GetPets(private val repository: PetsRepository) {
  suspend operator fun invoke(sex: MutableList<Sex>, size: MutableList<Size>, owner: String?) = repository.getPetsFromFirestore(sex, size, owner)
}
