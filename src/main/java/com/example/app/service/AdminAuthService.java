package com.example.app.service;

import com.example.app.domain.Admin;

public interface AdminAuthService {
	Admin authenticate(String id, String password);
}
