package com.example.adoptme.domain.use_case

import com.example.adoptme.domain.repository.OwnersRepository

class AddOwner(
  private val repository: OwnersRepository
) {
  suspend operator fun invoke(
    name: String?,
    email: String?,
    phone: String?,
    address: String?,
    website: String?,
    id: String? = null
  ) =
    repository.addOwnerToFirestore(name, email, phone, address, website, id)
}
