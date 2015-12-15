package com.example.www.service

import javax.inject.{Inject, Singleton}

import com.example.proto.Proto
import com.example.www.mysql.MysqlUtil
import com.twitter.finagle.exp.mysql.Client
import com.twitter.util.Future


@Singleton
class UserService @Inject()(mysql: Client) {
  private[this] val findQuery = "SELECT bytes FROM user WHERE id = ?"
  private[this] val createQuery = "INSERT INTO user (id, bytes, email) VALUES (?, ?, ?)"

  def find(id: Long): Future[Option[Proto.User]] = {
    mysql.prepare(findQuery)
      .select(id)(MysqlUtil.bytes(_, "bytes"))
      .map(_.headOption.map(Proto.User.parseFrom))
  }

  def create(user: Proto.User): Future[Proto.User] = {
    mysql.prepare(createQuery)(
      user.getId,
      user.toByteArray,
      user.getEmail
    ).map(MysqlUtil.value(user))
  }
}
