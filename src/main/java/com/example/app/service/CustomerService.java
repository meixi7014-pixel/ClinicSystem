package com.example.app.service;

import java.util.List;

import com.example.app.domain.Customer;

public interface CustomerService {
	List<Customer> getAllCustomers();

	Customer getCustomerById(Integer id);

	// 💡 追記：検索処理用のメソッド定義
	List<Customer> searchCustomers(Integer id, String name, String phone, String email);
}