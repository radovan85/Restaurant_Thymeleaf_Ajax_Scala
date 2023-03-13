package com.radovan.spring.controller

import java.security.Principal
import java.util.Optional

import com.radovan.spring.exceptions.InvalidUserException
import com.radovan.spring.model.RegistrationForm
import com.radovan.spring.service.{AddressService, CustomerService, UserService}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.annotation.Secured
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.{ModelAttribute, RequestMapping, RequestMethod}
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Controller
class MainController {

  @Autowired
  private var customerService:CustomerService = _

  @Autowired
  private var userService:UserService = _

  @Autowired
  private var addressService:AddressService = _

  @RequestMapping(value = Array("/"), method = Array(RequestMethod.GET))
  def indexPage = "index"

  @RequestMapping(value = Array("/login"), method = Array(RequestMethod.GET))
  def login = "fragments/login :: ajaxLoadedContent"

  @RequestMapping(value = Array("/home"), method = Array(RequestMethod.GET))
  def homePage = "fragments/homePage :: ajaxLoadedContent"

  @RequestMapping(value = Array("/userRegistration"), method = Array(RequestMethod.GET))
  def registration(map: ModelMap): String = {
    val tempForm = new RegistrationForm
    map.put("tempForm", tempForm)
    "fragments/registration :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/saveUser"), method = Array(RequestMethod.POST))
  def createUser(@ModelAttribute("tempForm") tempForm: RegistrationForm): String = {
    customerService.registerCustomer(tempForm)
    "fragments/homePage :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/registerComplete"), method = Array(RequestMethod.GET))
  def registrationCompleted = "fragments/registration_completed :: ajaxLoadedContent"

  @RequestMapping(value = Array("/registerFail"), method = Array(RequestMethod.GET))
  def registrationFailed = "fragments/registration_failed :: ajaxLoadedContent"

  @RequestMapping(value = Array("/loginErrorPage"), method = Array(RequestMethod.GET))
  def logError(map: ModelMap): String = {
    map.put("alert", "Invalid username or password!")
    "fragments/login :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/loginPassConfirm"), method = Array(RequestMethod.POST))
  def confirmLoginPass(principal: Principal): String = {
    val authPrincipal = Optional.ofNullable(principal)
    if (!authPrincipal.isPresent) {
      val error = new Error("Invalid user")
      throw new InvalidUserException(error)
    }
    "fragments/homePage :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/loggedout"), method = Array(RequestMethod.POST))
  def logout(redirectAttributes: RedirectAttributes): String = {
    SecurityContextHolder.clearContext()
    "fragments/homePage :: ajaxLoadedContent"
  }

  @Secured(value = Array("ROLE_USER"))
  @RequestMapping(value = Array("/accountInfo"))
  def userAccountInfo(map: ModelMap): String = {
    val authUser = userService.getCurrentUser
    val customer = customerService.getCustomerByUserId(authUser.getId)
    val address = addressService.getAddressById(customer.getAddressId)
    map.put("authUser", authUser)
    map.put("address", address)
    "fragments/accountDetails :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/about"), method = Array(RequestMethod.GET))
  def aboutPage = "fragments/about :: ajaxLoadedContent"
}

