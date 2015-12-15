package com.example.www.service

import javax.inject.Singleton


/*
 * Thin wrapper of System.currentTimeMillis just to stub it when testing.
 */
@Singleton
class TimeService {
  def currentTimeMillis(): Long = System.currentTimeMillis()
}
