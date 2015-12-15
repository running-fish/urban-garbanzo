package com.example.www.service

import javax.inject.{Inject, Singleton}

import com.example.www.mysql.MysqlUtil
import com.twitter.finagle.exp.mysql.Client
import com.twitter.util.Future


@Singleton
class UserIdService @Inject()(mysql: Client) {
  private[this] val query = "SELECT next_user_id() AS id"

  def create(): Future[Long] = {
    mysql.select(query)(MysqlUtil.long(_, "id")).map(_.head)
  }
}
