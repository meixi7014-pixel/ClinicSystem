package com.example.app.filter;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class AdminAuthFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		HttpSession session = httpRequest.getSession(false);

		String requestUri = httpRequest.getRequestURI();
		String contextPath = httpRequest.getContextPath();

		// 💡 ログイン画面の表示(GET)と認証処理(POST)へのアクセス時は、チェックをスキップして通過させる
		if (requestUri.equals(contextPath + "/admin/login")) {
			chain.doFilter(request, response);
			return;
		}

		// 💡 セッションが存在しない、または管理者情報が登録されていない場合はログイン画面へリダイレクト
		if (session == null || session.getAttribute("loginAdmin") == null) {
			httpResponse.sendRedirect(contextPath + "/admin/login");
			return; // 処理をここで中断
		}

		// 💡 ログイン状態であれば、次の処理（コントローラーなど）へ進める
		chain.doFilter(request, response);
	}
}