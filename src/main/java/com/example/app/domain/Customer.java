package com.example.app.domain;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import com.example.app.service.ReservationService;

import lombok.Data;

@Data
public class Customer {

	private Integer id;

	@NotBlank(message = "お名前を入力してください")
	@Size(max = 45, message = "お名前は45文字以内で入力してください")
	private String name;

	@NotBlank(message = "電話番号を入力してください")
	@Pattern(regexp = "^0[0-9]{9,10}$", message = "電話番号はハイフンなしの正しい桁数で入力してください")
	private String phoneNumber;

	@AssertTrue(message = "ご入力いただいた電話番号は、既に別の予約で登録されています")
	public boolean isUniquePhoneNumber() {
		if (this.phoneNumber == null || this.phoneNumber.isEmpty()) {
			return true; // 必須チェックは@NotBlank側で処理するためスルー
		}

		try {
			// SpringのコンテキストからReservationServiceの部品を強制的に引っ張り出す記述
			ReservationService service = org.springframework.web.context.support.WebApplicationContextUtils
					.getRequiredWebApplicationContext(
							((org.springframework.web.context.request.ServletRequestAttributes) org.springframework.web.context.request.RequestContextHolder
									.getRequestAttributes())
											.getRequest().getServletContext())
					.getBean(ReservationService.class);

			// データベースに問い合わせ、既に存在していれば不合格(false)を返す
			if (service.isPhoneNumberExists(this.phoneNumber)) {
				return false;
			}
		} catch (Exception e) {
			// 万が一システムエラーが起きた場合はログを出して通過させる（安全弁）
			return true;
		}
		return true;
	}

	@NotBlank(message = "メールアドレスを入力してください")
	@Email(message = "正しいメールアドレスの形式で入力してください")
	@Size(max = 255, message = "メールアドレスが長すぎます")
	private String email;

	@AssertTrue(message = "ご入力いただいたメールアドレスは、既に別の予約で登録されています")
	public boolean isUniqueEmail() {
		if (this.email == null || this.email.isEmpty()) {
			return true; // 必須チェックは@NotBlank側で処理するためスルー
		}

		try {
			ReservationService service = org.springframework.web.context.support.WebApplicationContextUtils
					.getRequiredWebApplicationContext(
							((org.springframework.web.context.request.ServletRequestAttributes) org.springframework.web.context.request.RequestContextHolder
									.getRequestAttributes())
											.getRequest().getServletContext())
					.getBean(ReservationService.class);

			// データベースに問い合わせ、既に存在していればfalseを返す
			if (service.isEmailExists(this.email)) {
				return false;
			}
		} catch (Exception e) {
			return true;
		}
		return true;
	}

	@NotBlank(message = "生年月日を入力してください")
	@Pattern(regexp = "^[0-9]{8}$", message = "生年月日は8桁の半角数字（例: 20001112）で入力してください")
	private String birthDate;

	@AssertTrue(message = "有効な生年月日を入力してください（16歳未満の方はご利用いただけません）")
	public boolean isValidBirthDate() {
		// 基礎バリデーションで弾かれている場合はスキップ
		if (birthDate == null || !birthDate.matches("^[0-9]{8}$")) {
			return true;
		}

		try {
			// 実在する日付以外NG
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuuMMdd")
					.withResolverStyle(java.time.format.ResolverStyle.STRICT);
			LocalDate parsedDate = LocalDate.parse(birthDate, formatter);

			// 年齢計算
			long age = ChronoUnit.YEARS.between(parsedDate, LocalDate.now());

			// 16～129歳以外NG
			if (age < 16 || age >= 130) {
				return false;
			}
			return true;

		} catch (DateTimeParseException e) {
			// 日付としてデタラメな数値の場合はエラー
			return false;
		}
	}

	//💡 画面表示用に「YYYY年M月D日（年齢 歳）」の文字列を返すメソッド
	public String getFormattedBirthDateAndAge() {
		// 💡 this.birthDate ではなく getBirthDate() を使用するように変更
		String currentBirthDate = getBirthDate();

		// 生年月日が未入力、または正しい8桁の数字でない場合は空文字を返す（安全対策）
		if (currentBirthDate == null || !currentBirthDate.matches("^[0-9]{8}$")) {
			return "";
		}

		try {
			// 1. "20001112" などの文字列を LocalDate 型に変換
			DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
			LocalDate parsedDate = LocalDate.parse(currentBirthDate, inputFormatter);

			// 2. 「yyyy年M月d日」の形式にフォーマット（1桁の月日は0埋めなし）
			DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy年M月d日");
			String formattedDate = parsedDate.format(outputFormatter);

			// 3. 現在の日付を基準に正確な年齢を計算
			long age = java.time.temporal.ChronoUnit.YEARS.between(parsedDate, LocalDate.now());

			// 4. 組み立てて返却
			return formattedDate + "（" + age + " 歳）";

		} catch (Exception e) {
			// 万が一の解析エラー時は、元の文字列をそのまま返す
			return currentBirthDate;
		}
	}

}