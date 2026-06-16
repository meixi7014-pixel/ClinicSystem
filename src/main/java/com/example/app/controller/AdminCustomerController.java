package com.example.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.app.domain.Customer;
import com.example.app.service.CustomerService;

@Controller
@RequestMapping("/admin/customers")
public class AdminCustomerController {
	@Autowired
	private CustomerService customerService;

	@GetMapping
	public String showCustomerList(Model model) {
		// データベースから顧客一覧を取得
		List<Customer> customerList = customerService.getAllCustomers();

		// Thymeleafにデータを渡す（属性名は "customers"）
		model.addAttribute("customers", customerList);

		// src/main/resources/templates/admin/customers/list.html を呼び出す
		return "admin/customers/list";
	}
}
