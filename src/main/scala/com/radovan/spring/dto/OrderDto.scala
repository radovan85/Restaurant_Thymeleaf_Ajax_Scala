package com.radovan.spring.dto

import java.lang.Double
import java.sql.Timestamp
import java.util

import scala.beans.BeanProperty

@SerialVersionUID(1L)
class OrderDto extends Serializable {

  @BeanProperty var orderId:Integer = _
  @BeanProperty var customerId:Integer = _
  @BeanProperty var addressId:Integer = _
  @BeanProperty var orderItemsIds:util.List[Integer] = _
  @BeanProperty var price:Double = _
  @BeanProperty var date:Timestamp = _
  @BeanProperty var dateStr:String = _


}