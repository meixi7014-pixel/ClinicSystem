package com.example.app.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.app.domain.Customer;

@Mapper
public interface CustomerMapper {

	void insertCustomer(Customer customer);

	// 重複件数を取得
	int countByPhoneNumber(String phoneNumber);

	int countByEmail(String email);

	List<Customer> findAllCustomers();

	Customer findCustomerById(Integer id);

	List<Customer> searchCustomers(
			@Param("id") Integer id,
			@Param("name") String name,
			@Param("phone") String phone,
			@Param("email") String email);
}