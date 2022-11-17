package com.example.adoptme.presentation.screens.components

import android.app.DatePickerDialog
import android.content.Context
import android.widget.DatePicker
import androidx.compose.foundation.clickable
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.adoptme.R
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ShowDatePicker(context: Context, date: MutableState<Date>) {
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
        text = stringResource(R.string.birth_date)
      )
    },
    enabled = false,
    modifier = Modifier
      .clickable { datePickerDialog.show() }
  )
}
