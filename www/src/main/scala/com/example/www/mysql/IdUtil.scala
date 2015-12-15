package com.example.www.mysql


/**
 * Extract timestamp and shardId from record id.
 * See http://instagram-engineering.tumblr.com/post/10853187575/sharding-ids-at-instagram
 */
object IdUtil {
  private[this] val epoch = 1420070400000L // 2015-01-01

  def timestamp(id: Long): Long = {
    (id >> 23) + epoch
  }

  def shardId(id: Long): Long = {
    (id << 41) >> 54
  }
}
