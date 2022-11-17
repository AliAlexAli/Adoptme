package com.example.adoptme.presentation.screens.components

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.adoptme.R
import com.example.adoptme.domain.model.Pet
import com.example.adoptme.domain.model.Response
import com.example.adoptme.domain.model.enums.Sex
import com.example.adoptme.domain.model.enums.Size
import com.example.adoptme.presentation.AuthViewModel
import com.example.adoptme.presentation.PetsViewModel
import java.util.*

@Composable
fun AddPetDialog(
  viewModel: PetsViewModel,
  authViewModel: AuthViewModel,
  pet: Pet? = null,
  onAdd: () -> Unit
) {
  var name by remember { mutableStateOf("") }
  val birth = remember { mutableStateOf(Calendar.getInstance().time) }
  var sex by remember { mutableStateOf(Sex.MALE) }
  var size by remember { mutableStateOf(Size.SMALL) }
  var description by remember { mutableStateOf("") }
  val image = remember { mutableStateOf(Uri.EMPTY) }
  val imageExt = remember { mutableStateOf("") }
  val focusRequester = remember { FocusRequester() }

  LaunchedEffect(Unit) {
    if (pet != null && viewModel.data.value is Response.Success) {
      pet.name?.let { name = it }
      pet.birth?.let { birth.value = it }
      pet.sex?.let { sex = it }
      pet.size?.let { size = it }
      pet.description?.let { description = it }
    }
  }

  Dialog(
    onDismissRequest = {
      viewModel.showAddPet.value = false
    },
    content = {
      Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colors.background
      ) {
        Column(
          Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
        ) {
          OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = {
              Text(
                text = stringResource(R.string.name)
              )
            },
            modifier = Modifier.focusRequester(focusRequester)
          )
          Spacer(
            modifier = Modifier.height(16.dp)
          )
          Text(stringResource(R.string.sex))
          Row {
            Sex.values().map {
              RadioButton(selected = sex == it, onClick = { sex = it })
              Text(
                text = it.value,
                modifier = Modifier
                  .clickable(onClick = { sex = it })
                  .padding(start = 4.dp)
              )
              Spacer(modifier = Modifier.size(4.dp))
            }
            Spacer(
              modifier = Modifier.height(12.dp)
            )
          }
          Text(stringResource(R.string.size))
          Row {
            Size.values().map {
              RadioButton(
                selected = size == it,
                onClick = { size = it })
              Text(
                text = it.value,
                modifier = Modifier
                  .clickable(onClick = { size = it })
                  .padding(start = 4.dp)
              )
              Spacer(modifier = Modifier.size(4.dp))
            }
          }
          Spacer(
            modifier = Modifier.height(12.dp)
          )

          val context = LocalContext.current

          ShowDatePicker(context = context, date = birth)

          Spacer(
            modifier = Modifier.height(16.dp)
          )
          OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = {
              Text(
                text = stringResource(R.string.description)
              )
            },
            modifier = Modifier.focusRequester(focusRequester)
          )
          Spacer(modifier = Modifier.height(16.dp))
          ImagePicker(context = context, filePath = image, extension = imageExt)
          Spacer(
            modifier = Modifier.height(16.dp)
          )
          DisposableEffect(Unit) {
            onDispose { }
          }
          Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            TextButton(
              onClick = {
                viewModel.showAddPet.value = false
                viewModel.addPet(
                  pet?.id,
                  name,
                  birth.value,
                  sex,
                  size,
                  description,
                  image.value,
                  imageExt.value,
                  authViewModel.user.value.id
                )
                onAdd()
              }
            ) {
              if (pet != null)
                Text(
                  text = stringResource(R.string.edit)
                )
              else Text(
                text = stringResource(R.string.add)
              )

            }
            TextButton(
              onClick = {
                viewModel.showAddPet.value = false
              }
            ) {
              Text(
                text = stringResource(R.string.cancel)
              )
            }
          }
        }
      }
    }
  )
}
