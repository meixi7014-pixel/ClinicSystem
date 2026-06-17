package com.example.app.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean; // 💡 追記
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import com.example.app.filter.AdminAuthFilter; // 💡 追記

@Configuration // 💡 もしコメントアウトしていた場合は解除して有効にしてください
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(auth -> auth
						.requestMatchers(
								"/",
								"/reservation/**",
								"/css/**",
								"/js/**",
								"/admin/**") // 💡 /admin/** も含めてSpring Security側はすべて通過させる
						.permitAll()
						.anyRequest().permitAll())
				.formLogin(login -> login.disable());

		return http.build();
	}

	// 💡 追記：自作した AdminAuthFilter を /admin/* のURL全体に適用する設定
	@Bean
	public FilterRegistrationBean<AdminAuthFilter> adminAuthFilterRegistration() {
		FilterRegistrationBean<AdminAuthFilter> bean = new FilterRegistrationBean<>();
		bean.setFilter(new AdminAuthFilter());
		bean.addUrlPatterns("/admin/*"); // 💡 /admin 配下のすべてのページを対象にする
		bean.setOrder(1); // 実行順序を明示（Spring Securityの後に動かす）
		return bean;
	}
}