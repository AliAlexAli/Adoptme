package com.example.adoptme.presentation.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Phone
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import coil.compose.rememberImagePainter
import com.example.adoptme.R
import com.example.adoptme.domain.model.Response
import com.example.adoptme.presentation.AuthViewModel
import com.example.adoptme.presentation.PetsViewModel
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.launch
import java.util.*

val android.content.Context.dataStore by preferencesDataStore("favorites")

@OptIn(ExperimentalComposeUiApi::class,
  com.google.accompanist.permissions.ExperimentalPermissionsApi::class
)
@Composable
fun PetScreen(petsViewModel: PetsViewModel, authViewModel: AuthViewModel) {

  val context = LocalContext.current

  val coroutineScope = rememberCoroutineScope()
  val scrollState = rememberScrollState()
  val favorite = remember { mutableStateOf(false) }

  val callPermissionState = rememberPermissionState(
    android.Manifest.permission.CALL_PHONE
  )

  when (val petsResponse = petsViewModel.data.value) {
    is Response.Loading -> {
      CircularProgressIndicator()
    }
    is Response.Success -> {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .verticalScroll(scrollState)
          .background(MaterialTheme.colors.background)
      ) {
        val pet = petsResponse.data.first()
        pet.id?.let {
          LaunchedEffect(Unit) {
            favorite.value = isFavorite(it, context)
          }
        }
        var fav = Icons.Default.FavoriteBorder
        if (favorite.value) fav = Icons.Default.Favorite

        Box(Modifier.fillMaxSize()) {
          Image(
            painter = rememberImagePainter(data = pet.image, builder = {
              crossfade(true)
              placeholder(R.drawable.dogplaceholder)
            }),
            modifier = Modifier
              .fillMaxWidth()
              .aspectRatio(1f),
            contentDescription = "Dog profile picture",
            contentScale = ContentScale.Crop
          )

          IconButton(modifier = Modifier
            .align(Alignment.TopEnd)
            .padding(12.dp),
            onClick = {
              pet.id?.let {
                coroutineScope.launch {
                  favorite.value = toggleFavorite(it, context)
                }
              }
            }) {
            Icon(
              modifier = Modifier.size(64.dp),
              imageVector = fav,
              contentDescription = "Toggle Favorite",
              tint = MaterialTheme.colors.primary
            )


          }

          IconButton(modifier = Modifier
            .align(Alignment.BottomEnd)
            .offset(x = (-25).dp, y = 37.dp)
            .size(74.dp)
            .background(MaterialTheme.colors.secondary, RoundedCornerShape(50)),
            onClick = {
              when(callPermissionState.status){
                is PermissionStatus.Denied -> {
                  callPermissionState.launchPermissionRequest()
                }
                is PermissionStatus.Granted -> {
                  context.startActivity(Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "+36206197550")))
                }
              }
               }) {
            Icon(
              modifier = Modifier
                .padding(12.dp)
                .fillMaxSize(),
              imageVector = Icons.Default.Phone,
              contentDescription = "Call button",
              tint = MaterialTheme.colors.primary
            )
          }

          IconButton(modifier = Modifier
            .align(Alignment.BottomEnd)
            .offset(x = (-105).dp, y = 32.dp)
            .size(54.dp)
            .background(MaterialTheme.colors.primaryVariant, RoundedCornerShape(50)),
            onClick = {
              val selectorIntent = Intent(Intent.ACTION_SENDTO)
              selectorIntent.data = Uri.parse("mailto:")

              val emailIntent = Intent(Intent.ACTION_SEND)
              emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("alishaltoutali@gmail.com"))
              emailIntent.putExtra(Intent.EXTRA_SUBJECT, "The subject")
              emailIntent.putExtra(Intent.EXTRA_TEXT, "The email body")
              emailIntent.selector = selectorIntent
              context.startActivity(Intent.createChooser(emailIntent, "Send email..."))
            }) {
            Icon(
              modifier = Modifier
                .padding(12.dp)
                .fillMaxSize(),
              imageVector = Icons.Default.Email,
              contentDescription = "Email button",
              tint = MaterialTheme.colors.primary
            )
          }

        }
        Column(
          modifier = Modifier
            .padding(all = 16.dp)
        ) {

          pet.name?.let {
            Text(
              text = it,
              fontSize = 32.sp,
              color = MaterialTheme.colors.onBackground
            )
          }
          Row(
            modifier = Modifier
              .padding(vertical = 16.dp)
              .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
          ) {
            pet.birth?.let {
              val birthDate = Calendar.getInstance()
              birthDate.time = it
              var age = Calendar
                .getInstance()
                .get(Calendar.YEAR) - birthDate.get(Calendar.YEAR)
              if (Calendar
                  .getInstance()
                  .get(Calendar.DAY_OF_YEAR) < birthDate.get(Calendar.DAY_OF_YEAR)
              ) age--
              petDataCard(label = "Kor", text = "${age} Éves")
            }
            pet.sex?.let { petDataCard(label = "Nem", text = "${it}") }
            pet.size?.let { petDataCard(label = "Méret", text = it) }
          }
          pet.description?.let { Text(text = it, color = MaterialTheme.colors.onBackground) }
        }
      }
    }
  }
}

@Composable
fun petDataCard(label: String, text: String) {
  Card(
    elevation = 3.dp,
    shape = RoundedCornerShape(10)
  ) {
    Column(
      modifier = Modifier
        .padding(12.dp),
      verticalArrangement = Arrangement.SpaceEvenly,
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(text = label, modifier = Modifier.alpha(alpha = 0.6f), maxLines = 1)
      Text(text = text, fontSize = 20.sp, fontWeight = FontWeight.Bold, maxLines = 1)
    }
  }
}

suspend fun toggleFavorite(id: String, context: Context): Boolean {
  val ID = intPreferencesKey(id)
  var ret = false
  context.dataStore.edit { favorites ->
    if (favorites[ID] == 1) {
      ret = false
      favorites[ID] = 0
    } else {
      ret = true
      favorites[ID] = 1;
    }
  }
  Log.d("toggledFavorite", id.plus(ret))
  return ret
}

suspend fun isFavorite(id: String, context: Context): Boolean {
  val ID = intPreferencesKey(id)
  var ret = false
  context.dataStore.edit { favorites ->
    ret = favorites[ID] == 1
  }
  Log.d("isFavorite", id.plus(ret))
  return ret
}
