package com.example.adoptme.domain.use_case

data class UseCases (
  val getPets: GetPets,
  val getPet: GetPet,
  val addPet: AddPet,
  val deletePet: DeletePet,
  val addImage: AddImage,
  val getOwner: GetOwner,
  val getOwnerByEmail: GetOwnerByEmail,
  val addOwner: AddOwner
)
