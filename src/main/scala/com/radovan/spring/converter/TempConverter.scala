package com.radovan.spring.converter

import java.time.format.DateTimeFormatter
import java.util
import java.util.Optional

import com.radovan.spring.dto.{AddressDto, CartDto, CartItemDto, CustomerDto, OrderAddressDto, OrderDto, OrderItemDto, ProductDto, RoleDto, UserDto}
import com.radovan.spring.entity.{AddressEntity, CartEntity, CartItemEntity, CustomerEntity, OrderAddressEntity, OrderEntity, OrderItemEntity, ProductEntity, RoleEntity, UserEntity}
import com.radovan.spring.repository.{AddressRepository, CartItemRepository, CartRepository, CustomerRepository, OrderAddressRepository, OrderItemRepository, OrderRepository, ProductRepository, RoleRepository, UserRepository}
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired

import scala.collection.JavaConverters._

class TempConverter {

  @Autowired
  private var mapper:ModelMapper = _

  @Autowired
  private var cartRepository:CartRepository = _

  @Autowired
  private var customerRepository:CustomerRepository = _

  @Autowired
  private var cartItemRepository:CartItemRepository = _

  @Autowired
  private var productRepository:ProductRepository = _

  @Autowired
  private var userRepository:UserRepository = _

  @Autowired
  private var orderRepository:OrderRepository = _

  @Autowired
  private var orderItemRepository:OrderItemRepository = _

  @Autowired
  private var roleRepository:RoleRepository = _

  @Autowired
  private var addressRepository:AddressRepository = _

  @Autowired
  private var orderAddressRepository:OrderAddressRepository = _

  def cartEntityToDto(cartEntity: CartEntity): CartDto = {
    val returnValue = mapper.map(cartEntity, classOf[CartDto])
    val customerEntity = Optional.ofNullable(cartEntity.getCustomer)
    if (customerEntity.isPresent) returnValue.setCustomerId(customerEntity.get.getCustomerId)
    val cartItems = Optional.ofNullable(cartEntity.getCartItems)
    val cartItemsIds = new util.ArrayList[Integer]
    if (cartItems.isPresent) {
      for (item <- cartItems.get.asScala) {
        cartItemsIds.add(item.getCartItemId)
      }
    }
    returnValue.setCartItemIds(cartItemsIds)
    returnValue
  }

  def cartDtoToEntity(cartDto: CartDto): CartEntity = {
    val returnValue:CartEntity = mapper.map(cartDto, classOf[CartEntity])
    val customerId = Optional.ofNullable(cartDto.getCustomerId)
    if (customerId.isPresent) {
      val customer = customerRepository.getById(customerId.get)
      returnValue.setCustomer(customer)
    }
    val cartItemsIds = Optional.ofNullable(cartDto.getCartItemIds)
    val cartItems = new util.ArrayList[CartItemEntity]
    if (!cartItemsIds.isEmpty) {
      for (itemId <- cartItemsIds.get.asScala) {
        val itemEntity = cartItemRepository.getById(itemId)
        cartItems.add(itemEntity)
      }
    }
    returnValue.setCartItems(cartItems)
    returnValue
  }

  def cartItemEntityToDto(itemEntity: CartItemEntity): CartItemDto = {
    val returnValue:CartItemDto = mapper.map(itemEntity, classOf[CartItemDto])
    val cartEntity = Optional.ofNullable(itemEntity.getCart)
    if (cartEntity.isPresent) returnValue.setCartId(cartEntity.get.getCartId)
    val productEntity:Optional[ProductEntity] = Optional.ofNullable(itemEntity.getProduct)
    if (productEntity.isPresent) returnValue.setProductId(productEntity.get.getProductId)
    returnValue
  }

  def cartItemDtoToEntity(itemDto: CartItemDto): CartItemEntity = {
    val returnValue = mapper.map(itemDto, classOf[CartItemEntity])
    val cartId = Optional.ofNullable(itemDto.getCartId)
    if (cartId.isPresent) {
      val cartEntity = cartRepository.getById(cartId.get)
      returnValue.setCart(cartEntity)
    }
    val productId = Optional.ofNullable(itemDto.getProductId)
    if (productId.isPresent) {
      val productEntity = productRepository.getById(productId.get)
      returnValue.setProduct(productEntity)
    }
    returnValue
  }

