package com.radovan.spring.dto

import java.lang.Double
import java.util
import scala.beans.BeanProperty

@SerialVersionUID(1L)
class CartItemDto() extends Serializable {


  @BeanProperty var cartItemId:Integer = _
  @BeanProperty var price:Double = _
  @BeanProperty var quantity:Integer = _
  @BeanProperty var hotnessLevel:String = _
  @BeanProperty var cartId:Integer = _
  @BeanProperty var productId:Integer = _
  @BeanProperty var hotnessLevelList:util.List[String] = _

  hotnessLevelList = new util.ArrayList[String]
  hotnessLevelList.add("Extremly Cold")
  hotnessLevelList.add("Cold")
  hotnessLevelList.add("Warmish")
  hotnessLevelList.add("Warm")
  hotnessLevelList.add("Hot")
  hotnessLevelList.add("Extremly Hot")


}

