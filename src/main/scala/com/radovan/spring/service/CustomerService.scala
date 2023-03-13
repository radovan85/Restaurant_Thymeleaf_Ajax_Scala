package com.radovan.spring.service

import java.util
import com.radovan.spring.dto.CustomerDto
import com.radovan.spring.model.RegistrationForm

trait CustomerService {

  def addCustomer(customer: CustomerDto): CustomerDto

  def getCustomer(customerId: Integer): CustomerDto

  def getCustomerByUserId(userId: Integer): CustomerDto

  def listAll: util.List[CustomerDto]

  def updateCustomer(customerId: Integer, customer: CustomerDto): CustomerDto

  def registerCustomer(form: RegistrationForm): CustomerDto

  def getCustomerByCartId(cartId: Integer): CustomerDto

  def deleteCustomer(customerId: Integer): Unit

  def resetCustomer(customerId: Integer): Unit
}