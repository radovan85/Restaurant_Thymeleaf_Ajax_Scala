package com.radovan.spring.service.impl

import java.lang.Double
import java.sql.Timestamp
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util
import java.util.Optional

import com.radovan.spring.converter.TempConverter
import com.radovan.spring.dto.OrderDto
import com.radovan.spring.entity.{CustomerEntity, OrderEntity, OrderItemEntity, UserEntity}
import com.radovan.spring.repository.{CartItemRepository, CustomerRepository, OrderAddressRepository, OrderItemRepository, OrderRepository}
import com.radovan.spring.service.{CartService, OrderService}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import scala.collection.JavaConverters._

@Service
@Transactional
class OrderServiceImpl extends OrderService {

  @Autowired
  private var orderRepository:OrderRepository = _

  @Autowired
  private var tempConverter:TempConverter = _

  @Autowired
  private var orderItemRepository:OrderItemRepository = _

  @Autowired
  private var customerRepository:CustomerRepository = _

  @Autowired
  private var cartItemRepository:CartItemRepository = _

  @Autowired
  private var cartService:CartService = _

  @Autowired
  private var orderAddressRepository:OrderAddressRepository = _

  override def addOrder: OrderDto = {
    var returnValue:OrderDto = null
    val authUser = SecurityContextHolder.getContext.getAuthentication.getPrincipal.asInstanceOf[UserEntity]
    val customerOptional = Optional.ofNullable(customerRepository.findByUserId(authUser.getId))
    var customerEntity:CustomerEntity = null
    val orderEntity = new OrderEntity
    var orderedItems:util.List[OrderItemEntity] = new util.ArrayList[OrderItemEntity]
    if (customerOptional.isPresent) {
      customerEntity = customerOptional.get
      val cartEntityOptional = Optional.ofNullable(customerEntity.getCart)
      if (cartEntityOptional.isPresent) {
        val cartEntity = cartEntityOptional.get
        val allCartItems = Optional.ofNullable(cartEntity.getCartItems)
        if (!allCartItems.isEmpty) {
          for (cartItem <- allCartItems.get.asScala) {
            val orderItem = tempConverter.cartItemToOrderItemEntity(cartItem)
            orderedItems.add(orderItem)
          }
          cartItemRepository.removeAllByCartId(cartEntity.getCartId)
          cartService.refreshCartState(cartEntity.getCartId)
          val address = customerEntity.getAddress
          val orderAddress = tempConverter.addressToOrderAddress(address)
          val storedOrderAddress = orderAddressRepository.save(orderAddress)
          orderEntity.setCustomer(customerEntity)
          orderEntity.setAddress(storedOrderAddress)
          var storedOrder = orderRepository.save(orderEntity)
          for (orderItem <- orderedItems.asScala) {
            orderItem.setOrder(storedOrder)
            orderItemRepository.save(orderItem)
          }
          orderedItems = orderItemRepository.findAllByOrderId(storedOrder.getOrderId)
          val orderPrice = Optional.ofNullable(orderItemRepository.calculateGrandTotal(storedOrder.getOrderId))
          if (orderPrice.isPresent) storedOrder.setPrice(orderPrice.get)
          storedOrderAddress.setOrder(storedOrder)
          orderAddressRepository.saveAndFlush(storedOrderAddress)
          storedOrder.setOrderItems(orderedItems)
          storedOrder = orderRepository.saveAndFlush(storedOrder)
          returnValue = tempConverter.orderEntityToDto(storedOrder)
        }
      }
    }
    returnValue
  }

  override def getTodaysOrders: util.List[OrderDto] = {
    val returnValue = new util.ArrayList[OrderDto]
    val currentDate = LocalDateTime.now
    var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val currentDateStr = currentDate.format(formatter)
    val timestamp1Str = currentDateStr + " 00:00:00"
    val timestamp2Str = currentDateStr + " 23:59:59"
    formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val timestamp1 = Timestamp.valueOf(LocalDateTime.parse(timestamp1Str, formatter))
    val timestamp2 = Timestamp.valueOf(LocalDateTime.parse(timestamp2Str, formatter))
    val allOrders = Optional.ofNullable(orderRepository.findAllTodaysOrders(timestamp1, timestamp2))
    if (!allOrders.isEmpty) {
      for (order <- allOrders.get.asScala) {
        val orderDto = tempConverter.orderEntityToDto(order)
        returnValue.add(orderDto)
      }
    }
    returnValue
  }

  override def listAllByCustomerId(customerId: Integer): util.List[OrderDto] = {
    val returnValue = new util.ArrayList[OrderDto]
    val allOrders = Optional.ofNullable(orderRepository.findAllByCustomerId(customerId))
    if (!allOrders.isEmpty) {
      for (order <- allOrders.get.asScala) {
        val orderDto = tempConverter.orderEntityToDto(order)
        returnValue.add(orderDto)
      }
    }
    returnValue
  }

  override def calculateOrderPrice(orderId: Integer): Double = {
    val orderTotal = Optional.ofNullable(orderItemRepository.calculateGrandTotal(orderId))
    var returnValue = 0d
    if (orderTotal.isPresent) returnValue = orderTotal.get
    returnValue
  }

  override def listAll: util.List[OrderDto] = {
    val allOrders = orderRepository.findAll
    val returnValue = new util.ArrayList[OrderDto]
    for (order <- allOrders.asScala) {
      val orderDto = tempConverter.orderEntityToDto(order)
      returnValue.add(orderDto)
    }
    returnValue
  }

  override def getOrder(orderId: Integer): OrderDto = {
    var returnValue:OrderDto = null
    val orderEntity = Optional.ofNullable(orderRepository.getById(orderId))
    if (orderEntity.isPresent) returnValue = tempConverter.orderEntityToDto(orderEntity.get)
    returnValue
  }

  override def deleteOrder(orderId: Integer): Unit = {
    orderRepository.eraseById(orderId)
    orderRepository.flush()
  }
}

