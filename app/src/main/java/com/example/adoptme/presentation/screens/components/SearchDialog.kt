package com.example.adoptme.presentation.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.adoptme.R
import com.example.adoptme.domain.model.enums.Sex
import com.example.adoptme.domain.model.enums.Size
import com.example.adoptme.presentation.PetsViewModel


@Composable
fun SearchDialog(viewModel: PetsViewModel) {
    val size = remember { mutableStateListOf<Size>() }
    val sex = remember { mutableStateListOf<Sex>() }
    val favorite = remember { mutableStateOf(viewModel.filter.value.favorite) }

    size.addAll(viewModel.filter.value.size)
    sex.addAll(viewModel.filter.value.sex)
    viewModel.filter.value.favorite = favorite.value

    Dialog(
        onDismissRequest = {
            viewModel.showAddPet.value = false
        },
        content = {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colors.background
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text(stringResource(R.string.sex))
                    Box(modifier = Modifier.padding(0.dp, 16.dp)) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Sex.values().map {
                                Row {
                                    Checkbox(
                                        checked = sex.contains(it),
                                        onCheckedChange = { checked ->
                                            if (checked) sex.add(it) else sex.remove(
                                                it
                                            )
                                        })
                                    Text(
                                        text = it.value,
                                        modifier = Modifier.padding(start = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                    Text(stringResource(R.string.size))
                    Box(modifier = Modifier.padding(0.dp, 16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Size.values().map {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Checkbox(
                                        checked = size.contains(it),
                                        onCheckedChange = { checked ->
                                            if (checked) size.add(it) else size.remove(
                                                it
                                            )
                                        }
                                    )
                                    Text(
                                        text = it.value,
                                        modifier = Modifier.padding(start = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = favorite.value,
                            onCheckedChange = { favorite.value = it }
                        )
                        Text(
                            text = stringResource(R.string.favorite),
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(32.dp),
                        modifier = Modifier.padding(top = 32.dp)
                    ) {
                        Button(modifier = Modifier.background(
                            MaterialTheme.colors.primary,
                            RoundedCornerShape(8.dp)
                        ),
                            onClick = {
                                viewModel.showSearchDialog.value = false
                                viewModel.setFilterSex(sex)
                                viewModel.setFilterSize(size)
                                viewModel.getPets()
                            }
                        ) {
                            Text(
                                text = stringResource(R.string.search),
                                color = MaterialTheme.colors.onPrimary,
                            )
                        }
                        TextButton(
                            onClick = {
                                viewModel.showSearchDialog.value = false
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
