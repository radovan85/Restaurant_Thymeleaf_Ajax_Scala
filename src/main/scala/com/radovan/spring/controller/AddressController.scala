package com.radovan.spring.controller

import com.radovan.spring.dto.AddressDto
import com.radovan.spring.service.AddressService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.{ModelAttribute, PathVariable, RequestMapping, RequestMethod}

@Controller
@RequestMapping(value = Array("/addresses"))
class AddressController {

  @Autowired
  private var addressService:AddressService = _

  @RequestMapping(value = Array("/updateAddress/{addressId}"))
  def renderAddressForm(@PathVariable("addressId") addressId: Integer, map: ModelMap): String = {
    val address = new AddressDto
    val currentAddress = addressService.getAddressById(addressId)
    map.put("address", address)
    map.put("currentAddress", currentAddress)
    "fragments/updateAddressForm :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/createAddress"), method = Array(RequestMethod.POST))
  def createAddress(@ModelAttribute("address") address: AddressDto): String = {
    addressService.createAddress(address)
    "fragments/homePage :: ajaxLoadedContent"
  }
}

