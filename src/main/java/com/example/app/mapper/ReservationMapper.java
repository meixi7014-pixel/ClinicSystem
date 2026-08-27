package com.example.app.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.app.domain.Reservation;

@Mapper
public interface ReservationMapper {
	void insertReservation(Reservation reservation);

	List<Map<String, Object>> findWeeklyReservationCounts(
			@Param("startDate") String startDate,
			@Param("endDate") String endDate);

	List<Map<String, Object>> findReservationDetailsByDate(String targetDate);

	List<String> findReservedTimesByDate(@Param("targetDate") String targetDate);

	Map<String, Object> findLatestReservationByCustomerId(Integer customerId);
}
