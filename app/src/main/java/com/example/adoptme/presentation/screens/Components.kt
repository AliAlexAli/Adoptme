package com.example.adoptme.presentation.screens

import android.app.DatePickerDialog
import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import android.widget.DatePicker
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.adoptme.R
import com.example.adoptme.domain.model.util.NavigationEnum
import com.example.adoptme.presentation.AuthViewModel
import com.example.adoptme.presentation.PetsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


@Composable
fun EmailField(focusManager: FocusManager, viewModel: AuthViewModel) {
  val userEmail = viewModel.userEmail.value

  OutlinedTextField(
    value = userEmail,
    label = { Text(text = stringResource(id = R.string.email)) },
    singleLine = true,
    keyboardOptions = KeyboardOptions(
      keyboardType = KeyboardType.Email,
      imeAction = ImeAction.Next
    ),
    keyboardActions = KeyboardActions(
      onNext = {
        focusManager.moveFocus(FocusDirection.Down)
      }),
    onValueChange = { viewModel.setUserEmail(it) }
  )
}

@Composable
fun PasswordField(focusManager: FocusManager, viewModel: AuthViewModel) {
  val password = viewModel.password.value
  var showPassword by remember { mutableStateOf(false) }

  OutlinedTextField(
    value = password,
    label = { Text(text = stringResource(id = R.string.password)) },
    singleLine = true,
    onValueChange = { viewModel.setPassword(it) },

    keyboardOptions = KeyboardOptions(
      keyboardType = KeyboardType.Password,
      imeAction = ImeAction.Done
    ),
    keyboardActions = KeyboardActions(
      onDone = {
        focusManager.clearFocus()
      }
    ),
    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
    trailingIcon = {
      val image = if (showPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff

      IconButton(onClick = { showPassword = !showPassword }) {
        Icon(imageVector = image, contentDescription = "Toggle password visibility icon")
      }
    }

  )
}

@Composable
fun ButtonEmailPasswordCreate(viewModel: AuthViewModel) {
  Button(
    enabled = viewModel.isValidEmailAndPassword(),
    content = { Text(text = stringResource(id = R.string.register)) },
    onClick = {
      viewModel.createUserWithEmailAndPassword()

    }
  )
}

@Composable
fun ButtonEmailPasswordLogin(viewModel: AuthViewModel) {
  Button(
    enabled = viewModel.isValidEmailAndPassword(),
    content = { Text(text = stringResource(R.string.login)) },
    onClick = { viewModel.signInWithEmailAndPassword() })
}

@Composable
fun AuthTopBar(
  currentScreen: NavigationEnum, scope: CoroutineScope, scaffoldState: ScaffoldState
) {
  TopAppBar(
    title = { Text(text = stringResource(currentScreen.title)) },

    navigationIcon = {
      IconButton(onClick = {
        scope.launch {
          scaffoldState.drawerState.open()
        }
      }) {
        Icon(Icons.Default.Menu, "Menu Icon")
      }
    }
  )
}

@Composable
fun MainTopBar(
  scope: CoroutineScope,
  scaffoldState: ScaffoldState,
  viewModel: PetsViewModel
) {
  TopAppBar(
    title = { Text(text = "") },
    navigationIcon = {
      IconButton(onClick = {
        scope.launch {
          scaffoldState.drawerState.open()
        }
      }) {
        Icon(Icons.Default.Menu, "Menu Icon")
      }
    },
    actions = {
      IconButton(onClick = { viewModel.showSearchDialog.value = true }) {
        Icon(Icons.Default.Search, "Search Icon")
      }
    }
  )
}

@Composable
fun AuthDrawerContent(
  navController: NavController,
  scope: CoroutineScope,
  scaffoldState: ScaffoldState
) {
  Column(
    Modifier
      .padding(20.dp)
      .fillMaxSize(), Arrangement.spacedBy(20.dp)
  ) {
    TextButton(modifier = Modifier.fillMaxWidth(),
      onClick = {
        navController.navigate(NavigationEnum.Register.name)
        scope.launch {
          scaffoldState.drawerState.close()
        }
      }) {
      Text(
        text = stringResource(id = R.string.register),
        textAlign = TextAlign.Start,
        modifier = Modifier.fillMaxWidth()
      )
    }
    TextButton(modifier = Modifier.fillMaxWidth(), onClick = {
      navController.navigate(NavigationEnum.Login.name)
      scope.launch {
        scaffoldState.drawerState.close()
      }
    }) {
      Text(
        text = stringResource(id = R.string.login),
        textAlign = TextAlign.Start,
        modifier = Modifier.fillMaxWidth()
      )
    }
  }
}

@Composable
fun MainDrawerContent(
  navController: NavController,
  scope: CoroutineScope,
  scaffoldState: ScaffoldState,
  viewModel: AuthViewModel
) {
  Column(
    Modifier
      .padding(20.dp)
      .fillMaxSize(), Arrangement.spacedBy(20.dp)
  ) {
    TextButton(modifier = Modifier.fillMaxWidth(),
      onClick = {
        viewModel.signOut()
        navController.navigate(NavigationEnum.Login.name)
        scope.launch {
          scaffoldState.drawerState.close()
        }
      }) {
      Text(
        text = stringResource(id = R.string.exit),
        textAlign = TextAlign.Start,
        modifier = Modifier.fillMaxWidth()
      )
    }
  }
}

@Composable
fun PetFloatingActionButton(viewModel: PetsViewModel) {
  FloatingActionButton(
    onClick = {
      viewModel.showAddPet.value = true
    }
  ) {
    Icon(
      imageVector = Icons.Default.Add,
      contentDescription = "Add new pet"
    )
  }
}

@Composable
fun SearchDialog(viewModel: PetsViewModel) {
  var size by remember { mutableStateOf("") }
  var sex by remember { mutableStateOf("") }
  val focusRequester = FocusRequester()

  AlertDialog(
    onDismissRequest = {
      viewModel.showAddPet.value = false
    },
    text = {
      Column {
        Text("Nem")
        Row {
          RadioButton(selected = sex == "Kan", onClick = { sex = "Kan" })
          Text(
            text = "Kan",
            modifier = Modifier
              .clickable(onClick = { sex = "Kan" })
              .padding(start = 4.dp)
          )
          Spacer(modifier = Modifier.size(4.dp))
          RadioButton(selected = sex == "Szuka", onClick = { sex = "Szuka" })
          Text(
            text = "Szuka",
            modifier = Modifier
              .clickable(onClick = { sex = "Szuka" })
              .padding(start = 4.dp)
          )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text("Méret")
        Row {
          RadioButton(selected = size == "Kicsi", onClick = { size = "Kicsi" })
          Text(
            text = "Kicsi",
            modifier = Modifier
              .clickable(onClick = { size = "Kicsi" })
              .padding(start = 4.dp)
          )
          Spacer(modifier = Modifier.size(4.dp))
          RadioButton(selected = size == "Közepes", onClick = { size = "Közepes" })
          Text(
            text = "Közepes",
            modifier = Modifier
              .clickable(onClick = { size = "Közepes" })
              .padding(start = 4.dp)
          )
          Spacer(modifier = Modifier.size(4.dp))
          RadioButton(selected = size == "Nagy", onClick = { size = "Nagy" })
          Text(
            text = "Nagy",
            modifier = Modifier
              .clickable(onClick = { size = "Nagy" })
              .padding(start = 4.dp)
          )
        }
      }
    },

    confirmButton = {
      TextButton(
        onClick = {
          viewModel.showSearchDialog.value = false
          viewModel.getPets(sex, size)
        }
      ) {
        Text(
          text = "Keresés"
        )
      }
    },
    dismissButton = {
      TextButton(
        onClick = {
          viewModel.showSearchDialog.value = false
        }
      ) {
        Text(
          text = "Mégse"
        )
      }
    }

  )
}

@Composable
fun AddPetDialog(viewModel: PetsViewModel, authViewModel: AuthViewModel) {
  var name by rememberSaveable { mutableStateOf("") }
  var birth = rememberSaveable { mutableStateOf(Calendar.getInstance().time) }
  var sex by rememberSaveable { mutableStateOf("") }
  var size by rememberSaveable { mutableStateOf("") }
  var description by rememberSaveable { mutableStateOf("") }
  var image = rememberSaveable { mutableStateOf(Uri.EMPTY) }
  var imageExt = rememberSaveable { mutableStateOf("") }
  val focusRequester = remember { FocusRequester() }
  AlertDialog(
    onDismissRequest = {
      viewModel.showAddPet.value = false
    },
    text = {
      Column(
        modifier = Modifier.verticalScroll(rememberScrollState())
      ) {
        OutlinedTextField(
          value = name,
          onValueChange = { name = it },
          label = {
            Text(
              text = "Név"
            )
          },
          modifier = Modifier.focusRequester(focusRequester)
        )
        Spacer(
          modifier = Modifier.height(16.dp)
        )
        Text("Nem")
        Row {
          RadioButton(selected = sex == "Kan", onClick = { sex = "Kan" })
          Text(
            text = "Kan",
            modifier = Modifier
              .clickable(onClick = { sex = "Kan" })
              .padding(start = 4.dp)
          )
          Spacer(modifier = Modifier.size(4.dp))
          RadioButton(selected = sex == "Szuka", onClick = { sex = "Szuka" })
          Text(
            text = "Szuka",
            modifier = Modifier
              .clickable(onClick = { sex = "Szuka" })
              .padding(start = 4.dp)
          )
        }
        Spacer(
          modifier = Modifier.height(16.dp)
        )
        Text("Méret")
        Row {
          RadioButton(selected = size == "Kicsi", onClick = { size = "Kicsi" })
          Text(
            text = "Kicsi",
            modifier = Modifier
              .clickable(onClick = { size = "Kicsi" })
              .padding(start = 4.dp)
          )
          Spacer(modifier = Modifier.size(4.dp))
          RadioButton(selected = size == "Közepes", onClick = { size = "Közepes" })
          Text(
            text = "Közepes",
            modifier = Modifier
              .clickable(onClick = { size = "Közepes" })
              .padding(start = 4.dp)
          )
          Spacer(modifier = Modifier.size(4.dp))
          RadioButton(selected = size == "Nagy", onClick = { size = "Nagy" })
          Text(
            text = "Nagy",
            modifier = Modifier
              .clickable(onClick = { size = "Nagy" })
              .padding(start = 4.dp)
          )
        }
        Spacer(
          modifier = Modifier.height(16.dp)
        )


        val context = LocalContext.current
        val showDateDialog = remember { mutableStateOf(false) }

        showDatePicker(context = context, date = birth)

        Spacer(
          modifier = Modifier.height(16.dp)
        )
        OutlinedTextField(
          value = description,
          onValueChange = { description = it },
          label = {
            Text(
              text = "Leírás"
            )
          },
          modifier = Modifier
            .focusRequester(focusRequester)
        )
        Spacer(
          modifier = Modifier.height(16.dp)
        )
        ImagePicker(context = context, filePath = image, extension = imageExt)
        Spacer(
          modifier = Modifier.height(16.dp)
        )
        DisposableEffect(Unit) {
          onDispose { }
        }
      }
    },
    confirmButton = {
      TextButton(
        onClick = {
          viewModel.showAddPet.value = false
          viewModel.addPet(
            name,
            birth.value,
            sex,
            size,
            description,
            image.value,
            imageExt.value,
            authViewModel.user.value.id
          )
        }
      ) {
        Text(
          text = "Hozzáadás"
        )
      }
    },
    dismissButton = {
      TextButton(
        onClick = {
          viewModel.showAddPet.value = false
        }
      ) {
        Text(
          text = "Mégse"
        )
      }
    }
  )
}

@Composable
fun showDatePicker(context: Context, date: MutableState<Date>) {
  val year: Int
  val month: Int
  val day: Int

  val calendar = Calendar.getInstance()

  year = calendar.get(Calendar.YEAR)
  month = calendar.get(Calendar.MONTH)
  day = calendar.get(Calendar.DAY_OF_MONTH)
  calendar.time = Date()

  val datePickerDialog = DatePickerDialog(
    context,
    { _: DatePicker, year: Int, month: Int, day: Int ->
      calendar.set(year, month, day)
      date.value = calendar.time
    },
    year,
    month,
    day
  )

  OutlinedTextField(
    value = SimpleDateFormat("yyyy-MM-dd").format(date.value),
    onValueChange = {},
    label = {
      Text(
        text = "Születési dátum"
      )
    },
    enabled = false,
    modifier = Modifier
      .clickable { datePickerDialog.show() }
  )
}

@Composable
fun ImagePicker(context: Context, filePath: MutableState<Uri>, extension: MutableState<String>) {

  val launcher = rememberLauncherForActivityResult(
    contract =
    ActivityResultContracts.GetContent()
  ) {
    if (it != null) {
      filePath.value = it
      extension.value = MimeTypeMap.getSingleton().getExtensionFromMimeType(context.contentResolver.getType(filePath.value))!!
    }
  }
  Button(onClick = {
    launcher.launch("image/*")
  }) {
    Text(text = "Kép feltöltése")
  }
}
