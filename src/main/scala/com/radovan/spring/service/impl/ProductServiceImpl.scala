package com.radovan.spring.service.impl

import java.util.Optional
import java.util

import com.radovan.spring.converter.TempConverter
import com.radovan.spring.dto.ProductDto
import com.radovan.spring.repository.{CartItemRepository, CartRepository, ProductRepository}
import com.radovan.spring.service.{CartService, ProductService}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import scala.collection.JavaConverters._

@Service
@Transactional
class ProductServiceImpl extends ProductService {

  @Autowired
  private var productRepository:ProductRepository = _

  @Autowired
  private var tempConverter:TempConverter = _

  @Autowired
  private var cartService:CartService = _

  @Autowired
  private var cartRepository:CartRepository = _

  @Autowired
  private var cartItemRepository:CartItemRepository = _

  override def getProduct(productId: Integer): ProductDto = {
    val productEntity = Optional.ofNullable(productRepository.getById(productId))
    var returnValue:ProductDto = null
    if (productEntity.isPresent) returnValue = tempConverter.productEntityToDto(productEntity.get)
    returnValue
  }

  override def listByCategory(category: String): util.List[ProductDto] = {
    val allProducts = Optional.ofNullable(productRepository.findAllByCategory(category))
    val returnValue = new util.ArrayList[ProductDto]
    if (!allProducts.isEmpty) {
      for (product <- allProducts.get.asScala) {
        val productDto = tempConverter.productEntityToDto(product)
        returnValue.add(productDto)
      }
    }
    returnValue
  }

  override def addProduct(product: ProductDto): ProductDto = {
    val productId = Optional.ofNullable(product.getProductId)
    val productEntity = tempConverter.productDtoToEntity(product)
    val storedProduct = productRepository.save(productEntity)
    val returnValue = tempConverter.productEntityToDto(storedProduct)
    if (productId.isPresent) {
      val allCartItems = Optional.ofNullable(cartItemRepository.findAllByProductId(returnValue.getProductId))
      if (!allCartItems.isEmpty) {
        for (itemEntity <- allCartItems.get.asScala) {
          var price = returnValue.getProductPrice
          price = price * itemEntity.getQuantity
          itemEntity.setPrice(price)
          cartItemRepository.saveAndFlush(itemEntity)
        }
        val allCarts = Optional.ofNullable(cartRepository.findAll)
        if (!allCarts.isEmpty) {
          for (cartEntity <- allCarts.get.asScala) {
            cartService.refreshCartState(cartEntity.getCartId)
          }
        }
      }
    }
    returnValue
  }

  override def listAll: util.List[ProductDto] = {
    val allProducts = Optional.ofNullable(productRepository.findAll)
    val returnValue = new util.ArrayList[ProductDto]
    if (!allProducts.isEmpty) {
      for (productEntity <- allProducts.get.asScala) {
        val productDto = tempConverter.productEntityToDto(productEntity)
        returnValue.add(productDto)
      }
    }
    returnValue
  }

  override def updateProduct(productId: Integer, product: ProductDto): ProductDto = {
    val tempProduct = productRepository.getById(productId)
    val productEntity = tempConverter.productDtoToEntity(product)
    productEntity.setProductId(tempProduct.getProductId)
    val updatedProduct = productRepository.saveAndFlush(productEntity)
    val returnValue = tempConverter.productEntityToDto(updatedProduct)
    returnValue
  }

  override def deleteProduct(productId: Integer): Unit = {
    productRepository.deleteById(productId)
    productRepository.flush()
  }
}
