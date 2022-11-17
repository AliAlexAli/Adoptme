package com.example.adoptme.domain.model

import javax.inject.Inject

class Owner @Inject constructor(
  val id: String?,
  val name: String?,
  val address: String?,
  val email: String?,
  val phone: String?,
  val website: String?
) {
  constructor() : this(null, null, null, null, null, null)
}
