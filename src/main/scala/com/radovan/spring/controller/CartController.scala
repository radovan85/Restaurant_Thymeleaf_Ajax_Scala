package com.radovan.spring.controller

import java.util.Optional

import com.radovan.spring.dto.CartItemDto
import com.radovan.spring.service.{CartItemService, CartService, CustomerService, ProductService, UserService}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.{ModelAttribute, PathVariable, RequestMapping, RequestMethod}

@Controller
@RequestMapping(value = Array("/cart"))
class CartController {

  @Autowired
  private var productService:ProductService = _

  @Autowired
  private var userService:UserService = _

  @Autowired
  private var customerService:CustomerService = _

  @Autowired
  private var cartService:CartService = _

  @Autowired
  private var cartItemService:CartItemService = _

  @RequestMapping(value = Array("/addToCart/{productId}"))
  def renderItemForm(@PathVariable("productId") productId: Integer, map: ModelMap): String = {
    val cartItem = new CartItemDto
    val selectedProduct = productService.getProduct(productId)
    map.put("cartItem", cartItem)
    map.put("selectedProduct", selectedProduct)
    map.put("allHotnessLevels", cartItem.getHotnessLevelList)
    "fragments/cartItemForm :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/addToCart"), method = Array(RequestMethod.POST))
  def addCartItem(@ModelAttribute("cartItem") cartItem: CartItemDto): String = {
    val productId = cartItem.getProductId
    val hotnessLevel = cartItem.getHotnessLevel
    val authUser = userService.getCurrentUser
    val customer = customerService.getCustomerByUserId(authUser.getId)
    val cart = cartService.getCartByCartId(customer.getCartId)
    val product = productService.getProduct(productId)
    val existingCartItem = Optional.ofNullable(cartItemService.getCartItemByCartIdAndProductIdAndHotnessLevel(cart.getCartId, productId, hotnessLevel))
    if (existingCartItem.isPresent) {
      cartItem.setCartItemId(existingCartItem.get.getCartItemId)
      cartItem.setCartId(cart.getCartId)
      cartItem.setQuantity(existingCartItem.get.getQuantity + cartItem.getQuantity)
      if (cartItem.getQuantity > 50) cartItem.setQuantity(50)
      cartItem.setPrice(product.getProductPrice * cartItem.getQuantity)
      cartItemService.addCartItem(cartItem)
      cartService.refreshCartState(cart.getCartId)
    }
    else {
      cartItem.setQuantity(cartItem.getQuantity)
      if (cartItem.getQuantity > 50) cartItem.setQuantity(50)
      cartItem.setCartId(cart.getCartId)
      cartItem.setPrice(product.getProductPrice * cartItem.getQuantity)
      cartItemService.addCartItem(cartItem)
      cartService.refreshCartState(cart.getCartId)
    }
    "fragments/homePage :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/itemAddCompleted"), method = Array(RequestMethod.GET))
  def itemAdded = "fragments/itemAdded :: ajaxLoadedContent"

  @RequestMapping(value = Array("/getCart"), method = Array(RequestMethod.GET))
  def cartDetails(map: ModelMap): String = {
    val authUser = userService.getCurrentUser
    val customer = customerService.getCustomerByUserId(authUser.getId)
    val cart = cartService.getCartByCartId(customer.getCartId)
    val allCartItems = cartItemService.listAllByCartId(customer.getCartId)
    val allProducts = productService.listAll
    map.put("allCartItems", allCartItems)
    map.put("allProducts", allProducts)
    map.put("cart", cart)
    "fragments/cart :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/deleteItem/{cartId}/{itemId}"), method = Array(RequestMethod.GET))
  def deleteCartItem(@PathVariable("cartId") cartId: Integer, @PathVariable("itemId") itemId: Integer): String = {
    cartItemService.removeCartItem(cartId, itemId)
    "fragments/homePage :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/deleteAllItems/{cartId}"), method = Array(RequestMethod.GET))
  def deleteAllCartItems(@PathVariable("cartId") cartId: Integer): String = {
    cartItemService.eraseAllCartItems(cartId)
    "fragments/homePage :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/invalidCart"), method = Array(RequestMethod.GET))
  def invalidCartEx(map: ModelMap) = "fragments/invalidCart :: ajaxLoadedContent"
}

