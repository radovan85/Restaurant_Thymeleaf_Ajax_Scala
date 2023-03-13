package com.radovan.spring.entity

import javax.persistence.{CascadeType, Column, Entity, GeneratedValue, GenerationType, Id, JoinColumn, ManyToOne, Table, Transient}
import java.lang.Double
import java.util

import scala.beans.BeanProperty

@Entity
@Table(name = "cart_items")
@SerialVersionUID(1L)
class CartItemEntity extends Serializable {

  @Column(name = "item_id")
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @BeanProperty var cartItemId:Integer = _

  @BeanProperty var price:Double = _

  @BeanProperty var quantity:Integer = _

  @Column(name = "hotness_level")
  @BeanProperty var hotnessLevel:String = _

  @ManyToOne
  @JoinColumn(name = "cart_id")
  @BeanProperty var cart:CartEntity = _

  @ManyToOne(cascade = Array(CascadeType.ALL))
  @JoinColumn(name = "product_id")
  @BeanProperty var product:ProductEntity = _

  @Transient
  @BeanProperty var hotnessLevelList:util.List[String] = _


}
