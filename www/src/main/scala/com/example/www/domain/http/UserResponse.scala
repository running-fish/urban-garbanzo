package com.example.www.domain.http

import com.example.www.domain


object UserResponse {

  case class Show(user: domain.User)

  case class Create(user: domain.User)

}
