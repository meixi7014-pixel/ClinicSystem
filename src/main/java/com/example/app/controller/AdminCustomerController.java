package com.example.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable; // 💡 追記
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
		List<Customer> customerList = customerService.getAllCustomers();
		model.addAttribute("customers", customerList);
		return "admin/customers/list";
	}

	// 💡 追記：顧客詳細画面の表示 (GET: /admin/customers/{id})
	@GetMapping("/{id}")
	public String showCustomerDetail(@PathVariable("id") Integer id, Model model) {
		// IDをキーにデータベースから顧客を1件取得
		Customer customer = customerService.getCustomerById(id);

		// 該当する顧客がいない場合の簡易安全対策
		if (customer == null) {
			return "redirect:/admin/customers";
		}

		// Thymeleafに顧客情報を渡す
		model.addAttribute("customer", customer);

		// src/main/resources/templates/admin/customers/detail.html を呼び出す
		return "admin/customers/detail";
	}
}