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
  ),
  MyData(
  title = R.string.myData
  );

  companion object {
    fun fromRoute(route: String?, isLoggedIn: State<Boolean>): NavigationEnum {
      return when (route?.substringBefore("/")) {
        Main.name -> Main
        Login.name -> Main
        Register.name -> Main
        Pet.name -> Pet
        MyData.name -> {if(isLoggedIn.value) MyData else Main}
        null -> Main
        else -> throw IllegalArgumentException("Route $route is not recognized.")
      }
    }
  }
}
