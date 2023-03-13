package com.radovan.spring.service.impl

import java.util.Optional

import com.radovan.spring.converter.TempConverter
import com.radovan.spring.dto.AddressDto
import com.radovan.spring.repository.AddressRepository
import com.radovan.spring.service.AddressService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class AddressServiceImpl extends AddressService {

  @Autowired
  private var addressRepository:AddressRepository = _

  @Autowired
  private var tempConverter:TempConverter = _

  override def getAddressById(addressId: Integer): AddressDto = {
    var returnValue:AddressDto = null
    val addressEntity = Optional.ofNullable(addressRepository.getById(addressId))
    if (addressEntity.isPresent) returnValue = tempConverter.addressEntityToDto(addressEntity.get)
    returnValue
  }

  override def createAddress(address: AddressDto): AddressDto = {
    val addressEntity = tempConverter.addressDtoToEntity(address)
    val storedAddress = addressRepository.save(addressEntity)
    val returnValue = tempConverter.addressEntityToDto(storedAddress)
    returnValue
  }

  override def deleteAddress(addressId: Integer): Unit = {
    addressRepository.deleteById(addressId)
    addressRepository.flush()
  }
}

