package com.radovan.spring.service

import java.lang.Double
import com.radovan.spring.dto.CartDto

trait CartService {

  def getCartByCartId(cartId: Integer): CartDto

  def calculateCartPrice(cartId: Integer): Double

  def refreshCartState(cartId: Integer): Unit

  def validateCart(cartId: Integer): CartDto

  def deleteCartItem(itemId: Integer, cartId: Integer): Unit

  def eraseAllCartItems(cartId: Integer): Unit

  def deleteCart(cartId: Integer): Unit
}