  def customerEntityToDto(customerEntity: CustomerEntity): CustomerDto = {
    val returnValue = mapper.map(customerEntity, classOf[CustomerDto])
    val userEntity = Optional.ofNullable(customerEntity.getUser)
    if (userEntity.isPresent) returnValue.setUserId(userEntity.get.getId)
    val orders = Optional.ofNullable(customerEntity.getOrders)
    val ordersIds = new util.ArrayList[Integer]
    if (!orders.isEmpty) {
      for (orderEntity <- orders.get.asScala) {
        ordersIds.add(orderEntity.getOrderId)
      }
    }
    returnValue.setOrdersIds(ordersIds)
    val cartEntity = Optional.ofNullable(customerEntity.getCart)
    if (cartEntity.isPresent) returnValue.setCartId(cartEntity.get.getCartId)
    val addressEntity = Optional.ofNullable(customerEntity.getAddress)
    if (addressEntity.isPresent) returnValue.setAddressId(addressEntity.get.getAddressId)
    returnValue
  }

  def customerDtoToEntity(customer: CustomerDto): CustomerEntity = {
    val returnValue = mapper.map(customer, classOf[CustomerEntity])
    val userId = Optional.ofNullable(customer.getUserId)
    if (userId.isPresent) {
      val userEntity = userRepository.getById(userId.get)
      returnValue.setUser(userEntity)
    }
    val ordersIds = Optional.ofNullable(customer.getOrdersIds)
    val orders = new util.ArrayList[OrderEntity]
    if (ordersIds.isPresent) {
      for (orderId <- ordersIds.get.asScala) {
        val orderEntity = orderRepository.getById(orderId)
        orders.add(orderEntity)
      }
    }
    returnValue.setOrders(orders)
    val cartId = Optional.ofNullable(customer.getCartId)
    if (cartId.isPresent) {
      val cartEntity = cartRepository.getById(cartId.get)
      returnValue.setCart(cartEntity)
    }
    val addressId = Optional.ofNullable(customer.getAddressId)
    if (addressId.isPresent) {
      val address = addressRepository.getById(addressId.get)
      returnValue.setAddress(address)
    }
    returnValue
  }

  def orderEntityToDto(orderEntity: OrderEntity): OrderDto = {
    val returnValue = mapper.map(orderEntity, classOf[OrderDto])
    val customerEntity = Optional.ofNullable(orderEntity.getCustomer)
    if (customerEntity.isPresent) returnValue.setCustomerId(customerEntity.get.getCustomerId)
    val address = Optional.ofNullable(orderEntity.getAddress)
    if (address.isPresent) returnValue.setAddressId(address.get.getOrderAddressId)
    val orderItems = Optional.ofNullable(orderEntity.getOrderItems)
    val orderItemsIds = new util.ArrayList[Integer]
    if (!orderItems.isEmpty) {
      for (itemEntity <- orderItems.get.asScala) {
        orderItemsIds.add(itemEntity.getOrderItemId)
      }
    }
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    val dateOpt = Optional.ofNullable(orderEntity.getDate)
    if (dateOpt.isPresent) {
      val dateStr = dateOpt.get.toLocalDateTime.format(formatter)
      returnValue.setDateStr(dateStr)
    }
    returnValue.setOrderItemsIds(orderItemsIds)
    returnValue
  }

  def orderDtoToEntity(order: OrderDto): OrderEntity = {
    val returnValue:OrderEntity = mapper.map(order, classOf[OrderEntity])
    val customerId = Optional.ofNullable(order.getCustomerId)
    if (customerId.isPresent) {
      val customerEntity = customerRepository.getById(customerId.get)
      returnValue.setCustomer(customerEntity)
    }
    val addressId = Optional.ofNullable(order.getAddressId)
    if (addressId.isPresent) {
      val addressEntity:OrderAddressEntity = orderAddressRepository.getById(addressId.get)
      returnValue.setAddress(addressEntity)
    }
    val orderItemsIds = Optional.ofNullable(order.getOrderItemsIds)
    val orderItems = new util.ArrayList[OrderItemEntity]
    if (!orderItemsIds.isEmpty) {
      for (itemId <- orderItemsIds.get.asScala) {
        val itemEntity = orderItemRepository.getById(itemId)
        orderItems.add(itemEntity)
      }
    }
    returnValue.setOrderItems(orderItems)
    returnValue
  }

  def orderItemEntityToDto(itemEntity: OrderItemEntity): OrderItemDto = {
    val returnValue = mapper.map(itemEntity, classOf[OrderItemDto])
    val orderEntity = Optional.ofNullable(itemEntity.getOrder)
    if (orderEntity.isPresent) returnValue.setOrderId(orderEntity.get.getOrderId)
    returnValue
  }

  def orderItemDtoToEntity(itemDto: OrderItemDto): OrderItemEntity = {
    val returnValue = mapper.map(itemDto, classOf[OrderItemEntity])
    val orderId = Optional.ofNullable(itemDto.getOrderId)
    if (orderId.isPresent) {
      val orderEntity = orderRepository.getById(orderId.get)
      returnValue.setOrder(orderEntity)
    }
    returnValue
  }

  def productEntityToDto(productEntity: ProductEntity): ProductDto = {
    val returnValue = mapper.map(productEntity, classOf[ProductDto])
    returnValue
  }

