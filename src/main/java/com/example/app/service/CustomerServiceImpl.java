package com.example.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.app.domain.Customer;
import com.example.app.mapper.CustomerMapper;

@Service
public class CustomerServiceImpl implements CustomerService {
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
}
