package com.example.adoptme.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adoptme.domain.model.Pet
import com.example.adoptme.domain.model.Response
import com.example.adoptme.domain.use_case.UseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class PetsViewModel @Inject constructor(private val useCases: UseCases) : ViewModel() {

  private val _data = mutableStateOf<Response<ArrayList<Pet>>>(Response.Loading)
  val data: State<Response<ArrayList<Pet>>> = _data

  var showAddPet = mutableStateOf(false)


  fun getPets() {
    viewModelScope.launch {
      useCases.getPets()
        .collect() { response -> _data.value = response as Response<ArrayList<Pet>> }
    }
  }

  fun getPetById(petId: String) {
    viewModelScope.launch {
      useCases.getPet(petId)
        .collect() { response -> _data.value = response as Response<ArrayList<Pet>> }
    }
  }

  fun addPet(
    name: String?,
    birth: Date?,
    sex: String?,
    size: String?,
    description: String?,
    image: String?
  ) {
    viewModelScope.launch {
      _data.value = Response.Loading
      useCases.addPet(name, birth, sex, size, description, image)
        .collect() { response -> _data.value = response as Response<ArrayList<Pet>> }
    }
  }
}
