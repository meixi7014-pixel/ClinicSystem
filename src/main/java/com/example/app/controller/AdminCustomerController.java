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
import com.example.app.mapper.ReservationMapper;
import com.example.app.service.CustomerService;

@Controller
@RequestMapping("/admin/customers")
public class AdminCustomerController {

	@Autowired
	private CustomerService customerService;

	@Autowired
	private ReservationMapper reservationMapper;

	// 💡 修正：現在のページ番号を受け取るために「@RequestParam(name = "page", defaultValue = "1") int page」を追加
	@GetMapping
	public String showCustomerList(
			@RequestParam(name = "searchId", required = false) Integer searchId,
			@RequestParam(name = "searchName", required = false) String searchName,
			@RequestParam(name = "searchPhone", required = false) String searchPhone,
			@RequestParam(name = "searchEmail", required = false) String searchEmail,
			@RequestParam(name = "page", defaultValue = "1") int page,
			Model model) {

		// 💡 修正：引数に page を追加し、指定されたページの20件のみを取得する
		List<Customer> customerList = customerService.searchCustomers(searchId, searchName, searchPhone, searchEmail, page);

		// 💡 追加：検索条件に合致するデータ全体の総ページ数を取得する
		int totalPages = customerService.getTotalPages(searchId, searchName, searchPhone, searchEmail);

		model.addAttribute("customers", customerList);

		// 💡 追加：現在のページ番号と総ページ数をHTML（Thymeleaf）に送る
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", totalPages);

		// 検索条件を保持するために画面に送り返す（ページ切り替えリンクでも使用します）
		model.addAttribute("searchId", searchId);
		model.addAttribute("searchName", searchName);
		model.addAttribute("searchPhone", searchPhone);
		model.addAttribute("searchEmail", searchEmail);

		return "admin/customers/list";
	}

	// 💡 変更なし：顧客詳細画面の表示 (GET: /admin/customers/{id})
	@GetMapping("/{id}")
	public String showCustomerDetail(@PathVariable("id") Integer id, Model model) {
		Customer customer = customerService.getCustomerById(id);

		if (customer == null) {
			return "redirect:/admin/customers";
		}

		model.addAttribute("customer", customer);

		Map<String, Object> latestReservation = reservationMapper.findLatestReservationByCustomerId(id);

		if (latestReservation != null) {
			model.addAttribute("reservation", latestReservation);
		} else {
			model.addAttribute("reservation", null);
		}

		return "admin/customers/detail";
	}
}