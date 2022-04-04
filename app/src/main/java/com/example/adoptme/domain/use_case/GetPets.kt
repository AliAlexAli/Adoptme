package com.example.adoptme.domain.use_case

import com.example.adoptme.domain.repository.PetsRepository

class GetPets(private val repository: PetsRepository) {
  suspend operator fun invoke(sex: String = "", size: String = "") = repository.getPetsFromFirestore(sex, size)
}
