package com.radovan.spring.dto

import scala.beans.BeanProperty
import java.lang.Double
import java.util

@SerialVersionUID(1L)
class CartDto extends Serializable {

  @BeanProperty var cartId:Integer = _
  @BeanProperty var customerId:Integer = _
  @BeanProperty var cartItemIds:util.List[Integer] = _
  @BeanProperty var cartPrice:Double = _


}

