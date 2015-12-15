package com.example.www.controller

import javax.inject.{Inject, Singleton}

import com.example.proto.Proto
import com.example.www.domain
import com.example.www.domain.http.{UserRequest, UserResponse}
import com.example.www.mysql.IdUtil
import com.example.www.service.{TimeService, UserIdService, UserService}
import com.twitter.finatra.http.Controller
import com.twitter.util.Future


@Singleton
class UserController @Inject()(
  userService: UserService,
  userIdService: UserIdService,
  timeService: TimeService
) extends Controller {

  get("/users/:id")(show)

  post("/create-user")(create)

  def show(request: UserRequest.Show): Future[Option[UserResponse.Show]] = {
    userService.find(request.id).map {
      case Some(category) => Some(UserResponse.Show(domain.User.fromProto(category)))
      case _ => None
    }
  }

  def create(request: UserRequest.Create): Future[UserResponse.Create] = {
    for {
      id <- userIdService.create()
      user = Proto.User.newBuilder()
        .setId(id)
        .setCreatedAt(IdUtil.timestamp(id))
        .setUpdatedAt(IdUtil.timestamp(id))
        .setName(request.name)
        .setEmail(request.email)
        .build()
      _ <- userService.create(user)
    } yield UserResponse.Create(domain.User.fromProto(user))
  }
}
