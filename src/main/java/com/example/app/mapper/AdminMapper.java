package com.example.app.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.example.app.domain.Admin;

@Mapper
public interface AdminMapper {
	//IDをキーに管理者情報を取得
	Admin findById(String id);
}
