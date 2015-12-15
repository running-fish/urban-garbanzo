package com.example.mysql.module

import javax.inject.Singleton

import com.google.inject.Provides
import com.twitter.finagle.exp.Mysql
import com.twitter.finagle.exp.mysql.{Client, Transactions}
import com.twitter.inject.TwitterModule


object MysqlModule extends TwitterModule {
  val host = flag("mysql.host", "127.0.0.1", "MySQL host")
  val port = flag("mysql.port", 3306, "MySQL port")
  val database = flag("mysql.database", "example_0000", "MySQL database")
  val username = flag("mysql.username", "example", "MySQL username")
  val password = flag("mysql.password", "example", "MySQL password")

  @Singleton
  @Provides
  def provideRichClient(): Client with Transactions = {
    Mysql.client
      .withCredentials(username(), password())
      .withDatabase(database())
      .newRichClient(s"inet!${host()}:${port()}")
  }
}
