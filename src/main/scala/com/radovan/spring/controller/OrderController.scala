package com.radovan.spring.controller

import com.radovan.spring.dto.OrderDto
import com.radovan.spring.service.{AddressService, CartItemService, CartService, CustomerService, OrderService, ProductService}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.{PathVariable, RequestMapping, RequestMethod}

@Controller
@RequestMapping(value = Array("/orders"))
class OrderController {

  @Autowired
  private var cartService:CartService = _

  @Autowired
  private var customerService:CustomerService = _

  @Autowired
  private var cartItemService:CartItemService = _

  @Autowired
  private var addressService:AddressService = _

  @Autowired
  private var productService:ProductService = _

  @Autowired
  private var orderService:OrderService = _

  @RequestMapping(value = Array("/confirmOrder/{cartId}"), method = Array(RequestMethod.GET))
  def confirmOrder(@PathVariable("cartId") cartId: Integer, map: ModelMap): String = {
    val order = new OrderDto
    cartService.validateCart(cartId)
    val customer = customerService.getCustomerByCartId(cartId)
    val allCartItems = cartItemService.listAllByCartId(cartId)
    val address = addressService.getAddressById(customer.getAddressId)
    val cart = cartService.getCartByCartId(customer.getCartId)
    val allProducts = productService.listAll
    map.put("order", order)
    map.put("customer", customer)
    map.put("allCartItems", allCartItems)
    map.put("address", address)
    map.put("cart", cart)
    map.put("allProducts", allProducts)
    "fragments/orderConfirmation :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/processOrder"), method = Array(RequestMethod.POST))
  def processOrder: String = {
    orderService.addOrder
    "fragments/homePage :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/orderCompleted"), method = Array(RequestMethod.GET))
  def orderCompletedReport = "fragments/orderCompleted :: ajaxLoadedContent"
}

