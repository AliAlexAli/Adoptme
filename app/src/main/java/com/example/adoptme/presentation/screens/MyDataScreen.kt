package com.example.adoptme.presentation.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.navigation.NavController
import com.example.adoptme.R
import com.example.adoptme.domain.model.util.NavigationEnum
import com.example.adoptme.presentation.AuthViewModel
import com.example.adoptme.presentation.PetsViewModel
import com.example.adoptme.presentation.screens.components.AuthDrawerContent
import com.example.adoptme.presentation.screens.components.MainDrawerContent
import com.example.adoptme.presentation.screens.components.MainTopBar
import com.example.adoptme.presentation.screens.components.UpdateUserButton
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun MyDataScreen(navController: NavController, authViewModel: AuthViewModel, petsViewModel: PetsViewModel) {
  val focusManager = LocalFocusManager.current

  val scaffoldState = rememberScaffoldState()
  val scope = rememberCoroutineScope()

  if(!authViewModel.isLoggedIn.value) LaunchedEffect(Unit){ navController.navigate(NavigationEnum.Main.name)}

  if (authViewModel.error.value.isNotBlank()) scope.launch {
    scaffoldState.snackbarHostState.showSnackbar(
      authViewModel.error.value
    )
  }

  authViewModel.user.value.name?.let { authViewModel.setName(it) }
  authViewModel.user.value.website?.let { authViewModel.setWebsite(it) }
  authViewModel.user.value.address?.let { authViewModel.setAddress(it) }
  authViewModel.user.value.phone?.let { authViewModel.setPhone(it) }

  Scaffold(
    scaffoldState = scaffoldState,
    topBar = {
      MainTopBar(scope, scaffoldState, petsViewModel)
    },
    drawerContent = {
      if (authViewModel.isLoggedIn.value) {
        MainDrawerContent(
          navController,
          scope,
          scaffoldState,
          authViewModel,
          petsViewModel
        )

      } else {
        AuthDrawerContent(
          navController = navController,
          scope = scope,
          scaffoldState = scaffoldState
        )
      }
    }
  ) {

    Column(
      modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colors.background),
      verticalArrangement = Arrangement.SpaceEvenly,
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      OutlinedTextField(
        value = authViewModel.name.value,
        label = { Text(text = stringResource(R.string.ownerName)) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
          keyboardType = KeyboardType.Text,
          imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
          onNext = {
            focusManager.moveFocus(FocusDirection.Down)
          }),
        onValueChange = { authViewModel.setName(it) }
      )
      OutlinedTextField(
        value = authViewModel.phone.value,
        label = { Text(text = stringResource(R.string.ownerPhone)) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
          keyboardType = KeyboardType.Phone,
          imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
          onNext = {
            focusManager.moveFocus(FocusDirection.Down)
          }),
        onValueChange = { authViewModel.setPhone(it) }
      )
      OutlinedTextField(
        value = authViewModel.address.value,
        label = { Text(text = stringResource(R.string.ownerAddress)) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
          keyboardType = KeyboardType.Text,
          imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
          onNext = {
            focusManager.moveFocus(FocusDirection.Down)
          }),
        onValueChange = { authViewModel.setAddress(it) }
      )
      OutlinedTextField(
        value = authViewModel.website.value,
        label = { Text(text = stringResource(R.string.ownerSite)) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
          keyboardType = KeyboardType.Text,
          imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
          onNext = {
            focusManager.moveFocus(FocusDirection.Down)
          }),
        onValueChange = { authViewModel.setWebsite(it) }
      )
      UpdateUserButton(authViewModel)
    }
  }
}
