package com.radovan.spring.repository

import java.sql.Timestamp
import java.util

import com.radovan.spring.entity.OrderEntity
import org.springframework.data.jpa.repository.{JpaRepository, Modifying, Query}
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
trait OrderRepository extends JpaRepository[OrderEntity, Integer] {

  @Query(value = "select * from orders where customer_id = :customerId", nativeQuery = true)
  def findAllByCustomerId(@Param("customerId") customerId: Integer): util.List[OrderEntity]

  @Query(value = "select * from orders where date >= :timestamp1 and date <= :timestamp2", nativeQuery = true)
  def findAllTodaysOrders(@Param("timestamp1") timestamp1: Timestamp, @Param("timestamp2") timestamp2: Timestamp): util.List[OrderEntity]

  @Modifying
  @Query(value = "delete from orders where order_id = :orderId", nativeQuery = true)
  def eraseById(@Param("orderId") orderId: Integer): Unit
}
