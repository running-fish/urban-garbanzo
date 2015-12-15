package com.example.www.domain.http

import com.twitter.finatra.request.QueryParam
import com.twitter.finatra.validation.Size


object UserRequest {

  case class Show(@QueryParam id: Long)

  case class Create(
    @Size(min = 1, max = 64) name: String,
    @Size(min = 1, max = 64) email: String
  )

}
