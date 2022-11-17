package com.example.adoptme.presentation.screens

import android.annotation.SuppressLint
import android.content.res.Resources
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.rememberImagePainter
import com.example.adoptme.R
import com.example.adoptme.domain.model.Pet
import com.example.adoptme.domain.model.Response
import com.example.adoptme.domain.model.util.NavigationEnum
import com.example.adoptme.presentation.AuthViewModel
import com.example.adoptme.presentation.PetsViewModel
import com.example.adoptme.presentation.screens.components.*
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun MainScreen(
  navController: NavController,
  petsViewModel: PetsViewModel,
  authViewModel: AuthViewModel
) {
  val backstackEntry = navController.currentBackStackEntryAsState()

  val scaffoldState = rememberScaffoldState()
  val scope = rememberCoroutineScope()
  val currentScreen =
    NavigationEnum.fromRoute(backstackEntry.value?.destination?.route, authViewModel.isLoggedIn)

  if (authViewModel.error.value.isNotBlank()) scope.launch {
    scaffoldState.snackbarHostState.showSnackbar(
      authViewModel.error.value
    )
  }

  when (val petsResponse = petsViewModel.data.value) {
    is Response.Error -> scope.launch {
      scaffoldState.snackbarHostState.showSnackbar(
        petsResponse.message
      )
    }
  }

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
    },
    floatingActionButton = {
      if (authViewModel.isLoggedIn.value) {
        AddPetFab(petsViewModel)
      }
    }
  ) {
    PetsList(navController, viewModel = petsViewModel, authViewModel)
  }
}


@OptIn(InternalCoroutinesApi::class)
@Composable
fun PetsList(navController: NavController, viewModel: PetsViewModel, authViewModel: AuthViewModel) {
  when (val petsResponse = viewModel.data.value) {
    is Response.Loading -> SwipeRefresh(
      modifier = Modifier.fillMaxSize(),
      state = rememberSwipeRefreshState(isRefreshing = true),
      onRefresh = { viewModel.getPets() }) {}
    is Response.Success -> {
      SwipeRefresh(
        modifier = Modifier.fillMaxSize(),
        state = rememberSwipeRefreshState(isRefreshing = false),
        onRefresh = { viewModel.getPets() }) {
        if (petsResponse.data.isEmpty())
          Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = stringResource(R.string.not_found))
          }
        else LazyColumn(Modifier.padding(all = 10.dp)) {
          items(items = petsResponse.data) { pet ->
            PetCard(
              pet = pet,
              viewModel = viewModel,
              authViewModel = authViewModel,
              navController = navController
            )
          }
        }
      }
      if (viewModel.showAddPet.value) {
        AddPetDialog(viewModel = viewModel, authViewModel = authViewModel, onAdd = {
          navController.navigate(NavigationEnum.Main.name)
        })
      }
      if (viewModel.showSearchDialog.value) {
        SearchDialog(viewModel = viewModel)
      }
    }

    is Error -> petsResponse.message?.let {
      Text(
        text = it,
        modifier = Modifier.padding(16.dp)
      )
    }
  }
}

@Composable
@InternalCoroutinesApi
fun PetCard(
  pet: Pet,
  viewModel: PetsViewModel,
  authViewModel: AuthViewModel,
  navController: NavController
) {
  Card(
    modifier = Modifier
      .padding(all = 10.dp)
      .height(280.dp).testTag(stringResource(R.string.test_pet_card)),
    elevation = 3.dp,
    shape = RoundedCornerShape(10)
  ) {
    Row {
      Image(
        painter = rememberImagePainter(data = pet.image, builder = {
          crossfade(false)
          placeholder(R.drawable.dogplaceholder)
        }),
        contentDescription = "Dog profile picture",
        Modifier
          .fillMaxWidth(0.5f)
          .fillMaxHeight(),
        contentScale = ContentScale.Crop,
        alignment = Alignment.Center
      )
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(all = 10.dp),
        verticalArrangement = Arrangement.SpaceEvenly
      ) {
        pet.name?.let { name ->
          Text(
            text = name,
            fontSize = 20.sp
          )
        }

        pet.sex?.let { sex ->
          Text(text = sex.value, color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f))

          pet.description?.let { desc ->
            Text(
              modifier = Modifier.padding(vertical = 12.dp),
              text = desc,
              overflow = TextOverflow.Ellipsis,
              maxLines = 6,
              color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
            )
          }

          pet.id?.let { id ->
            TextButton(
              modifier = Modifier
                .clip(RoundedCornerShape(20))
                .background(MaterialTheme.colors.primary),
              onClick = {
                authViewModel.setpetId(id)
                navController.navigate(NavigationEnum.Pet.name)

              }) {
              Text(
                text = stringResource(id = R.string.petMoreButton),
                color = MaterialTheme.colors.background
              )
            }
          }
        }
      }
    }
  }
}
