package com.example.adoptme.domain.use_case

import com.example.adoptme.domain.repository.OwnersRepository

class GetOwner(private val repository: OwnersRepository) {
  suspend operator fun invoke(ownerId: String) = repository.getOwnerFromFirestore(ownerId)
}
