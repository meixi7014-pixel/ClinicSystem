package com.example.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.app.domain.Admin;
import com.example.app.mapper.AdminMapper;

@Service
public class AdminAuthServiceImpl implements AdminAuthService {
	@Autowired
	private AdminMapper adminMapper;

	// 💡 パスワード照合用のエンコーダーを用意（手動でインスタンス化、または標準Bean定義）
	private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@Override
	public Admin authenticate(String id, String password) {
		// DBから管理者情報を取得
		Admin admin = adminMapper.findById(id);

		// 管理者が存在し、かつ入力された平文パスワードがDBのハッシュ化パスワードと一致するか検証
		if (admin != null && passwordEncoder.matches(password, admin.getPasswordHash())) {
			return admin; // 認証成功
		}
		return null; // 認証失敗
	}
}
