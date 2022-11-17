package com.example.adoptme.presentation

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adoptme.domain.model.Owner
import com.example.adoptme.domain.model.Pet
import com.example.adoptme.domain.model.Response
import com.example.adoptme.domain.model.enums.Sex
import com.example.adoptme.domain.model.enums.Size
import com.example.adoptme.domain.use_case.UseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class Filter {
  var sex: MutableList<Sex> = ArrayList()
  var size: MutableList<Size> = ArrayList()
  var owner: String? = null
}

@HiltViewModel
class PetsViewModel @Inject constructor(private val useCases: UseCases) : ViewModel() {

  private val _data = mutableStateOf<Response<ArrayList<Pet>>>(Response.Loading)
  var data: State<Response<ArrayList<Pet>>> = _data

  private val _ownerData = mutableStateOf(Owner())
  var ownerData: State<Owner> = _ownerData

  private val _filter = mutableStateOf(Filter())
  var filter: State<Filter> = _filter

  val showAddPet = mutableStateOf(false)
  val showSearchDialog = mutableStateOf(false)

  fun setOwner(owner: Owner = Owner()) {
    _ownerData.value = owner
  }

  fun setFilterSex(sex: MutableList<Sex>) {
    _filter.value.sex = sex
  }

  fun setFilterSize(size: MutableList<Size>) {
    _filter.value.size = size
  }

  fun setFilterOwner(owner: String? = null) {
    _filter.value.owner = owner
  }

  fun getPets() {
    _data.value = Response.Loading
    viewModelScope.launch {
      useCases.getPets(filter.value.sex, filter.value.size, filter.value.owner)
        .collect { response -> _data.value = response }
    }
  }

  fun clearFilters() {
    _filter.value.sex.clear()
    _filter.value.size.clear()
    _filter.value.owner = null
  }

  fun getPetById(petId: String) {
    _data.value = Response.Loading
    viewModelScope.launch {
      useCases.getPet(petId)
        .collect { response -> _data.value = response }
    }
  }

  suspend fun deletePet(petId: String): Response<Unit> {
    var response: Response<Unit> = Response.Loading

    viewModelScope.launch {
      useCases.deletePet(petId)
        .collect { res -> response = res }
    }

    return response
  }

  fun getOwnerById(ownerId: String) {
    Log.d("ownerId", ownerId)
    _ownerData.value = Owner()
    viewModelScope.launch {
      useCases.getOwner(ownerId)
        .collect { response -> _ownerData.value = response }
    }
  }


  fun addPet(
    id: String?,
    name: String?,
    birth: Date?,
    sex: Sex?,
    size: Size?,
    description: String?,
    image: Uri?,
    imageExt: String?,
    owner: String?
  ) {
    viewModelScope.launch {
      if (image != null && image != Uri.EMPTY) {
        withContext(Dispatchers.Default) {
          addImage("${UUID.randomUUID()}.$imageExt", image)
        }
      }

      Log.d("addPetView", id.toString())

      _data.value = Response.Loading
      val img =
        if (uploadResponse.value is Response.Success) (uploadResponse.value as Response.Success<String>).data else null
      useCases.addPet(
        id,
        name,
        birth,
        sex,
        size,
        description,
        img,
        owner
      )
        .collect { response -> _data.value = response as Response<ArrayList<Pet>> }
      getPets()
    }
  }

  private val _uploadResponse = mutableStateOf<Response<String>>(Response.Loading)
  private var uploadResponse: State<Response<String>> = _uploadResponse

  suspend fun addImage(filename: String, file: Uri): Boolean {
    useCases.addImage(filename, file)
      .collect { response -> _uploadResponse.value = response as Response<String> }
    return true
  }

}
