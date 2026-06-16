package com.example.app.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.app.domain.Admin;
import com.example.app.service.AdminAuthService;

@Controller
@RequestMapping("/admin")
public class AdminAuthController {
	@Autowired
	private AdminAuthService adminAuthService;

	// 1. ログイン画面の表示 (GET: /admin/login)
	@GetMapping("/login")
	public String showLoginPage() {
		return "admin/login";
	}

	// 2. ログイン認証処理 (POST: /admin/login)
	@PostMapping("/login")
	public String login(
			@RequestParam("adminId") String id,
			@RequestParam("password") String password,
			HttpSession session,
			Model model) {

		Admin admin = adminAuthService.authenticate(id, password);

		if (admin != null) {
			// ログイン成功：セッションに管理者情報を保存してTOPへ
			session.setAttribute("loginAdmin", admin);
			return "redirect:/admin";
		} else {
			// ログイン失敗：エラーメッセージを渡してログイン画面へ戻る
			model.addAttribute("errorMessage", "管理者IDまたはパスワードが正しくありません。");
			// 入力されたIDを画面に残すための保持
			model.addAttribute("typedId", id);
			return "admin/login";
		}
	}

	// 3. 管理TOPページの表示 (GET: /admin)
	@GetMapping
	public String showAdminTop(HttpSession session) {
		// 簡易ログインチェック（セッションがなければログイン画面へ強制リダイレクト）
		if (session.getAttribute("loginAdmin") == null) {
			return "redirect:/admin/login";
		}
		return "admin/index";
	}
}
