package com.radovan.spring.entity

import javax.persistence.{Column, Entity, GeneratedValue, GenerationType, Id, OneToOne, Table}

import scala.beans.BeanProperty

@Entity
@Table(name = "addresses")
@SerialVersionUID(1L)
class AddressEntity extends Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(name = "id")
  @BeanProperty var addressId:Integer = _

  @BeanProperty var address:String = _

  @BeanProperty var city:String = _

  @Column(name = "zip_code")
  @BeanProperty var zipcode:String = _

  @OneToOne(mappedBy = "address")
  @BeanProperty var customer:CustomerEntity = _


}

