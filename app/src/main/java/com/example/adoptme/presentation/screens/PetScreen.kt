package com.example.adoptme.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.example.adoptme.R
import com.example.adoptme.domain.model.Response
import com.example.adoptme.presentation.AuthViewModel
import com.example.adoptme.presentation.PetsViewModel
import java.util.*

@Composable
fun PetScreen(petsViewModel: PetsViewModel, authViewModel: AuthViewModel) {

  when (val petsResponse = petsViewModel.data.value) {
    is Response.Success -> petPreview(petsViewModel = petsViewModel)
  }
}

@Composable
fun petPreview(petsViewModel: PetsViewModel) {

  when (val petsResponse = petsViewModel.data.value) {
    is Response.Success ->
      Column(
        modifier = Modifier
          .fillMaxSize()
          .verticalScroll(rememberScrollState())
      ) {
        val pet = petsResponse.data.first()
        Image(
          painter = rememberImagePainter(data = pet.image, builder = {
            crossfade(false)
            placeholder(R.drawable.dogplaceholder)
          }),
          modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f),
          contentDescription = "Dog profile picture",
          contentScale = ContentScale.Crop
        )

        Column(
          modifier = Modifier
            .padding(16.dp)
        ) {

          pet.name?.let { Text(text = it, fontSize = 32.sp) }
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
          pet.description?.let { Text(text = it) }
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
