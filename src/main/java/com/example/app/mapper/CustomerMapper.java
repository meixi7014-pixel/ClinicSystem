package com.example.app.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.example.app.domain.Customer;

@Mapper
public interface CustomerMapper {

	void insertCustomer(Customer customer);

}
