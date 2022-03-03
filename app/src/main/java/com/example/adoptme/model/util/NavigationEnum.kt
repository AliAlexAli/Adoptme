package com.example.adoptme.model.util

import androidx.compose.runtime.State
import com.example.adoptme.R

enum class NavigationEnum (val title: Int) {
  Login(
    title = R.string.login
  ),
  Register(
    title = R.string.register
  ),
  Welcome(
    title = R.string.welcome
  );

  companion object {
    fun fromRoute(route: String?, isLoggedIn: State<Boolean>): NavigationEnum {
      return if (!isLoggedIn.value) {
        when (route?.substringBefore("/")) {
          Login.name -> Login
          Register.name -> Register
          else -> Login // Redirects to Login if some other page, but not logged in
        }
      } else {
        // Define here all your logged in routings
        when (route?.substringBefore("/")) {
          Welcome.name -> Welcome
          Login.name -> Welcome
          Register.name -> Welcome
          null -> Welcome
          else -> throw IllegalArgumentException("Route $route is not recognized.")
        }
      }
    }
  }
}
