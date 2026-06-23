package com.example.app.controller;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters; // 💡 追記：日付の調整用
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParseException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.app.mapper.ReservationMapper;

import tools.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("/admin/reservations")
public class AdminReservationController {

	@Autowired
	private ReservationMapper reservationMapper;

	@GetMapping("/weekly")
	public String showWeeklyCalendar(
			@RequestParam(name = "date", required = false) String dateStr,
			Model model) throws JsonParseException {

		// 1. 基準となる日付を決定する（パラメータがあればそれを解析、なければ今日）
		LocalDate baseDate;
		DateTimeFormatter paramFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");

		if (dateStr != null && dateStr.matches("^[0-9]{8}$")) {
			try {
				baseDate = LocalDate.parse(dateStr, paramFormatter);
			} catch (Exception e) {
				baseDate = LocalDate.now();
			}
		} else {
			baseDate = LocalDate.now();
		}

		// 2. 💡 修正：基準日を基に「直近の過去または当日の日曜日」を正しく計算する
		// これにより、6/23（火）から見て 6/21（日）が正しくスタート日になります
		LocalDate startDate = baseDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
		LocalDate endDate = startDate.plusWeeks(1); // 翌週の日曜日

		// 前の週（-1週）と次の週（+1週）のボタン用URL日付を計算
		String prevWeekStr = startDate.minusWeeks(1).format(paramFormatter);
		String nextWeekStr = startDate.plusWeeks(1).format(paramFormatter);

		// SQL検索用の「YYYY-MM-DD」形式文字列
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String startDateSql = startDate.format(dtf) + " 00:00:00";
		String endDateSql = endDate.format(dtf) + " 00:00:00";

		// 3. データベースから該当週のデータを取得
		List<Map<String, Object>> weeklyCounts = reservationMapper.findWeeklyReservationCounts(startDateSql, endDateSql);

		// 4. JSONに変換
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonCounts = objectMapper.writeValueAsString(weeklyCounts);

		// 5. HTML（Thymeleaf）にデータを渡す
		model.addAttribute("jsonCounts", jsonCounts);
		model.addAttribute("prevWeek", prevWeekStr);
		model.addAttribute("nextWeek", nextWeekStr);
		model.addAttribute("baseDateStr", startDate.format(paramFormatter)); // カレンダー描画の基準日

		return "admin/reservations/weekly";
	}

	// 💡 変更なし：特定の日付の予約詳細一覧を表示する (GET: /admin/reservations/detail/{dateStr})
	@GetMapping("/detail/{dateStr}")
	public String showReservationDetail(@org.springframework.web.bind.annotation.PathVariable("dateStr") String dateStr,
			Model model) {

		String targetDateSql = "";
		String formattedDate = "";
		if (dateStr != null && dateStr.matches("^[0-9]{8}$")) {
			String year = dateStr.substring(0, 4);
			String month = dateStr.substring(4, 6);
			String day = dateStr.substring(6, 8);
			targetDateSql = year + "-" + month + "-" + day;
			formattedDate = year + "年" + Integer.parseInt(month) + "月" + Integer.parseInt(day) + "日";
		} else {
			return "redirect:/admin/reservations/weekly";
		}

		List<Map<String, Object>> reservations = reservationMapper.findReservationDetailsByDate(targetDateSql);

		List<Map<String, Object>> normalizedList = new java.util.ArrayList<>();
		for (Map<String, Object> original : reservations) {
			Map<String, Object> cleanMap = new java.util.HashMap<>();
			for (Map.Entry<String, Object> entry : original.entrySet()) {
				String key = entry.getKey().toLowerCase();
				if (key.equals("id"))
					cleanMap.put("id", entry.getValue());
				else if (key.equals("customer_id"))
					cleanMap.put("customerId", entry.getValue());
				else if (key.equals("customer_name"))
					cleanMap.put("customerName", entry.getValue());
				else if (key.equals("reserve_time"))
					cleanMap.put("reserveTime", entry.getValue());
			}
			normalizedList.add(cleanMap);
		}

		model.addAttribute("reservations", normalizedList);
		model.addAttribute("formattedDate", formattedDate);

		return "admin/reservations/detail";
	}
}