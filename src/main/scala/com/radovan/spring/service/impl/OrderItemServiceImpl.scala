package com.radovan.spring.service.impl

import java.util.Optional
import java.util

import com.radovan.spring.converter.TempConverter
import com.radovan.spring.dto.OrderItemDto
import com.radovan.spring.repository.OrderItemRepository
import com.radovan.spring.service.OrderItemService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import scala.collection.JavaConverters._

@Service
@Transactional
class OrderItemServiceImpl extends OrderItemService {

  @Autowired
  private var itemRepository:OrderItemRepository = _

  @Autowired
  private var tempConverter:TempConverter = _

  override def getItem(itemId: Integer): OrderItemDto = {
    var returnValue:OrderItemDto = null
    val itemEntity = Optional.ofNullable(itemRepository.getById(itemId))
    if (itemEntity.isPresent) returnValue = tempConverter.orderItemEntityToDto(itemEntity.get)
    returnValue
  }

  override def listAllByOrderId(orderId: Integer): util.List[OrderItemDto] = {
    val allItems = Optional.ofNullable(itemRepository.findAllByOrderId(orderId))
    val returnValue = new util.ArrayList[OrderItemDto]
    if (!allItems.isEmpty) {
      for (itemEntity <- allItems.get.asScala) {
        val itemDto = tempConverter.orderItemEntityToDto(itemEntity)
        returnValue.add(itemDto)
      }
    }
    returnValue
  }

  override def eraseAllByOrderId(orderId: Integer): Unit = {
    itemRepository.deleteAllByOrderId(orderId)
    itemRepository.flush()
  }
}
