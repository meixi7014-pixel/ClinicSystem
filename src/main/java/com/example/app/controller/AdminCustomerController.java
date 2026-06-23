package com.example.app.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.app.domain.Customer;
import com.example.app.mapper.ReservationMapper; // 💡 追記：マッパーをインポート
import com.example.app.service.CustomerService;

@Controller
@RequestMapping("/admin/customers")
public class AdminCustomerController {

	@Autowired
	private CustomerService customerService;

	@Autowired
	private ReservationMapper reservationMapper; // 💡 追記：予約マッパーを注入

	@GetMapping
	public String showCustomerList(
			@RequestParam(name = "searchId", required = false) Integer searchId,
			@RequestParam(name = "searchName", required = false) String searchName,
			@RequestParam(name = "searchPhone", required = false) String searchPhone,
			@RequestParam(name = "searchEmail", required = false) String searchEmail,
			Model model) {

		// 💡 修正：すべての顧客を取得するのではなく、検索条件に合う顧客を取得する
		// （※もし現在 Service クラスを経由している場合は、Service にこれらの引数を渡すようにしてください）
		// ここではMapperを直接、またはServiceを介して呼び出す実装に合わせます
		List<Customer> customerList = customerService.searchCustomers(searchId, searchName, searchPhone, searchEmail);

		model.addAttribute("customers", customerList);

		// 💡 追加：検索条件を保持するために画面に送り返す
		model.addAttribute("searchId", searchId);
		model.addAttribute("searchName", searchName);
		model.addAttribute("searchPhone", searchPhone);
		model.addAttribute("searchEmail", searchEmail);

		return "admin/customers/list";
	}

	// 💡 修正：顧客詳細画面の表示 (GET: /admin/customers/{id})
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

		// 💡 追記：該当顧客の最新の予約データをデータベースから取得
		Map<String, Object> latestReservation = reservationMapper.findLatestReservationByCustomerId(id);

		// 💡 追記：HTML（Thymeleaf）へデータを引き渡す
		if (latestReservation != null) {
			model.addAttribute("reservation", latestReservation);
		} else {
			model.addAttribute("reservation", null);
		}

		// src/main/resources/templates/admin/customers/detail.html を呼び出す
		return "admin/customers/detail";
	}
}