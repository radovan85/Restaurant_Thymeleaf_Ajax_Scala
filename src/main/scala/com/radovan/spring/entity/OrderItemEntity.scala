package com.radovan.spring.entity

import javax.persistence.{Column, Entity, GeneratedValue, GenerationType, Id, JoinColumn, ManyToOne, Table}
import java.lang.Double

import scala.beans.BeanProperty

@Entity
@Table(name = "order_items")
@SerialVersionUID(1L)
class OrderItemEntity extends Serializable {

  @Column(name = "item_id")
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @BeanProperty var orderItemId:Integer = _

  @BeanProperty var price:Double = _

  @BeanProperty var quantity:Integer = _

  @Column(name = "hotness_level")
  @BeanProperty var hotnessLevel:String = _

  @Column(name = "product_name")
  @BeanProperty var productName:String = _

  @Column(name = "product_price")
  @BeanProperty var productPrice:Double = _

  @ManyToOne
  @JoinColumn(name = "order_id")
  @BeanProperty var order:OrderEntity = _


}

