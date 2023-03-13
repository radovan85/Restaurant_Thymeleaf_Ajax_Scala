package com.radovan.spring.controller

import java.nio.file.{Files, Paths}

import com.radovan.spring.dto.ProductDto
import com.radovan.spring.exceptions.ImagePathException
import com.radovan.spring.service.{AddressService, CartItemService, CartService, CustomerService, OrderAddressService, OrderItemService, OrderService, ProductService, UserService}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.{ModelAttribute, PathVariable, RequestMapping, RequestMethod, RequestParam}
import org.springframework.web.multipart.MultipartFile

import scala.collection.JavaConverters._

@Controller
@RequestMapping(value = Array("/admin"))
class AdminController {

  @Autowired
  private var productService:ProductService = _

  @Autowired
  private var customerService:CustomerService = _

  @Autowired
  private var userService:UserService = _

  @Autowired
  private var addressService:AddressService = _

  @Autowired
  private var orderService:OrderService = _

  @Autowired
  private var orderItemService:OrderItemService = _

  @Autowired
  private var orderAddressService:OrderAddressService = _

  @Autowired
  private var cartItemService:CartItemService = _

  @Autowired
  private var cartService:CartService = _

  @RequestMapping(value = Array("/"))
  def adminHome = "fragments/admin :: ajaxLoadedContent"