  def productDtoToEntity(product: ProductDto): ProductEntity = {
    val returnValue = mapper.map(product, classOf[ProductEntity])
    returnValue
  }

  def userEntityToDto(userEntity: UserEntity): UserDto = {
    val returnValue = mapper.map(userEntity, classOf[UserDto])
    returnValue.setEnabled(userEntity.getEnabled)
    val roles = Optional.ofNullable(userEntity.getRoles)
    val rolesIds = new util.ArrayList[Integer]
    if (!roles.isEmpty) {
      for (roleEntity <- roles.get.asScala) {
        rolesIds.add(roleEntity.getId)
      }
    }
    returnValue.setRolesIds(rolesIds)
    returnValue
  }

  def userDtoToEntity(userDto: UserDto): UserEntity = {
    val returnValue = mapper.map(userDto, classOf[UserEntity])
    val roles = new util.ArrayList[RoleEntity]
    val rolesIds = Optional.ofNullable(userDto.getRolesIds)
    if (!rolesIds.isEmpty) {
      for (roleId <- rolesIds.get.asScala) {
        val role = roleRepository.getById(roleId)
        roles.add(role)
      }
    }
    returnValue.setRoles(roles)
    returnValue
  }

  def roleEntityToDto(roleEntity: RoleEntity): RoleDto = {
    val returnValue = mapper.map(roleEntity, classOf[RoleDto])
    val users = Optional.ofNullable(roleEntity.getUsers)
    val userIds = new util.ArrayList[Integer]
    if (!users.isEmpty) {
      for (user <- users.get.asScala) {
        userIds.add(user.getId)
      }
    }
    returnValue.setUsersIds(userIds)
    returnValue
  }

  def roleDtoToEntity(roleDto: RoleDto): RoleEntity = {
    val returnValue = mapper.map(roleDto, classOf[RoleEntity])
    val usersIds = Optional.ofNullable(roleDto.getUsersIds)
    val users = new util.ArrayList[UserEntity]
    if (!usersIds.isEmpty) {
      for (userId <- usersIds.get.asScala) {
        val userEntity = userRepository.getById(userId)
        users.add(userEntity)
      }
    }
    returnValue.setUsers(users)
    returnValue
  }

  def cartItemDtoToOrderItemDto(cartItem: CartItemDto): OrderItemDto = {
    val returnValue = mapper.map(cartItem, classOf[OrderItemDto])
    returnValue
  }

  def cartItemEntityToOrderItemEntity(cartItem: CartItemEntity): OrderItemEntity = {
    val returnValue = mapper.map(cartItem, classOf[OrderItemEntity])
    returnValue
  }

  def cartItemToOrderItemEntity(cartItemEntity: CartItemEntity): OrderItemEntity = {
    val returnValue = mapper.map(cartItemEntity, classOf[OrderItemEntity])
    val product = Optional.ofNullable(cartItemEntity.getProduct)
    if (product.isPresent) {
      returnValue.setProductName(product.get.getProductName)
      returnValue.setProductPrice(product.get.getProductPrice)
    }
    returnValue
  }

  def addressEntityToDto(addressEntity: AddressEntity): AddressDto = {
    val returnValue = mapper.map(addressEntity, classOf[AddressDto])
    val customerEntity = Optional.ofNullable(addressEntity.getCustomer)
    if (customerEntity.isPresent) returnValue.setCustomerId(customerEntity.get.getCustomerId)
    returnValue
  }

  def addressDtoToEntity(address: AddressDto): AddressEntity = {
    val returnValue = mapper.map(address, classOf[AddressEntity])
    val customerId = Optional.ofNullable(address.getCustomerId)
    if (customerId.isPresent) {
      val customerEntity = customerRepository.getById(customerId.get)
      returnValue.setCustomer(customerEntity)
    }
    returnValue
  }

  def orderAddressEntityToDto(address: OrderAddressEntity): OrderAddressDto = {
    val returnValue = mapper.map(address, classOf[OrderAddressDto])
    val orderEntity = Optional.ofNullable(address.getOrder)
    if (orderEntity.isPresent) returnValue.setOrderId(orderEntity.get.getOrderId)
    returnValue
  }

  def orderAddressDtoToEntity(address: OrderAddressDto): OrderAddressEntity = {
    val returnValue = mapper.map(address, classOf[OrderAddressEntity])
    val orderId = Optional.ofNullable(address.getOrderId)
    if (orderId.isPresent) {
      val orderEntity = orderRepository.getById(orderId.get)
      returnValue.setOrder(orderEntity)
    }
    returnValue
  }

  def addressToOrderAddress(address: AddressEntity): OrderAddressEntity = {
    val returnValue = mapper.map(address, classOf[OrderAddressEntity])
    returnValue
  }
}

