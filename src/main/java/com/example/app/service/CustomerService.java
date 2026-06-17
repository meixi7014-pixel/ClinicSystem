package com.example.app.service;

import java.util.List;

import com.example.app.domain.Customer;

public interface CustomerService {
	List<Customer> getAllCustomers();

	Customer getCustomerById(Integer id);
}