  @RequestMapping(value = Array("/createProduct"), method = Array(RequestMethod.GET))
  def renderProductForm(map: ModelMap): String = {
    val product = new ProductDto
    map.put("product", product)
    map.put("allCategories", product.getCategoryList)
    "fragments/productForm :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/createProduct"), method = Array(RequestMethod.POST))
  @throws[Throwable]
  def createProduct(@ModelAttribute("product") product: ProductDto, map: ModelMap, @RequestParam("productImage") file: MultipartFile, @RequestParam("imgName") imgName: String): String = {
    val fileLocation = "C:\\Users\\Radovan\\IdeaProjects\\Restaurant_Thymeleaf_Ajax_Scala\\src\\main\\resources\\static\\images\\productImages\\"
    var imageUUID:String = null
    val locationPath = Paths.get(fileLocation)
    if (!Files.exists(locationPath)) {
      val error = new Error("Invalid file path!")
      throw new ImagePathException(error)
    }
    imageUUID = file.getOriginalFilename
    val fileNameAndPath = Paths.get(fileLocation, imageUUID)
    if (file != null && !file.isEmpty) {
      Files.write(fileNameAndPath, file.getBytes)
      System.out.println("IMage Save at:" + fileNameAndPath.toString)
    }
    else imageUUID = imgName
    product.setImageName(imageUUID)
    productService.addProduct(product)
    "fragments/homePage :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/allProducts"), method = Array(RequestMethod.GET))
  def allProductsList(map: ModelMap): String = {
    val allProducts = productService.listAll
    map.put("allProducts", allProducts)
    var recordsPerPage:Integer = 5;
    map.put("recordsPerPage", recordsPerPage)
    "fragments/adminProductList :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/updateProduct/{productId}"), method = Array(RequestMethod.GET))
  def renderUpdateForm(@PathVariable("productId") productId: Integer, map: ModelMap): String = {
    val product = new ProductDto
    val currentProduct = productService.getProduct(productId)
    map.put("product", product)
    map.put("currentProduct", currentProduct)
    map.put("allCategories", product.getCategoryList)
    "fragments/updateProduct :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/deleteProduct/{productId}"), method = Array(RequestMethod.GET))
  @throws[Throwable]
  def deleteProduct(@PathVariable("productId") productId: Integer): String = {
    val product = productService.getProduct(productId)
    val path = Paths.get("C:\\Users\\Radovan\\IdeaProjects\\Restaurant_Thymeleaf_Ajax_Scala\\src\\main\\resources\\static\\images\\productImages\\" + product.getImageName)
    if (Files.exists(path)) Files.delete(path)
    else {
      val error = new Error("Invalid file path!")
      throw new ImagePathException(error)
    }
    cartItemService.eraseAllByProductId(productId)
    productService.deleteProduct(productId)
    "fragments/homePage :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/productDetails/{productId}"), method = Array(RequestMethod.GET))
  def getProductDetails(@PathVariable("productId") productId: Integer, map: ModelMap): String = {
    val currentProduct = productService.getProduct(productId)
    map.put("currentProduct", currentProduct)
    "fragments/productDetails :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/allCustomers"), method = Array(RequestMethod.GET))
  def listAllCustomers(map: ModelMap): String = {
    val allCustomers = customerService.listAll
    val allUsers = userService.listAllUsers
    var recordsPerPage:Integer = 7;
    map.put("allCustomers", allCustomers)
    map.put("allUsers", allUsers)
    map.put("recordsPerPage", recordsPerPage)
    "fragments/customerList :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/customerDetails/{customerId}"), method = Array(RequestMethod.GET))
  def getCustomerDetails(@PathVariable("customerId") customerId: Integer, map: ModelMap): String = {
    val customer = customerService.getCustomer(customerId)
    val address = addressService.getAddressById(customer.getAddressId)
    val user = userService.getUserById(customer.getUserId)
    val allOrders = orderService.listAllByCustomerId(customerId)
    map.put("customer", customer)
    map.put("address", address)
    map.put("user", user)
    map.put("allOrders", allOrders)
    "fragments/customerDetails :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/allOrders"), method = Array(RequestMethod.GET))
  def getAllOrders(map: ModelMap): String = {
    val allOrders = orderService.listAll
    val allCustomers = customerService.listAll
    val allUsers = userService.listAllUsers
    var recordsPerPage:Integer = 10;
    map.put("allOrders", allOrders)
    map.put("allCustomers", allCustomers)
    map.put("allUsers", allUsers)
    map.put("recordsPerPage", recordsPerPage)
    "fragments/orderList :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/allOrdersToday"), method = Array(RequestMethod.GET))
  def getAllOrdersToday(map: ModelMap): String = {
    val allOrders = orderService.getTodaysOrders
    val allCustomers = customerService.listAll
    val allUsers = userService.listAllUsers
    var recordsPerPage:Integer = 10;
    map.put("allOrders", allOrders)
    map.put("allCustomers", allCustomers)
    map.put("allUsers", allUsers)
    map.put("recordsPerPage", recordsPerPage)
    "fragments/ordersTodayList :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/getOrder/{orderId}"), method = Array(RequestMethod.GET))
  def orderDetails(@PathVariable("orderId") orderId: Integer, map: ModelMap): String = {
    val order = orderService.getOrder(orderId)
    val customer = customerService.getCustomer(order.getCustomerId)
    val address = orderAddressService.getAddressById(order.getAddressId)
    val orderPrice = orderService.calculateOrderPrice(orderId)
    val orderedItems = orderItemService.listAllByOrderId(orderId)
    map.put("order", order)
    map.put("address", address)
    map.put("orderPrice", orderPrice)
    map.put("orderedItems", orderedItems)
    map.put("customer", customer)
    "fragments/orderDetails :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/deleteOrder/{orderId}"), method = Array(RequestMethod.GET))
  def deleteOrder(@PathVariable("orderId") orderId: Integer): String = {
    val order = orderService.getOrder(orderId)
    val addressId = order.getAddressId
    orderItemService.eraseAllByOrderId(orderId)
    orderService.deleteOrder(orderId)
    orderAddressService.deleteAddress(addressId)
    "fragments/homePage :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/invalidPath"), method = Array(RequestMethod.GET))
  def invalidImagePath = "fragments/invalidImagePath :: ajaxLoadedContent"

  @RequestMapping(value = Array("/deleteCustomer/{customerId}"), method = Array(RequestMethod.GET))
  def removeCustomer(@PathVariable("customerId") customerId: Integer): String = {
    val customer = customerService.getCustomer(customerId)
    val cart = cartService.getCartByCartId(customer.getCartId)
    val address = addressService.getAddressById(customer.getAddressId)
    val user = userService.getUserById(customer.getUserId)
    val allOrders = orderService.listAllByCustomerId(customerId)
    for (order <- allOrders.asScala) {
      orderItemService.eraseAllByOrderId(order.getOrderId)
      orderService.deleteOrder(order.getOrderId)
      orderAddressService.deleteAddress(order.getAddressId)
    }
    cartItemService.eraseAllCartItems(cart.getCartId)
    customerService.resetCustomer(customerId)
    addressService.deleteAddress(address.getAddressId)
    cartService.deleteCart(cart.getCartId)
    customerService.deleteCustomer(customerId)
    userService.deleteUser(user.getId)
    "fragments/homePage :: ajaxLoadedContent"
  }
}

