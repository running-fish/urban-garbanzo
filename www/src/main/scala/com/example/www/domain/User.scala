package com.example.www.domain

import com.example.proto.Proto
import org.joda.time.DateTime


object User {
  def fromProto(proto: Proto.User): User = {
    User(
      proto.getId,
      new DateTime(proto.getCreatedAt),
      new DateTime(proto.getUpdatedAt),
      proto.getName,
      proto.getEmail
    )
  }
}

case class User(
  id: Long,
  createdAt: DateTime,
  updatedAt: DateTime,
  name: String,
  email: String
)
