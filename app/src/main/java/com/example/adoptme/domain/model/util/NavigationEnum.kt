package com.example.adoptme.domain.model.util

import androidx.compose.runtime.State
import com.example.adoptme.R

enum class NavigationEnum(val title: Int) {
  Login(
    title = R.string.login
  ),
  Register(
    title = R.string.register
  ),
  Main(
    title = R.string.main
  ),
  Pet(
    title = R.string.pet
  );

  companion object {
    fun fromRoute(route: String?, isLoggedIn: State<Boolean>): NavigationEnum {
      return when (route?.substringBefore("/")) {
        Main.name -> Main
        Login.name -> Main
        Register.name -> Main
        Pet.name -> Pet
        null -> Main
        else -> throw IllegalArgumentException("Route $route is not recognized.")
      }
    }
  }
}
