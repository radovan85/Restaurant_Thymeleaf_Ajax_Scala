package com.radovan.spring.service.impl

import java.lang.Double
import java.util.Optional

import com.radovan.spring.converter.TempConverter
import com.radovan.spring.dto.CartDto
import com.radovan.spring.exceptions.InvalidCartException
import com.radovan.spring.repository.{CartItemRepository, CartRepository}
import com.radovan.spring.service.CartService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CartServiceImpl extends CartService {

  @Autowired
  private var cartRepository:CartRepository = _

  @Autowired
  private var cartItemRepository:CartItemRepository = _

  @Autowired
  private var tempConverter:TempConverter = _

  override def getCartByCartId(cartId: Integer): CartDto = {

    var returnValue:CartDto = null
    val cartEntity = Optional.ofNullable(cartRepository.getById(cartId))
    if (cartEntity.isPresent) returnValue = tempConverter.cartEntityToDto(cartEntity.get)
    returnValue
  }

  override def calculateCartPrice(cartId: Integer): Double = {
    var returnValue = 0d
    val cartPrice = Optional.ofNullable(cartItemRepository.calculateGrandTotal(cartId))
    if (cartPrice.isPresent) returnValue = cartPrice.get
    returnValue
  }

  override def refreshCartState(cartId: Integer): Unit = {
    val cartEntity = cartRepository.getById(cartId)
    val price = Optional.ofNullable(cartItemRepository.calculateGrandTotal(cartId))
    if (price.isPresent) cartEntity.setCartPrice(price.get)
    else cartEntity.setCartPrice(0d)
    cartRepository.saveAndFlush(cartEntity)
  }

  override def validateCart(cartId: Integer): CartDto = {
    val cartEntity = Optional.ofNullable(cartRepository.getById(cartId))
    var returnValue = new CartDto
    val error = new Error("Invalid Cart")
    if (cartEntity.isPresent) {
      if (cartEntity.get.getCartItems.size == 0) throw new InvalidCartException(error)
      returnValue = tempConverter.cartEntityToDto(cartEntity.get)
    }
    else throw new InvalidCartException(error)
    returnValue
  }

  override def deleteCartItem(itemId: Integer, cartId: Integer): Unit = {
    cartItemRepository.removeCartItem(cartId, itemId)
    cartItemRepository.flush()
  }

  override def eraseAllCartItems(cartId: Integer): Unit = {
    cartItemRepository.removeAllByCartId(cartId)
    cartItemRepository.flush()
  }

  override def deleteCart(cartId: Integer): Unit = {
    cartRepository.deleteById(cartId)
    cartItemRepository.flush()
  }
}

