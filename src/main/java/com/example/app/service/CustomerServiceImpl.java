package com.example.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.app.domain.Customer;
import com.example.app.mapper.CustomerMapper;

@Service
public class CustomerServiceImpl implements CustomerService {

	private static final int PAGE_SIZE = 20; // 💡 1ページあたりの表示件数を20件に固定

	@Autowired
	private CustomerMapper customerMapper;

	@Override
	public List<Customer> getAllCustomers() {
		return customerMapper.findAllCustomers();
	}

	@Override
	public Customer getCustomerById(Integer id) {
		return customerMapper.findCustomerById(id);
	}

	@Override
	public List<Customer> searchCustomers(Integer id, String name, String phone, String email, int page) {
		// 💡 ページ番号（1から始まる）をもとにOFFSETを計算
		int offset = (page - 1) * PAGE_SIZE;
		return customerMapper.searchCustomers(id, name, phone, email, PAGE_SIZE, offset);
	}

	@Override
	public int getTotalPages(Integer id, String name, String phone, String email) {
		// 💡 該当する総件数を取得し、総ページ数を繰り上げ（Math.ceil）で計算
		int totalCustomers = customerMapper.countSearchCustomers(id, name, phone, email);
		return (int) Math.ceil((double) totalCustomers / PAGE_SIZE);
	}
}