package com.example.adoptme.presentation

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adoptme.domain.model.Pet
import com.example.adoptme.domain.model.Response
import com.example.adoptme.domain.use_case.UseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject


@HiltViewModel
class PetsViewModel @Inject constructor(private val useCases: UseCases) : ViewModel() {

  private val _data = mutableStateOf<Response<ArrayList<Pet>>>(Response.Loading)
  var data: State<Response<ArrayList<Pet>>> = _data

  val showAddPet = mutableStateOf(false)
  val showSearchDialog = mutableStateOf(false)

  val userId = mutableStateOf<String>("")

  fun getPets(sex: String = "", size: String = "") {
    _data.value = Response.Loading
    viewModelScope.launch {
      useCases.getPets(sex, size)
        .collect() { response -> _data.value = response as Response<ArrayList<Pet>> }
    }
  }

  fun getPetById(petId: String) {
    _data.value = Response.Loading
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
    image: Uri?,
    owner: String?
  ) {
    viewModelScope.launch {
      if (image != null) {
        withContext(Dispatchers.Default) {
          addImage("id.jpg", image)
        }
      }

      _data.value = Response.Loading
      if (uploadResponse.value is Response.Success<String>)
        useCases.addPet(
          name,
          birth,
          sex,
          size,
          description,
          (uploadResponse.value as Response.Success<String>).data,
          owner
        )
          .collect() { response -> _data.value = response as Response<ArrayList<Pet>> }
      getPets()
    }
  }

  private val _uploadResponse = mutableStateOf<Response<String>>(Response.Loading)
  var uploadResponse: State<Response<String>> = _uploadResponse

  suspend fun addImage(filename: String, file: Uri): Boolean {
    useCases.addImage(filename, file)
      .collect() { response -> _uploadResponse.value = response as Response<String> }
    return true
  }

}
