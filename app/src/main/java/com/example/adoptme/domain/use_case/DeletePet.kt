package com.example.adoptme.domain.use_case

import com.example.adoptme.domain.repository.PetsRepository

class DeletePet(private val repository: PetsRepository) {
  suspend operator fun invoke(petId: String) = repository.deletePetFromFirestore(petId)
}
