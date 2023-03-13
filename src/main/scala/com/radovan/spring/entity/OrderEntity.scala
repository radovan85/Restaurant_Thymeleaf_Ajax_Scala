package com.radovan.spring.entity

import javax.persistence.{CascadeType, Column, Entity, FetchType, GeneratedValue, GenerationType, Id, JoinColumn, ManyToOne, OneToMany, OneToOne, Table}
import org.hibernate.annotations.{CreationTimestamp, Fetch, FetchMode}
import java.sql.Timestamp
import java.lang.Double
import java.util
import scala.beans.BeanProperty

@Entity
@Table(name = "orders")
@SerialVersionUID(1L)
class OrderEntity extends Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(name = "order_id")
  @BeanProperty var orderId:Integer = _

  @ManyToOne
  @JoinColumn(name = "customer_id")
  @BeanProperty var customer:CustomerEntity = _

  @OneToOne(cascade = Array(CascadeType.ALL), fetch = FetchType.EAGER)
  @JoinColumn(name = "address_id")
  @BeanProperty var address:OrderAddressEntity = _

  @OneToMany(mappedBy = "order", cascade = Array(CascadeType.ALL), fetch = FetchType.EAGER)
  @Fetch(value = FetchMode.SUBSELECT)
  @BeanProperty var orderItems:util.List[OrderItemEntity] = _

  @BeanProperty var price:Double = _

  @CreationTimestamp
  @BeanProperty var date:Timestamp = _


}

