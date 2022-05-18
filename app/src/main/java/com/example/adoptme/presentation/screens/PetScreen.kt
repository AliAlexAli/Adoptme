package com.example.adoptme.presentation.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

val android.content.Context.dataStore by preferencesDataStore("favorites")

@OptIn(
  ExperimentalComposeUiApi::class,
  com.google.accompanist.permissions.ExperimentalPermissionsApi::class
)
@Composable
fun PetScreen(petsViewModel: PetsViewModel, authViewModel: AuthViewModel) {

  val context = LocalContext.current

  val coroutineScope = rememberCoroutineScope()
  val scrollState = rememberScrollState()
  val favorite = remember { mutableStateOf(false) }

  when (val petsResponse = petsViewModel.data.value) {
    is Response.Loading -> {
      LaunchedEffect(Unit) {
        while(true) {
          delay(1000)
          petsViewModel.getPets()
        }
      }
    }
    is Response.Success -> {
      when (val owner = petsViewModel.ownerData.value) {
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
                  context.startActivity(
                    Intent(
                      Intent.ACTION_DIAL,
                      Uri.parse("tel:" + owner.data.phone)
                    )
                  )
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
                  emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(owner.data.email))
                  emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Örökbefogadási kérelem: " + pet.id)
                  emailIntent.putExtra(Intent.EXTRA_TEXT, "")
                  emailIntent.selector = selectorIntent
                  context.startActivity(Intent.createChooser(emailIntent, "Email küldés..."))
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
        is Response.Loading -> {
          Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
          ) {
            CircularProgressIndicator()
          }
          LaunchedEffect(Unit) {
            while(true) {
              delay(1000)
              petsResponse.data.first().owner?.let { petsViewModel.getOwnerById(it) }
            }
          }
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
