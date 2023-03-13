package com.radovan.spring.service

import java.lang.Double
import java.util
import com.radovan.spring.dto.OrderDto

trait OrderService {

  def addOrder: OrderDto

  def getTodaysOrders: util.List[OrderDto]

  def listAllByCustomerId(customerId: Integer): util.List[OrderDto]

  def calculateOrderPrice(orderId: Integer): Double

  def listAll: util.List[OrderDto]

  def getOrder(orderId: Integer): OrderDto

  def deleteOrder(orderId: Integer): Unit
}
