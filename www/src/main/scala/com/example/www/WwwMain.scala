package com.example.www

import com.example.mysql.module.MysqlModule
import com.example.www.controller.UserController
import com.twitter.finagle.http.{Request, Response}
import com.twitter.finatra.http.HttpServer
import com.twitter.finatra.http.filters.CommonFilters
import com.twitter.finatra.http.routing.HttpRouter
import com.twitter.finatra.logging.filter.{LoggingMDCFilter, TraceIdMDCFilter}


object WwwMain extends WwwServer

class WwwServer extends HttpServer {
  override val name = "www"

  override val modules = Seq(MysqlModule)

  override def configureHttp(router: HttpRouter): Unit = {
    router
      .filter[LoggingMDCFilter[Request, Response]]
      .filter[TraceIdMDCFilter[Request, Response]]
      .filter[CommonFilters]
      .add[UserController]
  }
}
