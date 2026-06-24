package com.example.app.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.app.domain.Customer;

@Mapper
public interface CustomerMapper {

	void insertCustomer(Customer customer);

	int countByPhoneNumber(String phoneNumber);

	int countByEmail(String email);

	List<Customer> findAllCustomers();

	Customer findCustomerById(Integer id);

	// 💡 修正：limit と offset を引数に追加
	List<Customer> searchCustomers(
			@Param("id") Integer id,
			@Param("name") String name,
			@Param("phone") String phone,
			@Param("email") String email,
			@Param("limit") int limit,
			@Param("offset") int offset);

	// 💡 追加：条件に該当する総件数を取得
	int countSearchCustomers(
			@Param("id") Integer id,
			@Param("name") String name,
			@Param("phone") String phone,
			@Param("email") String email);
}