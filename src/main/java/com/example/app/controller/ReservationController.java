package com.example.app.controller;

import java.time.LocalDateTime;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.app.domain.Customer;
import com.example.app.domain.Reservation;
import com.example.app.service.ReservationService;

@Controller
@RequestMapping("/reservation")
public class ReservationController {
	@Autowired
	private ReservationService reservationService;

	// ①予約日時選択画面
	@GetMapping
	public String showReservationPage(HttpSession session) {
		session.invalidate();
		return "reservation";
	}

	// ②顧客情報入力画面
	@PostMapping("/customer")
	public String handleReservation(
			@RequestParam("clinic") String clinic,
			@RequestParam("selected_date") String selectedDate,
			@RequestParam("timeslot") String timeslot,
			HttpSession session, Model model) {
		LocalDateTime reservedAt = LocalDateTime.parse(selectedDate + "T" + timeslot);
		Reservation reservation = new Reservation();
		reservation.setReservedAt(reservedAt);

		session.setAttribute("scopedTarget.reservation", reservation);
		session.setAttribute("selectedClinic", clinic);

		model.addAttribute("customer", new Customer());
		return "customer";
	}

	// ③情報確認画面
	@PostMapping("/confirm")
	public String handleCustomerInfo(
			@Validated Customer customer,
			BindingResult result,
			HttpSession session, Model model) {
		// 入力不備がある場合は②に戻る
		if (result.hasErrors()) {
			return "customer";
		}

		// 情報をセッションに保存
		session.setAttribute("scopedTarget.customer", customer);

		Reservation reservation = (Reservation) session.getAttribute("scopedTarget.reservation");
		String clinic = (String) session.getAttribute("selectedClinic");

		model.addAttribute("clinic", clinic);
		model.addAttribute("reservation", reservation);
		model.addAttribute("customer", customer);

		return "confirm";

	}

	// ④完了画面

	@PostMapping("/complete")
	public String handleComplete(HttpSession session) {
		Customer customer = (Customer) session.getAttribute("scopedTarget.customer");
		Reservation reservation = (Reservation) session.getAttribute("scopedTarget.reservation");

		// セッションが切れた場合はTOPにリダイレクト
		if (customer == null || reservation == null) {
			return "redirect:/reservation";
		}

		reservationService.registerBooking(customer, reservation);

		session.invalidate();

		return "complete";

	}

}
