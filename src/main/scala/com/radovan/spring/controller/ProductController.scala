package com.radovan.spring.controller

import java.util

import com.radovan.spring.dto.ProductDto
import com.radovan.spring.service.ProductService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.{PathVariable, RequestMapping, RequestMethod}

@Controller
@RequestMapping(value = Array("/products"))
class ProductController {

  @Autowired
  private var productService: ProductService = _

  @RequestMapping(value = Array("/allProducts"), method = Array(RequestMethod.GET))
  def allProductsList(map: ModelMap): String = {
    val allProducts: util.List[ProductDto] = productService.listAll
    val product = new ProductDto
    var recordsPerPage:Integer = 9;
    map.put("product", product)
    map.put("allProducts", allProducts)
    map.put("recordsPerPage", recordsPerPage)
    "fragments/productList :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/allProductsByCategory/{category}"), method = Array(RequestMethod.GET))
  def allProductsByCategory(@PathVariable("category") category: String, map: ModelMap): String = {
    val allProducts: util.List[ProductDto] = productService.listByCategory(category)
    val product = new ProductDto
    map.put("product", product)
    map.put("allProducts", allProducts)
    "fragments/productList :: ajaxLoadedContent"
  }
}

