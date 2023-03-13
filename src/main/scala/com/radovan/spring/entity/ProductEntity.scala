package com.radovan.spring.entity

import javax.persistence.{Column, Entity, GeneratedValue, GenerationType, Id, Table, Transient}
import java.lang.Double
import java.util

import scala.beans.BeanProperty

@Entity
@Table(name = "products")
@SerialVersionUID(1L)
class ProductEntity extends Serializable {

  @Column(name = "product_id")
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Id
  @BeanProperty var productId:Integer = _

  @Column(name = "product_name")
  @BeanProperty var productName:String = _

  @Column(name = "product_price")
  @BeanProperty var productPrice:Double = _

  @BeanProperty var description:String = _

  @BeanProperty var category:String = _

  @Transient
  @BeanProperty var categoryList:util.List[String] = _

  @Column(name = "image_name")
  @BeanProperty var imageName:String = _

  @Transient
  def getMainImagePath: String = {
    if (productId == null || imageName == null) return "/images/productImages/unknown.jpg"
    "/images/productImages/" + this.imageName
  }


}

