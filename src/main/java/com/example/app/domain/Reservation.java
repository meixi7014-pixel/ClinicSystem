package com.example.app.domain;

import java.time.LocalDateTime;

public class Reservation {

	private Integer id;
	private Integer customerId;

	// 予約ステータス（0：仮予約 1：本予約 2：キャンセル）
	private Integer status;

	// 予約枠の日時
	private LocalDateTime reservedAt;

	// 仮予約の有効期限（将来使用）
	private LocalDateTime temporaryExpiresAt;

	// キャンセル日時
	private LocalDateTime canceledAt;

	// 予約データの作成日時
	private LocalDateTime createdAt;

}