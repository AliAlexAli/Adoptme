package com.example.adoptme.domain.use_case

import com.example.adoptme.domain.repository.OwnersRepository

class GetOwnerByEmail(private val repository: OwnersRepository) {
  suspend operator fun invoke(email: String) = repository.getOwnerFromFirestoreByEmail(email)
}
