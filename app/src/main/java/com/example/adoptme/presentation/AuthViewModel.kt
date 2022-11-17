package com.example.adoptme.presentation

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adoptme.domain.model.Owner
import com.example.adoptme.domain.use_case.UseCases
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "AuthViewModel"

@HiltViewModel
class AuthViewModel @Inject constructor(private val useCases: UseCases) : ViewModel() {

  private var auth = Firebase.auth

  private val _isLoggedIn = mutableStateOf(false)
  val isLoggedIn: State<Boolean> = _isLoggedIn

  private val _error = mutableStateOf("")
  val error: State<String> = _error

  private val _userEmail = mutableStateOf("")
  val userEmail: State<String> = _userEmail

  private val _password = mutableStateOf("")
  val password: State<String> = _password

  private val _petId = mutableStateOf("")
  val petId: State<String> = _petId

  private val _name = mutableStateOf<String>("")
  val name: State<String> = _name

  private val _phone = mutableStateOf<String>("")
  val phone: State<String> = _phone

  private val _address = mutableStateOf<String>("")
  val address: State<String> = _address

  private val _website = mutableStateOf<String>("")
  val website: State<String> = _website

  private val _user = mutableStateOf<Owner>(Owner())
  val user: State<Owner> = _user

  // Setters
  fun setUserEmail(email: String) {
    _userEmail.value = email
  }

  fun setPassword(password: String) {
    _password.value = password
  }

  fun setError(error: String) {
    _error.value = error
  }

  fun setpetId(petId: String) {
    _petId.value = petId
  }

  fun setName(name: String) {
    _name.value = name
  }

  fun setPhone(phone: String) {
    _phone.value = phone
  }

  fun setAddress(address: String) {
    _address.value = address
  }

  fun setWebsite(website: String) {
    _website.value = website
  }

  init {
    _isLoggedIn.value = false
  }

  fun getUserByEmail(email: String) {
    Log.d("getUserByEmail", email)
    _user.value = Owner()
    viewModelScope.launch {
      useCases.getOwnerByEmail(email)
        .collect() { response -> _user.value = response; response.id?.let {
          Log.d("getUserResponse",
            it
          )
        } }
    }
  }

  fun createUserWithEmailAndPassword() = viewModelScope.launch {

    auth.createUserWithEmailAndPassword(_userEmail.value, _password.value)
      .addOnCompleteListener { task ->
        if (task.isSuccessful) {
          viewModelScope.launch {
            useCases.addOwner(
              name.value,
              userEmail.value,
              phone.value,
              address.value,
              website.value
            ).collect() {}
          }          // Sign in success, update UI with the signed-in user's information
          Log.d(TAG, "createUserWithEmail:success")
          val user = auth.currentUser
          if (user != null) {
            user.email?.let { getUserByEmail(it) }
            _user.value.id?.let { Log.d("Logged in", it) }
          }
          _isLoggedIn.value = true
        } else {
          // If sign in fails, display a message to the user.
          _error.value = task.exception?.localizedMessage ?: "Ismeretlen hiba!"
          Log.w(TAG, "createUserWithEmail:failure", task.exception)
        }
      }
  }

  fun updateUser() = viewModelScope.launch {

    viewModelScope.launch {
      useCases.addOwner(
        name.value,
        userEmail.value,
        phone.value,
        address.value,
        website.value,
        user.value.id
      ).collect() {}
    }
    Log.d(TAG, "updateUser:success")
  }


  fun signInWithEmailAndPassword() = viewModelScope.launch {

    auth.signInWithEmailAndPassword(_userEmail.value, _password.value)
      .addOnCompleteListener { task ->
        if (task.isSuccessful) {
          Log.d(TAG, "signInWithEmail:success")
          val user = auth.currentUser
          if (user != null) {
            user.email?.let { getUserByEmail(it) }
            _user.value.id?.let { Log.d("Logged in", it) }
          }
          _isLoggedIn.value = true
        } else {
          _error.value = task.exception?.message.toString()
          Log.w(TAG, "signUserWithEmail:failure", task.exception)
        }
      }
  }

  fun isValidEmailAndPassword(): Boolean {
    return (!userEmail.value.isBlank() && !password.value.isBlank())
  }


  fun signOut() = viewModelScope.launch {
    auth.signOut()
    _isLoggedIn.value = false
  }


}
