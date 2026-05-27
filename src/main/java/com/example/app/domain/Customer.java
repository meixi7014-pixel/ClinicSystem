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

	@NotBlank(message = "メールアドレスを入力してください")
	@Email(message = "正しいメールアドレスの形式で入力してください")
	@Size(max = 255, message = "メールアドレスが長すぎます")
	private String email;

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
}