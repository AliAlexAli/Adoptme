package com.example.adoptme.presentation

import android.app.Application
import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "AuthViewModel"

@HiltViewModel
class AuthViewModel @Inject constructor(application: Application) : ViewModel() {

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

  init {
    _isLoggedIn.value = false
  }

  fun createUserWithEmailAndPassword() = viewModelScope.launch {

    auth.createUserWithEmailAndPassword(_userEmail.value, _password.value)
      .addOnCompleteListener { task ->
        if (task.isSuccessful) {
          // Sign in success, update UI with the signed-in user's information
          Log.d(TAG, "createUserWithEmail:success")
          val user = auth.currentUser
          _isLoggedIn.value = true
        } else {
          // If sign in fails, display a message to the user.
          _error.value = task.exception?.localizedMessage ?: "Ismeretlen hiba!"
          Log.w(TAG, "createUserWithEmail:failure", task.exception)
        }
      }
  }

  fun signInWithEmailAndPassword() = viewModelScope.launch {

    auth.signInWithEmailAndPassword(_userEmail.value, _password.value)
      .addOnCompleteListener { task ->
        if (task.isSuccessful) {
          // Sign in success, update UI with the signed-in user's information
          Log.d(TAG, "signInWithEmail:success")
          val user = auth.currentUser
          _isLoggedIn.value = true
        } else {
          // If sign in fails, display a message to the user.
          Log.w(TAG, "createUserWithEmail:failure", task.exception)
          _error.value = task.exception?.localizedMessage ?: "Ismeretlen hiba!"
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
