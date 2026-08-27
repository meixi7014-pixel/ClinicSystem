package com.example.app.controller;

import java.time.LocalDateTime;
import java.util.List;

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
import org.springframework.web.bind.annotation.ResponseBody;

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
		return "reservation/reservation";
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
		return "reservation/customer";
	}

	// ③情報確認画面
	@PostMapping("/confirm")
	public String handleCustomerInfo(
			@Validated Customer customer,
			BindingResult result,
			HttpSession session, Model model) {

		// バリデーションエラー（入力不備）がある場合は入力画面(customer.html)に引き戻す
		if (result.hasErrors()) {
			Reservation reservation = (Reservation) session.getAttribute("scopedTarget.reservation");
			String clinic = (String) session.getAttribute("selectedClinic");

			model.addAttribute("clinic", clinic);
			model.addAttribute("reservation", reservation);

			return "reservation/customer";
		}

		// 情報をセッションに保存
		session.setAttribute("scopedTarget.customer", customer);

		Reservation reservation = (Reservation) session.getAttribute("scopedTarget.reservation");
		String clinic = (String) session.getAttribute("selectedClinic");

		// 17歳以下に同意書表示
		boolean isUnder18 = false;
		if (customer.getBirthDate() != null && customer.getBirthDate().length() == 8) {
			try {
				int birthYear = Integer.parseInt(customer.getBirthDate().substring(0, 4));
				int birthMonth = Integer.parseInt(customer.getBirthDate().substring(4, 6));
				int birthDay = Integer.parseInt(customer.getBirthDate().substring(6, 8));

				java.time.LocalDate birthday = java.time.LocalDate.of(birthYear, birthMonth, birthDay);
				java.time.LocalDate today = java.time.LocalDate.now();

				int age = java.time.Period.between(birthday, today).getYears();

				if (age <= 17) {
					isUnder18 = true;
				}
			} catch (Exception e) {
				// パース失敗時は安全のためfalseのまま
			}
		}

		// 確実に確認画面(confirm.html)へすべてのデータを引き渡す
		model.addAttribute("clinic", clinic);
		model.addAttribute("reservation", reservation);
		model.addAttribute("customer", customer);
		model.addAttribute("isUnder18", isUnder18); // ★追加：確認画面でのリンク制御用

		return "reservation/confirm";
	}

	//③確認画面から②入力画面に「戻る」ボタンで戻ってきたときのGET処理（完全防衛版）
	@GetMapping("/customer")
	public String backToCustomer(HttpSession session, Model model) {
		Customer customer = (Customer) session.getAttribute("scopedTarget.customer");
		Reservation reservation = (Reservation) session.getAttribute("scopedTarget.reservation");
		String clinic = (String) session.getAttribute("selectedClinic");

		// もしセッションが切れて「予約日時」すら消えていた場合、強制的に1ページ目に飛ばす
		if (reservation == null) {
			return "redirect:/reservation";
		}

		// 顧客情報だけが何らかの理由で空だった場合は、画面割れを防ぐために空のオブジェクトを入れる
		if (customer == null) {
			customer = new Customer();
		}

		// customer.htmlの上部にあるクリニック・日時表示がエラーで割れないように積み直す
		model.addAttribute("clinic", clinic);
		model.addAttribute("reservation", reservation);
		model.addAttribute("customer", customer);

		return "reservation/customer";
	}

	//④完了画面
	@PostMapping("/complete")
	public String handleComplete(HttpSession session, Model model) {
		Customer customer = (Customer) session.getAttribute("scopedTarget.customer");
		Reservation reservation = (Reservation) session.getAttribute("scopedTarget.reservation");
		String clinic = (String) session.getAttribute("selectedClinic");

		// セッション切れの場合はTOPにリダイレクト
		if (customer == null || reservation == null) {
			return "redirect:/reservation";
		}

		// サービス層を呼び出してDBに登録
		reservationService.registerBooking(customer, reservation);

		// 画面（complete.html）で表示するためにデータをModelへ引越し
		model.addAttribute("completeClinic", clinic);
		model.addAttribute("completeReservation", reservation);

		// 次の予約に備えてセッションから予約データを削除
		session.removeAttribute("scopedTarget.customer");
		session.removeAttribute("scopedTarget.reservation");
		session.removeAttribute("selectedClinic");

		return "reservation/complete";
	}

	@GetMapping("/api/reserved-times")
	@ResponseBody
	public List<String> getReservedTimes(@RequestParam("date") String date) {
		return reservationService.getReservedTimesByDate(date);
	}

}