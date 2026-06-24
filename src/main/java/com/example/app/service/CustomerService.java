package com.example.app.service;

import java.util.List;

import com.example.app.domain.Customer;

public interface CustomerService {
	List<Customer> getAllCustomers();

	Customer getCustomerById(Integer id);

	// 💡 修正：page 引数を追加
	List<Customer> searchCustomers(Integer id, String name, String phone, String email, int page);

	// 💡 追加：総件数から総ページ数を計算して取得するメソッド
	int getTotalPages(Integer id, String name, String phone, String email);
}