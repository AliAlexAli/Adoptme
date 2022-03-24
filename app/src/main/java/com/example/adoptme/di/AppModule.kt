package com.example.adoptme.di

import com.example.adoptme.core.Constants
import com.example.adoptme.data.repository.PetsRepositoryImpl
import com.example.adoptme.domain.repository.PetsRepository
import com.example.adoptme.domain.use_case.AddPet
import com.example.adoptme.domain.use_case.GetPet
import com.example.adoptme.domain.use_case.GetPets
import com.example.adoptme.domain.use_case.UseCases
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
  @Provides
  fun provideFirebaseFirestore() = FirebaseFirestore.getInstance()

  @Provides
  fun providePetsRef(db: FirebaseFirestore) = db.collection(Constants.PETS)

  @Provides
  fun providePetsQuery(petsRef: CollectionReference) = petsRef.orderBy("name")

  @Provides
  fun provideBooksRepository(
    petsRef: CollectionReference,
    petsQuery: Query
  ): PetsRepository = PetsRepositoryImpl(petsRef, petsQuery)

  @Provides
  fun provideUseCases(repository: PetsRepository) = UseCases(
    getPets = GetPets(repository),
    getPet = GetPet(repository),
    addPet = AddPet(repository)
  )
}
