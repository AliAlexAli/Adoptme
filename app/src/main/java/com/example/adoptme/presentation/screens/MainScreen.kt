package com.example.adoptme.presentation.screens

import android.annotation.SuppressLint
import android.util.Log
import android.widget.LinearLayout
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
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch


@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun MainScreen(
  navController: NavController,
  viewModel: PetsViewModel,
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

  Scaffold(
    scaffoldState = scaffoldState,
    topBar = {
      if (currentScreen == NavigationEnum.Register
        || currentScreen == NavigationEnum.Login
      ) {
        AuthTopBar(currentScreen, scope, scaffoldState)
      } else
        MainTopBar(currentScreen, scope, scaffoldState)
    },
    drawerContent = {
      if (authViewModel.isLoggedIn.value) {
        MainDrawerContent(
          navController = navController,
          scope = scope,
          scaffoldState = scaffoldState,
          viewModel = authViewModel
        )

      } else {
        AuthDrawerContent(
          navController = navController,
          scope = scope,
          scaffoldState = scaffoldState
        )
      }
    },
    floatingActionButton = { PetFloatingActionButton(viewModel) }
  ) {
    viewModel.getPets()
    when (val r = viewModel.uploadResponse.value) {
      is Response.Success -> Log.d("Response Changed", r.data)
      is Response.Loading -> Log.d("Response Changed", "Loading")
      is Response.Error -> Log.d("Response Changed", r.message)
    }
    PetsList(navController, viewModel = viewModel, authViewModel)
  }
}

@OptIn(InternalCoroutinesApi::class)
@Composable
fun PetsList(navController: NavController, viewModel: PetsViewModel, authViewModel: AuthViewModel) {
  when (val petsResponse = viewModel.data.value) {
    is Response.Loading -> CircularProgressIndicator()
    is Response.Success -> {
      SwipeRefresh(
        modifier = Modifier.fillMaxSize(),
        state = rememberSwipeRefreshState(isRefreshing = false),
        onRefresh = { viewModel.getPets() }) {
        if (petsResponse.data != null)
          LazyColumn(Modifier.padding(all = 10.dp)) {
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
        AddPetDialog(viewModel = viewModel)
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
      .height(280.dp),
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
          Text(text = sex, color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f))

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
                viewModel.getPetById(id)
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

@Composable
fun CircularProgressBar(
  isDisplayed: Boolean
) {
  if (isDisplayed) {
    CircularProgressIndicator()
  }
}
