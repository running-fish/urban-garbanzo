package com.example.www.mysql

import com.twitter.finagle.exp.mysql._


object MysqlUtil {
  def value[T](value: T): PartialFunction[Result, T] = {
    case _: OK => value
    case e: Result => throw new RuntimeException(s"mysql error: ${e.toString}")
  }

  def bytes(row: Row, column: String): Array[Byte] = {
    row(column).map {
      case RawValue(_, _, _, bytes) => bytes
      case _ => throw new RuntimeException("bytes expected")
    }.getOrElse(throw new RuntimeException(s"column ${column} doesn't exist"))
  }

  def long(row: Row, column: String): Long = {
    row(column).map {
      case LongValue(long) => long
      case IntValue(int) => int.toLong
      case _ => throw new RuntimeException("long expected")
    }.getOrElse(throw new RuntimeException(s"column ${column} doesn't exist"))
  }

  def string(row: Row, column: String): String = {
    row(column).map {
      case StringValue(string) => string
      case _ => throw new RuntimeException("string expected")
    }.getOrElse(throw new RuntimeException(s"column ${column} doesn't exist"))
  }

  def bool(row: Row, column: String): Boolean = {
    row(column).map {
      case IntValue(i) => i == 0
      case _ => throw new RuntimeException("bool expected")
    }.getOrElse(throw new RuntimeException(s"column ${column} doesn't exist"))
  }
}
