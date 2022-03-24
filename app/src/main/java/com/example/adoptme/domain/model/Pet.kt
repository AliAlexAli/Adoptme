package com.example.adoptme.domain.model

import java.util.*
import javax.inject.Inject

class Pet @Inject constructor(
  val id: String?,
  val name: String?,
  val birth: Date?,
  val sex: String?,
  val size: String?,
  val description: String?,
  val image: String?,
  val date: Date? = Calendar.getInstance().time
) {
  constructor() : this(null, null, null, null, null, null, null)
}
