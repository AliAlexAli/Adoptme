package com.example.adoptme.di

import com.example.adoptme.core.Constants
import com.example.adoptme.data.repository.OwnersRepositoryImpl
import com.example.adoptme.data.repository.PetsRepositoryImpl
import com.example.adoptme.domain.repository.OwnersRepository
import com.example.adoptme.domain.repository.PetsRepository
import com.example.adoptme.domain.use_case.*
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named


@Module
@InstallIn(SingletonComponent::class)
object AppModule {
  @Provides
  fun provideFirebaseFirestore() = FirebaseFirestore.getInstance()

  @Provides
  fun provideFirebaseMessaging() = FirebaseMessaging.getInstance()

  @Provides
  fun providePetsRef(db: FirebaseFirestore) = db.collection(Constants.PETS)

  @Provides
  fun providePetsQuery(petsRef: CollectionReference) = petsRef.orderBy("name")

  @Provides
  @Named("ownersRef")
  fun provideOwnersRef(db: FirebaseFirestore) = db.collection(Constants.OWNERS)

  @Provides
  fun provideFirebaseStorage() = Firebase.storage

  @Provides
  fun provideStorageRef(st: FirebaseStorage) = st.reference

  @Provides
  fun providePetsRepository(
    petsRef: CollectionReference,
    petsQuery: Query,
    storageRef: StorageReference,
  ): PetsRepository = PetsRepositoryImpl(petsRef, petsQuery, storageRef)

  @Provides
  fun provideOwnersRepository(
    @Named("ownersRef")
    ownersRef: CollectionReference,
  ): OwnersRepository = OwnersRepositoryImpl(ownersRef)

  @Provides
  fun provideUseCases(petsRepository: PetsRepository, ownersRepository: OwnersRepository) = UseCases(
    getPets = GetPets(petsRepository),
    getPet = GetPet(petsRepository),
    addPet = AddPet(petsRepository),
    addImage = AddImage(petsRepository),
    getOwner = GetOwner(ownersRepository),
    getOwnerByEmail = GetOwnerByEmail(ownersRepository),
    addOwner = AddOwner(ownersRepository),
    deletePet = DeletePet(petsRepository)
  )
}
